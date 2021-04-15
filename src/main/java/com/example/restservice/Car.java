package com.example.restservice;

public class Car {

    private String carPlate;
    private String carBrand;
    private String carModel;

    public Car(String carPlate, String carBrand, String carModel) {
        this.carPlate = carPlate;
        this.carBrand = carBrand;
        this.carModel = carModel;
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
