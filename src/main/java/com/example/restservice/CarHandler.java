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
        if (car == null || car.getCarPlate().isEmpty()) {
            throw new CustomException("value_not_found_GET", "id", HttpStatus.NOT_FOUND);
        }
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
        if (car.getCarPlate() == null || car.getCarPlate().isEmpty()) {
            throw new CustomException("null_or_empty_value_POST", "plate", HttpStatus.BAD_REQUEST);
        }
        if (car.getCarModel() == null || car.getCarModel().isEmpty()) {
            throw new CustomException("null_or_empty_value_POST", "model", HttpStatus.BAD_REQUEST);
        }
        if (car.getCarBrand() == null || car.getCarBrand().isEmpty()) {
            throw new CustomException("null_or_empty_value_POST", "brand", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Car>(MockCars.saveCar(car), HttpStatus.CREATED);
    }

    @PutMapping("/car/{id}")
    public ResponseEntity<Car> updateCar(@RequestBody Car carDetails, @PathVariable int id) {
        Car car = MockCars.findById(id);
        if (car== null) {
            throw new CustomException("record_not_found_PUT", "id", HttpStatus.NOT_FOUND);
        }
        if (carDetails.getCarPlate() == null || carDetails.getCarPlate().isEmpty()) {
            throw new CustomException("null_or_empty_value_POST", "plate", HttpStatus.BAD_REQUEST);
        }
        if (carDetails.getCarModel() == null || carDetails.getCarModel().isEmpty()) {
            throw new CustomException("null_or_empty_value_POST", "model", HttpStatus.BAD_REQUEST);
        }
        if (carDetails.getCarBrand() == null || carDetails.getCarBrand().isEmpty()) {
            throw new CustomException("null_or_empty_value_POST", "brand", HttpStatus.BAD_REQUEST);
        }
        car.setCarPlate(carDetails.getCarPlate());
        car.setCarModel(carDetails.getCarModel());
        car.setCarBrand(carDetails.getCarBrand());
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Car> removeCar(@PathVariable int id) {
        Car car = MockCars.removeCarFromList(id);
        if (car == null || car.getCarPlate().isEmpty()) {
            throw new CustomException("null_or_empty_value_POST", "id", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }


}
