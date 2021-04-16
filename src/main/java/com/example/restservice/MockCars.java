package com.example.restservice;

import java.util.ArrayList;
import java.util.List;

public class MockCars {

    private static List<Car> listCar = new ArrayList<Car>();


    public static void setUpDataMock(){
        listCar.add(new Car("123", "Peugeot", "3008"));
        listCar.add(new Car("456", "Jeep", "Renegade"));
        listCar.add(new Car("789", "Renault", "Capture"));
    }

    public static Car searchCarByPlate(String plate){
        setUpDataMock();
        for(Car car : listCar) {
            if(car.getCarPlate().equals(plate)) {
                return car;
            }
        }
        return null;
    }

}
