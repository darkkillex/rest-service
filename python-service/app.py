import json
import logging
from time import time

import requests

from requests import ReadTimeout, ConnectionError, HTTPError, Timeout
from flask import Flask, jsonify
from faker import Faker
from faker_vehicle import VehicleProvider
from multiprocessing import Value, Lock
from multiprocessing.pool import ThreadPool

app = Flask(__name__)

fake = Faker('it_IT')
fake.add_provider(VehicleProvider)
counter_lock = Lock()
counter = Value('i', 0)  # defaults to 0
num_jobs = 10
num_cars = 100

logging.basicConfig(format='%(asctime)s - %(message)s', level=logging.INFO)


def counter_increment(self):
    with counter_lock:
        self.value += 1
    return self.value


def create_single_fake_car():
    car = {
        'carPlate': fake.license_plate(),
        'carBrand': fake.vehicle_make(),
        'carModel': fake.vehicle_model()
    }
    return car


def create_list_cars():
    list_cars = []
    for n_car in range(num_cars):
        list_cars.append(create_single_fake_car())
    logging.info('N° %d Cars was added to list', n_car + 1)
    return list_cars


def create_request(car):
    counter_thread = counter_increment(counter)
    url = "http://localhost:9090/car"
    headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
    try:
        resp = requests.post(url, data=json.dumps(car, sort_keys=False), headers=headers, timeout=10)
    except (ReadTimeout, ConnectionError, HTTPError, Timeout) as e:
        logging.error("Injection N° %d FAILED. Exception occurs on data injection. "
                      "Technical details given below:", counter_thread)
        logging.error(e)
    if resp.status_code != 201:
        logging.error("ERROR %d. Injection FAILED on object n° %d", resp.status_code, counter_thread)
    else:
        logging.info("Injection SUCCESS on object n° %d", counter_thread)
    return {'Car Plate': car['carPlate'],
            'Status code': resp.status_code}


@app.route('/injectdata')
def inject_data():
    start_time = time()
    pool_threads = ThreadPool(num_jobs)
    list_results = pool_threads.map(create_request, create_list_cars())
    logging.info("END of Data Injection")
    logging.info('Duration of data Injection %s', time() - start_time)
    return jsonify(list_results)


if __name__ == '__main__':
    app.run()
