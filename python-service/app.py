import json
import logging
import requests
import asyncio

from concurrent.futures import ProcessPoolExecutor
from requests import ReadTimeout, ConnectionError, HTTPError, Timeout
from flask import Flask, jsonify
from faker import Faker
from faker_vehicle import VehicleProvider
from multiprocessing import Value, Lock
from time import time

app = Flask(__name__)

fake = Faker('it_IT')
fake.add_provider(VehicleProvider)

logging.basicConfig(format='%(asctime)s - %(message)s', level=logging.INFO)

dict_cars = []

counter_lock = Lock()
counter = Value('i', 0)  # defaults to 0
num_task_failed = Value('i', 0)
num_task_success = Value('i', 0)

num_jobs = 10
queue = asyncio.Queue()
executor = ProcessPoolExecutor(max_workers=num_jobs)
loop = asyncio.get_event_loop()


# @app.route('/injectdata')
def push_single_car():
    global dict_cars
    counter_thread = counter_increment(counter)
    logging.info("Start Data Injection TASK N° %d", counter_thread)
    car = {
        'carPlate': fake.license_plate(),
        'carBrand': fake.vehicle_make(),
        'carModel': fake.vehicle_model()
    }
    url = "http://localhost:9090/car"
    headers = {'Content-type': 'application/json', 'Accept': 'application/json'}
    try:
        resp = requests.post(url, data=json.dumps(car, sort_keys=False), headers=headers, timeout=10)
        logging.info("Injection N° %d. Status code: %d", counter_thread, resp.status_code)
    except (ReadTimeout, ConnectionError, HTTPError, Timeout) as e:
        logging.error("Exception occurs on data injection. Technical details given below:")
        logging.error(e)
        counter_failed_connection = counter_increment(num_task_failed)
        print("N° FAILED CONNECTION TASKS:", counter_failed_connection)
        return "Injection FAILED. Remote service unreachable", 500
    if resp.status_code != 201:
        logging.error("ERROR %d. Injection failed on object n° %d", resp.status_code, counter_thread)
        counter_failed_400 = counter_increment(num_task_failed)
        print("N° STATUS 400 TASKS:", counter_failed_400)
        return "Injection FAILED", 400
    dict_cars.append(car)
    logging.info("End of data injection N° %d", counter_thread)
    counter_success = counter_increment(num_task_success)
    print("N° SUCCESS TASKS:", counter_success)
    return 201


def counter_increment(self):
    with counter_lock:
        self.value += 1
    return self.value


async def task_producer():
    for i in range(num_jobs):
        fut = loop.run_in_executor(executor, push_single_car)
        fut.add_done_callback(lambda f: queue.put_nowait(f.result()))


async def task_handler():
    completed = 0
    while completed < num_jobs:
        await queue.get()
        completed += 1

s = time()
loop.run_until_complete(asyncio.gather(task_producer(), task_handler()))
logging.info("duration %s", time() - s)
#logging.info("N° FAILED TASKS: %d", num_task_failed)
#logging.info("N° SUCCESS TASKS: %d", num_task_success)


if __name__ == '__main__':
    app.run()
