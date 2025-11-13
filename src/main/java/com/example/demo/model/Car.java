package com.example.demo.model;

public class Car {
    private String brand;
    private String model;
    private int year;

    public Car() { }

    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
}
