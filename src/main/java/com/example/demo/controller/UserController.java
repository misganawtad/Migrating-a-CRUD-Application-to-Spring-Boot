package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserService service;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService service, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(@RequestParam(value = "error", required = false) String error,
                       Model model) {
        model.addAttribute("users", service.findAll());
        if ("notfound".equals(error)) {
            model.addAttribute("errorMessage", "User not found or has been removed.");
        }
        return "users"; // templates/users.html
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user,
                         BindingResult br,
                         @RequestParam("roleName") String roleName,
                         Model model) {

        if (br.hasErrors()) {
            return "user-form";
        }

        // encode password from form
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));

        // set single role
        user.getRoles().clear();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName));
        user.getRoles().add(role);

        service.save(user);
        return "redirect:/admin/users";
    }


    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        User existing = service.findById(id);
        if (existing == null) {
            return "redirect:/admin/users?error=notfound";
        }
        model.addAttribute("user", existing);
        return "user-form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("user") @Valid User user,
                         BindingResult br,
                         @RequestParam("roleName") String roleName,
                         Model model) {

        if (br.hasErrors()) {
            return "user-form";
        }

        User existing = service.findById(id);
        if (existing == null) {
            return "redirect:/admin/users?error=notfound";
        }

        // copy fields
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setAge(user.getAge());
        existing.setEmail(user.getEmail());

        // change password only if something entered
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        // update role
        existing.getRoles().clear();
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role not found: " + roleName));
        existing.getRoles().add(role);

        service.save(existing);
        return "redirect:/admin/users";
    }



    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        if (service.findById(id) == null) return "redirect:/admin/users?error=notfound";
        service.deleteById(id);
        return "redirect:/admin/users";
    }


}
