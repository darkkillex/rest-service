import json
import logging

import requests
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
        dict_cars.append(car)
        url = "http://localhost:9090/car"
        headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
        resp = requests.post(url, data=json.dumps(car, sort_keys=False), headers=headers)
        logging.info("Request n° %d - Status code: %d", i+1, resp.status_code)
    logging.info("End of data injection")
    return jsonify(dict_cars)


if __name__ == '__main__':
    app.run()
