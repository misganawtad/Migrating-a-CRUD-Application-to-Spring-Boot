package com.example.demo.controller;

import com.example.demo.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HelloController {

    private final CarService carService;

    public HelloController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String printWelcome(Model model) {
        List<String> messages = new ArrayList<>();
        messages.add("Welcome!");

        model.addAttribute("messages", messages);
        return "index";  // -> templates/index.html
    }

    @GetMapping("/cars")
    public String showCars(@RequestParam(value = "count", required = false) Integer count,
                           Model model) {
        model.addAttribute("cars", carService.getCars(count));
        return "cars";   // -> templates/cars.html
    }
}
