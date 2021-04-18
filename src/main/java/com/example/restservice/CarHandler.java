package com.example.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class CarHandler {

    @GetMapping("/car/{plate}")
    public Car car(@PathVariable String plate) {
        return MockCars.searchCarByPlate(plate);
    }

    @GetMapping("/car")
    public ArrayList<Car> listCars() {
        return (ArrayList<Car>) MockCars.getListCar();
    }






}
