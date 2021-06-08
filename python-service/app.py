import json
import logging
import requests

from requests import ReadTimeout, ConnectionError, HTTPError, Timeout
from flask import Flask, jsonify
from faker import Faker
from faker_vehicle import VehicleProvider
from multiprocessing import Value, Lock


app = Flask(__name__)

fake = Faker('it_IT')
fake.add_provider(VehicleProvider)
counter_lock = Lock()
counter = Value('i', 0)  # defaults to 0
num_jobs = 10
num_cars = 100

logging.basicConfig(format='%(asctime)s - %(message)s', level=logging.INFO)

dict_cars = []


def create_single_fake_car():
    car = {
        'carPlate': fake.license_plate(),
        'carBrand': fake.vehicle_make(),
        'carModel': fake.vehicle_model()
    }
    return car


def add_single_fake_car_to_list_cars():
    global dict_cars
    for car in range(num_cars):
        dict_cars.append(create_single_fake_car())
    return dict_cars





def create_request(car):
    global dict_cars
    counter_thread = counter_increment(counter)
    #logging.info("Start Data Injection TASK N° %d", counter_thread)
    url = "http://localhost:9090/car"
    headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
    try:
        resp = requests.post(url, data=json.dumps(car, sort_keys=False), headers=headers, timeout=10)
        logging.info("Injection N° %d. Status code: %d", counter_thread, resp.status_code)
    except (ReadTimeout, ConnectionError, HTTPError, Timeout) as e:
        logging.error("Exception occurs on data injection. Technical details given below:")
        logging.error(e)
        return "Injection FAILED. Remote service unreachable", 500
    if resp.status_code != 201:
        logging.error("ERROR %d. Injection failed on object n° %d", resp.status_code, counter_thread)
        return "Injection FAILED", 400
    dict_cars.append(car)
    logging.info("End of data injection N° %d", counter_thread)
    return jsonify(car)



def counter_increment(self):
    with counter_lock:
        self.value += 1
    return self.value





if __name__ == '__main__':
    app.run()
