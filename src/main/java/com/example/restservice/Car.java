package com.example.restservice;

public class Car {

    private final long id;
    private String carPlate;
    private String carBrand;
    private String carModel;

    public Car(long id, String carPlate, String carBrand, String carModel) {
        this.id = id;
        this.carPlate = carPlate;
        this.carBrand = carBrand;
        this.carModel = carModel;
    }

    public long getId() {
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
