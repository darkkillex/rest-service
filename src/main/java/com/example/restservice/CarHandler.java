package com.example.restservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CarHandler {

    @GetMapping("/car")
    public Car car(@RequestParam Map<String, String> requestParams) throws Exception {
        String plate = requestParams.get("plate");
        String brand = requestParams.get("brand");
        String model = requestParams.get("model");
        return new Car(plate, brand, model);
    }



}
