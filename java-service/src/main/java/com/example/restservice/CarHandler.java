package com.example.restservice;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Timed
@RestController
public class CarHandler extends RuntimeException {

    @Autowired
    private MeterRegistry meterRegistry;

    @GetMapping("/car/{id}")
    public ResponseEntity<Car> searchCar(@PathVariable int id) {
        long start_time = System.currentTimeMillis();
        Car car = MockCars.findById(id);
        UtilityHandler.checkObjectIsNotNull(car);
        meterRegistry.counter("custom_java_service_single_car_requests_GET_COUNTER").increment();
        meterRegistry.timer("custom_java_service_single_car_requests_GET_TIMER").
                record(System.currentTimeMillis() - start_time, TimeUnit.MILLISECONDS);
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    @GetMapping("/car")
    public List<Car> listCar(@RequestParam(required = false) String plate,
                             @RequestParam(required = false) String model,
                             @RequestParam(required = false) String brand) {
        long start_time = System.currentTimeMillis();
        meterRegistry.counter("custom_java_service_list_car_requests_GET_COUNTER").increment();
        meterRegistry.timer("custom_java_service_list_car_requests_GET_TIMER").
                record(System.currentTimeMillis() - start_time, TimeUnit.MILLISECONDS);
        return MockCars.getFilteredListCar(plate, model, brand);
    }

    @PostMapping(path = "/car", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        long start_time = System.currentTimeMillis();
        UtilityHandler.checkValueParameter(car.getCarPlate(), "carPlate");
        UtilityHandler.checkValueParameter(car.getCarModel(), "carModel");
        UtilityHandler.checkValueParameter(car.getCarBrand(), "carBrand");
        MockCars.checkUniquePlate(car.getCarPlate());
        meterRegistry.counter("custom_java_service_requests_POST_COUNTER").increment();
        meterRegistry.timer("custom_java_service_requests_POST_TIMER").
                record(System.currentTimeMillis() - start_time, TimeUnit.MILLISECONDS);
        return new ResponseEntity<Car>(MockCars.saveCar(car), HttpStatus.CREATED);
    }

    @PutMapping("/car/{id}")
    public ResponseEntity<Car> updateCar(@RequestBody Car carDetails, @PathVariable int id) {
        long start_time = System.currentTimeMillis();
        Car car = MockCars.findById(id);
        UtilityHandler.checkObjectIsNotNull(car);
        UtilityHandler.checkValueParameter(carDetails.getCarPlate(), "carPlate");
        UtilityHandler.checkValueParameter(carDetails.getCarModel(), "carModel");
        UtilityHandler.checkValueParameter(carDetails.getCarBrand(), "carBrand");
        MockCars.checkUniquePlate(carDetails.getCarPlate());
        car.setCarPlate(carDetails.getCarPlate());
        car.setCarModel(carDetails.getCarModel());
        car.setCarBrand(carDetails.getCarBrand());
        meterRegistry.counter("custom_java_service_requests_PUT_COUNTER").increment();
        meterRegistry.timer("custom_java_service_requests_PUT_TIMER").
                record(System.currentTimeMillis() - start_time, TimeUnit.MILLISECONDS);
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Car> removeCar(@PathVariable int id) {
        long start_time = System.currentTimeMillis();
        Car car = MockCars.removeCarFromList(id);
        UtilityHandler.checkObjectIsNotNull(car);
        meterRegistry.counter("custom_java_service_requests_DELETE_COUNTER").increment();
        meterRegistry.timer("custom_java_service_requests_DELETE_TIMER").
                record(System.currentTimeMillis() - start_time, TimeUnit.MILLISECONDS);
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }



}
