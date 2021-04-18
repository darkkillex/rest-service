package com.example.restservice;

import java.util.ArrayList;
import java.util.List;

public class MockCars {

    private static List<Car> listCar = setUpDataMock();


    private static ArrayList<Car> setUpDataMock(){
        ArrayList<Car> list = new ArrayList<Car>();
        list.add(new Car("123", "Peugeot", "3008"));
        list.add(new Car("456", "Jeep", "Renegade"));
        list.add(new Car("789", "Renault", "Capture"));
        return list;
    }

    public static Car searchCarByPlate(String plate){
        for(Car car : listCar) {
            if(car.getCarPlate().equals(plate)) {
                return car;
            }
        }
        return null;
    }

    public static List<Car> getListCar() {
        return listCar;
    }
}
