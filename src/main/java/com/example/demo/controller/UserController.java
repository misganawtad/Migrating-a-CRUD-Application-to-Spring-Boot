package com.example.demo.controller;


import com.example.demo.service.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final CarService carService;

    public UserController(UserService service, CarService carService) {
        this.service = service;
        this.carService = carService;
    }

    @GetMapping
    public String list(@RequestParam(value = "error", required = false) String error,
                       Model model) {
        model.addAttribute("users", service.findAll());
        if ("notfound".equals(error)) {
            model.addAttribute("errorMessage", "User not found or has been removed.");
        }
        return "users";
    }

    // Create form
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("cars", carService.getCars(null));
        return "user-form";
    }

    // Create submit
    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult br,
                         Model model) {
        if (br.hasErrors()) return "user-form";
        model.addAttribute("cars", carService.getCars(null));
        service.save(user);
        return "redirect:/users";
    }

    // Edit form (null guard → redirect with message)
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User u = service.findById(id);
        if (u == null) return "redirect:/users?error=notfound";
        model.addAttribute("user", u);
        model.addAttribute("cars", carService.getCars(null));
        return "user-form";
    }


    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("user") @Valid User user,
                         BindingResult br, Model model) {
        if (br.hasErrors()) {
            model.addAttribute("cars", carService.getCars(null));
            return "user-form";
        }
        if (service.findById(id) == null) return "redirect:/users?error=notfound";
        user.setId(id);
        service.save(user);
        return "redirect:/users";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        if (service.findById(id) == null) return "redirect:/users?error=notfound";
        service.deleteById(id);
        return "redirect:/users";
    }
}
