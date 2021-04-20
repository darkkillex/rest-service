package com.example.restservice;

import java.util.ArrayList;
import java.util.List;
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
        list.add(new Car("12D", "Renault", "Clio"));
        return list;
    }


    public static Car findByPlate(String plate) {
        for (Car car : listCar) {
            if (car.getCarPlate().equals(plate)) {
                return car;
            }
        }
        return null;
    }

    public static List<Car> getFilteredListCar(String plate, String model, String brand) {
        if (plate == null && model == null && brand == null) {
            return listCar;
        } else {
            List<Predicate<Car>> allPredicates = new ArrayList<Predicate<Car>>();
            if (plate!=null){
                allPredicates.add(car->car.getCarPlate().equals(plate));
            } if (model!=null) {
                allPredicates.add(car -> car.getCarModel().equals(model));
            } if(brand!=null) {
                allPredicates.add(car -> car.getCarBrand().equals(brand));
            }
            List<Car> filteredListCar = listCar.stream()
                    .filter(allPredicates.stream().reduce(car->true, Predicate::and))
                    .collect(Collectors.toList());
            return filteredListCar;
        }
    }

    public static Car saveCar(Car car){
        listCar.add(car);
        return car;
    }

    public static List<Car> removeCarFromList(String plate) {
        Predicate<Car> carSelected = c -> c.getCarPlate().equals(plate);
        listCar.removeIf(carSelected);
        return listCar;
    }
}
