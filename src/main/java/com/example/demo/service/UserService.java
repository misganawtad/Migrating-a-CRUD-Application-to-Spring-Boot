package com.example.demo.service;


import com.example.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Long id);
    User save(User u);
    void deleteById(Long id);
}
