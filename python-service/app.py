from flask import Flask, jsonify
from faker import Faker
from faker_vehicle import VehicleProvider

app = Flask(__name__)


fake = Faker('it_IT')
fake.add_provider(VehicleProvider)

dict_cars = []


@app.route('/injectdata')
def get_car():
    for _ in range(10):
        car = {'carPlate': fake.license_plate(),
               'carBrand': fake.vehicle_make(),
               'carModel': fake.vehicle_model()}
        dict_cars.append(car)
    return jsonify(dict_cars), 201


if __name__ == '__main__':
    app.run()