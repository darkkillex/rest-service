package com.example.restservice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MockCars {

    private static List<Car> listCar = setUpDataMock();


    private static ArrayList<Car> setUpDataMock() {
        ArrayList<Car> list = new ArrayList<Car>();
        list.add(new Car("123", "Peugeot", "3008"));
        list.add(new Car("456", "Jeep", "Renegade"));
        list.add(new Car("789", "Renault", "Capture"));
        list.add(new Car("AAA", "Jeep", "Compass"));
        list.add(new Car("BBB", "Renault", "Clio"));
        return list;
    }

    public static Car searchCarByPlate(String plate) {
        for (Car car : listCar) {
            if (car.getCarPlate().equals(plate)) {
                return car;
            }
        }
        return null;
    }

    public static List<Car> getListCar(String plate, String model, String brand) {
        List<Predicate<Car>> allPredicates = new ArrayList<Predicate<Car>>();
        allPredicates.add(car->car.getCarPlate().equals(plate));
        allPredicates.add(car->car.getCarModel().equals(model));
        allPredicates.add(car->car.getCarBrand().equals(brand));
        if (plate == null && model == null && brand == null) {
            return listCar;
        } else {
            List<Car> filteredListCar = listCar.stream()
                    .filter(allPredicates.stream().reduce(car->false, Predicate::or))
                    .collect(Collectors.toList());
            return filteredListCar;
        }
    }


}
