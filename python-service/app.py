import json
import logging
import requests

from requests import ReadTimeout, ConnectionError, HTTPError, Timeout
from flask import Flask, jsonify
from faker import Faker
from faker_vehicle import VehicleProvider

app = Flask(__name__)

fake = Faker('it_IT')
fake.add_provider(VehicleProvider)

logging.basicConfig(format='%(asctime)s - %(message)s', level=logging.INFO)

dict_cars = []


@app.route('/injectdata')
def push_single_car():
    logging.info("Start data injection")
    for i in range(10):
        car = {
            'carPlate': fake.license_plate(),
            'carBrand': fake.vehicle_make(),
            'carModel': fake.vehicle_model()
        }
        url = "http://localhost:9090/car"
        headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
        try:
            resp = requests.post(url, data=json.dumps(car, sort_keys=False), headers=headers, timeout=10)
            logging.info("Request n° %d - Status code: %d", i+1, resp.status_code)
        except (ReadTimeout, ConnectionError, HTTPError, Timeout) as e:
            logging.error("Exception occurs on data injection. Technical details given below:")
            logging.error(e)
            return "Injection FAILED. Remote service unreachable", 500
        if resp.status_code != 201:
            logging.error("ERROR %d. Injection failed on object n° %d", resp.status_code, i+1)
            return "Injection FAILED", 400
        dict_cars.append(car)
    logging.info("End of data injection")
    return jsonify(dict_cars), 201


if __name__ == '__main__':
    app.run()
