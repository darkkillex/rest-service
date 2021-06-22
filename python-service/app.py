import json
import logging
import os
from time import time

import requests

from requests import ReadTimeout, ConnectionError, HTTPError, Timeout
from flask import Flask, jsonify, request, Response
from faker import Faker
from faker_vehicle import VehicleProvider
from multiprocessing import Value, Lock
from multiprocessing.pool import ThreadPool

app = Flask(__name__)

fake = Faker('it_IT')
fake.add_provider(VehicleProvider)
counter_lock = Lock()
counter = Value('i', 0)  # defaults to 0
counter_err = Value('i', 0)  # defaults to 0
counter_ok = Value('i', 0)
counter_success = 0
counter_error = 0
num_jobs = 10

logging.basicConfig(format='%(asctime)s - %(message)s', level=logging.INFO)


def get_environment_variable_for_url(env_var_name_1, env_var_name_2):
    #TODO Remember to set in the Environments Variables "JAVA_SERVICE_URL":"JAVA_SERVICE_PORT"/car"

    # Don't use localhost for the communication between container.
    # DOCKER CONTAINER URL= "http://java-container:8080/car"
    #   So, in the URL, we need to use the name of server container and the default (private) port. Ex 9090->8080
    # PYCHARM URL= "http://localhost:9090/car"
    #   it is used to connect it from Pycharm to java-container app or Intellij running app
    #   N.B. for containerize the whole service, switch back to the upper DOCKER CONTAINER URL
    env_var_1 = os.getenv(env_var_name_1)
    env_var_2 = os.getenv(env_var_name_2)
    logging.info(f"Loading URL Environment Variables '{env_var_name_1}' and '{env_var_name_2}'")
    if env_var_1 is None or env_var_2 is None:
        logging.info(f"Environment variable {env_var_name_1} or {env_var_name_2} "
                     f"was/were not found. Returning empty string")
        return ""
    return str(env_var_1 + ":" + env_var_2 + "/car")


URL_CONSTANT = get_environment_variable_for_url('JAVA_SERVICE_URL', 'JAVA_SERVICE_PORT')


def counter_increment(self):
    with counter_lock:
        self.value += 1
    return self.value


def log_ending_info_requests(string_kind_data_inj, start_time):
    logging.info("END of Data Injection in %s", string_kind_data_inj)
    logging.info("Duration of Data Injection in %s: %s", string_kind_data_inj, time() - start_time)
    logging.info("Successful requests: %d", counter_success)
    logging.info("Error requests occurred: %d", counter_error)


def create_single_fake_car():
    car = {
        'carPlate': fake.license_plate(),
        'carBrand': fake.vehicle_make(),
        'carModel': fake.vehicle_model()
    }
    return car


def create_list_cars(num_cars):
    list_cars = []
    for n_car in range(num_cars):
        list_cars.append(create_single_fake_car())
    logging.info('N° %d Cars was added to list', n_car + 1)
    return list_cars


def create_request(car):
    global counter_success, counter_error
    counter_thread = counter_increment(counter)
    headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
    try:
        resp = requests.post(url=URL_CONSTANT, data=json.dumps(car, sort_keys=False), headers=headers, timeout=10)
    except (ReadTimeout, ConnectionError, HTTPError, Timeout) as e:
        logging.error("Injection N° %d FAILED. Exception occurs on data injection. "
                      "Technical details given below:", counter_thread)
        logging.error(e)
    if resp.status_code != 201:
        counter_error = counter_increment(counter_err)
        logging.error("ERROR %d. Injection FAILED on object n° %d", resp.status_code, counter_thread)
    else:
        counter_success = counter_increment(counter_ok)
        logging.info("Injection SUCCESS on object n° %d", counter_thread)
    return {'Car Plate': car['carPlate'],
            'Status code': resp.status_code}


def get_car_number_from_url_parameter():
    number_of_cars_parameter = request.args.get('cars')
    if number_of_cars_parameter is None:
        number_of_cars_parameter = 1  # default value of car injected
    else:
        number_of_cars_parameter = int(number_of_cars_parameter)
    return number_of_cars_parameter


@app.route('/injectdataparallel')
def inject_data_in_parallel():
    start_time = time()
    try:
        number_of_cars_passed_in_the_parameter = get_car_number_from_url_parameter()
    except (ValueError, TypeError) as e:
        return {'Error message': 'wrong type parameter',
                'Status code': 500}, 500
    pool_threads = ThreadPool(num_jobs)
    list_results = pool_threads.map(create_request, create_list_cars(num_cars=number_of_cars_passed_in_the_parameter))
    log_ending_info_requests('Parallel', start_time=start_time)
    return jsonify(list_results)


@app.route('/injectdataseries')
def inject_data_in_series():
    start_time = time()
    #check if the number of cars passed in the parameter(...?cars=VALUE) is an integer
    try:
        number_of_cars_passed_in_the_parameter = get_car_number_from_url_parameter()
    except (ValueError, TypeError) as e:
        return {'Error message': 'wrong type parameter',
                'Status code': 500}, 500
    list_cars = create_list_cars(number_of_cars_passed_in_the_parameter)
    list_results = []
    for car in list_cars:
        list_results.append(create_request(car))
    log_ending_info_requests('Series', start_time=start_time)
    return jsonify(list_results)


if __name__ == '__main__':
    app.run()

