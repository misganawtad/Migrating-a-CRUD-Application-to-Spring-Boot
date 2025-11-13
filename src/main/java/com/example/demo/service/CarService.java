package com.example.demo.service;

import com.example.demo.model.Car;
import java.util.List;

public interface CarService {
    List<Car> getCars(Integer count);
}

