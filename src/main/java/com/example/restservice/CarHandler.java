package com.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CarHandler extends RuntimeException{

    @GetMapping("/car/{plate}")
    public ResponseEntity<Car> searchCar(@PathVariable String plate){
        Car car = MockCars.findByPlate(plate);
        if(car==null || car.getCarPlate().isEmpty()) {
            throw new CustomException("value_not_found_GET", "plate", HttpStatus.NOT_FOUND);
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
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        if (car.getCarPlate()==null || car.getCarPlate().isEmpty()){
            throw new CustomException("null_or_empty_value_POST", "plate", HttpStatus.BAD_REQUEST);
        }
        if (car.getCarModel()==null || car.getCarModel().isEmpty()){
            throw new CustomException("null_or_empty_value_POST", "model", HttpStatus.BAD_REQUEST);
        }
        if (car.getCarBrand()==null || car.getCarBrand().isEmpty()){
            throw new CustomException("null_or_empty_value_POST", "brand", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Car>(MockCars.saveCar(car), HttpStatus.OK);
    }

    @DeleteMapping("/car/{plate}")
    public ResponseEntity<Car> removeCar(@PathVariable String plate) {
        Car car = MockCars.removeCarFromList(plate);
        if (car==null || car.getCarPlate().isEmpty()){
            throw new CustomException("null_or_empty_value_POST", "plate", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Car>(car,HttpStatus.OK);
    }




}
