package com.example.demo.controller;

import com.example.demo.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserHomeController {

    @GetMapping("/user")
    public String userHome(@AuthenticationPrincipal User loggedInUser,
                           Model model) {
        model.addAttribute("user", loggedInUser);
        return "user-home"; // templates/user-home.html
    }
}
