package com.cfm.coffeemanagement.controller;

import com.cfm.coffeemanagement.filter.JWTAuthFilter;
import com.cfm.coffeemanagement.model.db.User;
import com.cfm.coffeemanagement.model.request.UpdateEmailRequest;
import com.cfm.coffeemanagement.model.response.UpdateEmailResponse;
import com.cfm.coffeemanagement.repository.UserRepository;
import com.cfm.coffeemanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final JWTAuthFilter jwtAuthFilter;

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, JWTAuthFilter jwtAuthFilter, UserRepository userRepository) {
        this.userService = userService;
        this.jwtAuthFilter = jwtAuthFilter;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @PutMapping("/")
    public UpdateEmailResponse updateEmail(@RequestBody UpdateEmailRequest request) {
        return userService.updateEmail(request);
    }

}
