package com.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarHandler {

    @GetMapping("/car/{plate}")
    public ResponseEntity<Car> searchCar(@PathVariable String plate) {
        Car car = MockCars.findByPlate(plate);
        if(car==null || car.getCarPlate().isEmpty()) {
            throw new CustomException();
        }
        return new ResponseEntity<Car>(car,HttpStatus.OK);
    }

    @GetMapping("/car")
    public List<Car> listCar(@RequestParam(required = false) String plate,
                             @RequestParam(required = false) String model,
                             @RequestParam(required = false) String brand){
        return MockCars.getFilteredListCar(plate, model, brand);
    }

    @PostMapping(path = "/car", consumes = "application/json", produces = "application/json")
    public Car createCar(@RequestBody Car car) {
        return MockCars.saveCar(car);
    }

    @DeleteMapping("/car/{plate}")
    public List<Car> removeCar(@PathVariable String plate) {
        return MockCars.removeCarFromList(plate);
    }




}
