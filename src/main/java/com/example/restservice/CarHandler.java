package com.example.restservice;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarHandler {

    @GetMapping("/car/{plate}")
    public Car car(@PathVariable String plate) {
        return MockCars.findByPlate(plate);
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
