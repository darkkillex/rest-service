package com.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarHandler extends RuntimeException {

    @GetMapping("/car/{id}")
    public ResponseEntity<Car> searchCar(@PathVariable int id) {
        Car car = MockCars.findById(id);
        MockCars.checkObjectIsNotNull(car);
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    @GetMapping("/car")
    public List<Car> listCar(@RequestParam(required = false) String plate,
                             @RequestParam(required = false) String model,
                             @RequestParam(required = false) String brand) {
        return MockCars.getFilteredListCar(plate, model, brand);
    }

    @PostMapping(path = "/car", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        MockCars.checkValueParameter(car.getCarPlate(), "carPlate");
        MockCars.checkValueParameter(car.getCarModel(), "carModel");
        MockCars.checkValueParameter(car.getCarBrand(), "carBrand");
        MockCars.checkUniquePlate(car.getCarPlate());
        return new ResponseEntity<Car>(MockCars.saveCar(car), HttpStatus.CREATED);
    }

    @PutMapping("/car/{id}")
    public ResponseEntity<Car> updateCar(@RequestBody Car carDetails, @PathVariable int id) {
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
        Car car = MockCars.removeCarFromList(id);
        MockCars.checkObjectIsNotNull(car);
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }


}
