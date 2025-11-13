package com.example.demo.service;

import com.example.demo.model.Car;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final List<Car> cars = Arrays.asList(
            new Car("Carina", "1", 2000),
            new Car("Corolla", "2", 2010),
            new Car("Civic", "3", 2000),
            new Car("Elantra", "4", 2010),
            new Car("Sonata", "4", 2020)
    );

    @Override
    public List<Car> getCars(Integer count) {
        if (count == null || count >= cars.size()) return cars;
        if (count <= 0) return Collections.emptyList();
        return cars.subList(0, count);
    }
}