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
        MockCars.isFoundObject(car, HttpStatus.NOT_FOUND);
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
        MockCars.isValidParameter(car.getCarPlate(), "plate", HttpStatus.BAD_REQUEST);
        MockCars.isValidParameter(car.getCarModel(), "model",HttpStatus.BAD_REQUEST);
        MockCars.isValidParameter(car.getCarBrand(), "brand",HttpStatus.BAD_REQUEST);
        return new ResponseEntity<Car>(MockCars.saveCar(car), HttpStatus.CREATED);
    }

    @PutMapping("/car/{id}")
    public ResponseEntity<Car> updateCar(@RequestBody Car carDetails, @PathVariable int id) {
        Car car = MockCars.findById(id);
        MockCars.isFoundObject(car, HttpStatus.NOT_FOUND);
        MockCars.isValidParameter(carDetails.getCarPlate(), "plate",HttpStatus.BAD_REQUEST);
        MockCars.isValidParameter(carDetails.getCarModel(), "model",HttpStatus.BAD_REQUEST);
        MockCars.isValidParameter(carDetails.getCarBrand(), "brand",HttpStatus.BAD_REQUEST);
        car.setCarPlate(carDetails.getCarPlate());
        car.setCarModel(carDetails.getCarModel());
        car.setCarBrand(carDetails.getCarBrand());
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<Car> removeCar(@PathVariable int id) {
        Car car = MockCars.removeCarFromList(id);
        MockCars.isFoundObject(car, HttpStatus.NOT_FOUND);
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }


}
