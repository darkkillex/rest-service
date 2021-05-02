package com.example.restservice;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CarHandler extends RuntimeException {

    private final MeterRegistry registry;

    public CarHandler(MeterRegistry registry) {
        this.registry = registry;
    }


    @GetMapping("/car/{id}")
    public ResponseEntity<Car> searchCar(@PathVariable int id) {
        addMetricToRequest("GET_SINGLE_CAR");
        Car car = MockCars.findById(id);
        MockCars.checkObjectIsNotNull(car);
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    @GetMapping("/car")
    public List<Car> listCar(@RequestParam(required = false) String plate,
                             @RequestParam(required = false) String model,
                             @RequestParam(required = false) String brand) {
        addMetricToRequest("GET_ALL_CARS");
        return MockCars.getFilteredListCar(plate, model, brand);
    }

    @PostMapping(path = "/car", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        addMetricToRequest("POST");
        MockCars.checkValueParameter(car.getCarPlate(), "carPlate");
        MockCars.checkValueParameter(car.getCarModel(), "carModel");
        MockCars.checkValueParameter(car.getCarBrand(), "carBrand");
        MockCars.checkUniquePlate(car.getCarPlate());
        return new ResponseEntity<Car>(MockCars.saveCar(car), HttpStatus.CREATED);
    }

    @PutMapping("/car/{id}")
    public ResponseEntity<Car> updateCar(@RequestBody Car carDetails, @PathVariable int id) {
        addMetricToRequest("PUT");
        Car car = MockCars.findById(id);
        MockCars.checkObjectIsNotNull(car);
        MockCars.checkValueParameter(carDetails.getCarPlate(), "carPlate");
        MockCars.checkValueParameter(carDetails.getCarModel(), "carModel");
        MockCars.checkValueParameter(carDetails.getCarBrand(), "carBrand");
        MockCars.checkUniquePlate(carDetails.getCarPlate());
        car.setCarPlate(carDetails.getCarPlate());
        car.setCarModel(carDetails.getCarModel());
        car.setCarBrand(carDetails.getCarBrand());
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Car> removeCar(@PathVariable int id) {
        addMetricToRequest("DELETE");
        Car car = MockCars.removeCarFromList(id);
        MockCars.checkObjectIsNotNull(car);
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    public void addMetricToRequest(String tag) {
        Tag methodTag = Tag.of("method", tag);
        List<Tag> tags = new ArrayList<>();
        tags.add(methodTag);
        registry.counter("number_of_requests",tags).increment();
    }

}
