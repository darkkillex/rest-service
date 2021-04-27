package com.example.restservice;

import java.util.concurrent.atomic.AtomicInteger;

public class Car {

    private static AtomicInteger nextId = new AtomicInteger();
    private final int id;
    private String carPlate, carBrand, carModel;

    public Car(String carPlate, String carBrand, String carModel) {
        this.id = nextId.incrementAndGet();
        this.carPlate = carPlate;
        this.carBrand = carBrand;
        this.carModel = carModel;
    }

    public int getId() {
        return id;
    }

    public String getCarPlate() {
        return carPlate;
    }

    public void setCarPlate(String carPlate) {
        this.carPlate = carPlate;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }
}
