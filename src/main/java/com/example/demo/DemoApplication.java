package com.example.demo;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }


    @Bean
    CommandLineRunner initData(UserRepository userRepository,
                               RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                var adminRole = roleRepository
                        .findByName("ROLE_ADMIN")
                        .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

                var userRole = roleRepository
                        .findByName("ROLE_USER")
                        .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

                // ADMIN user (ADMIN + USER roles)
                User admin = new User();
                admin.setFirstName("Admin");
                admin.setLastName("Boss");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("password1234"));
                admin.getRoles().add(adminRole);
                admin.getRoles().add(userRole);
                userRepository.save(admin);

                // Normal USER (only USER role)
                User u = new User();
                u.setFirstName("Normal");
                u.setLastName("User");
                u.setEmail("user@example.com");
                u.setPassword(passwordEncoder.encode("password1234"));
                u.getRoles().add(userRole);
                userRepository.save(u);
            }
        };
    }
}
