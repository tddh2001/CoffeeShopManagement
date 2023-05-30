package com.cfm.coffeemanagement.controller;

import com.cfm.coffeemanagement.filter.JWTAuthFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    private final JWTAuthFilter jwtAuthFilter;

    public WelcomeController(JWTAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @GetMapping("/welcome")
    public String welcomeHome() {
        return jwtAuthFilter.getCurrentUser();
    }
}