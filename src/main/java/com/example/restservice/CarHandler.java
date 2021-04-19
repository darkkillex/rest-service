package com.example.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CarHandler {

    @GetMapping("/car/{plate}")
    public Car car(@PathVariable String plate) {
        return MockCars.searchCarByPlate(plate);
    }


    @GetMapping("/car")
    public List<Car> listCar(@RequestParam(required = false) String plate,
                             @RequestParam(required = false) String model,
                             @RequestParam(required = false) String brand){
        return MockCars.getListCar(plate, model, brand);
    }







}
