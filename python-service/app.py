import json

import requests
from flask import Flask, jsonify
from faker import Faker
from faker_vehicle import VehicleProvider

app = Flask(__name__)

fake = Faker('it_IT')
fake.add_provider(VehicleProvider)

dict_cars = []


@app.route('/injectdata')
def push_single_car():
    for _ in range(10):
        car = {
            'carPlate': fake.license_plate(),
            'carBrand': fake.vehicle_make(),
            'carModel': fake.vehicle_model()
        }
        dict_cars.append(car)
        url = "http://localhost:9090/car"
        headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
        resp = requests.post(url, data=json.dumps(car, sort_keys=False), headers=headers)
        print(resp.json())
        print(resp.status_code)
    return jsonify(dict_cars)


if __name__ == '__main__':
    app.run()
