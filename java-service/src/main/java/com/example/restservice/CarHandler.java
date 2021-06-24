package com.example.restservice;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Timed
@RestController
public class CarHandler extends RuntimeException {

    @Autowired
    private MeterRegistry meterRegistry;

    @GetMapping("/car/{id}")
    public ResponseEntity<Car> searchCar(@PathVariable int id) {
        Car car = MockCars.findById(id);
        UtilityHandler.checkObjectIsNotNull(car);
        meterRegistry.counter("single_car_requests_GET").increment();
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    @GetMapping("/car")
    public List<Car> listCar(@RequestParam(required = false) String plate,
                             @RequestParam(required = false) String model,
                             @RequestParam(required = false) String brand) {
        meterRegistry.counter("list_car_requests_GET").increment();
        return MockCars.getFilteredListCar(plate, model, brand);
    }

    @PostMapping(path = "/car", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        UtilityHandler.checkValueParameter(car.getCarPlate(), "carPlate");
        UtilityHandler.checkValueParameter(car.getCarModel(), "carModel");
        UtilityHandler.checkValueParameter(car.getCarBrand(), "carBrand");
        MockCars.checkUniquePlate(car.getCarPlate());
        meterRegistry.counter("requests_POST").increment();
        return new ResponseEntity<Car>(MockCars.saveCar(car), HttpStatus.CREATED);
    }

    @PutMapping("/car/{id}")
    public ResponseEntity<Car> updateCar(@RequestBody Car carDetails, @PathVariable int id) {
        Car car = MockCars.findById(id);
        UtilityHandler.checkObjectIsNotNull(car);
        UtilityHandler.checkValueParameter(carDetails.getCarPlate(), "carPlate");
        UtilityHandler.checkValueParameter(carDetails.getCarModel(), "carModel");
        UtilityHandler.checkValueParameter(carDetails.getCarBrand(), "carBrand");
        MockCars.checkUniquePlate(carDetails.getCarPlate());
        car.setCarPlate(carDetails.getCarPlate());
        car.setCarModel(carDetails.getCarModel());
        car.setCarBrand(carDetails.getCarBrand());
        meterRegistry.counter("requests_PUT").increment();
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Car> removeCar(@PathVariable int id) {
        Car car = MockCars.removeCarFromList(id);
        UtilityHandler.checkObjectIsNotNull(car);
        meterRegistry.counter("requests_DELETE").increment();
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }



}
