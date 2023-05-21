package com.cfm.coffeemanagement.controller;

import com.cfm.coffeemanagement.model.request.ChangePasswordRequest;
import com.cfm.coffeemanagement.model.request.ForgotPasswordRequest;
import com.cfm.coffeemanagement.model.request.SignInRequest;
import com.cfm.coffeemanagement.model.request.SignUpRequest;
import com.cfm.coffeemanagement.model.response.ActiveResponse;
import com.cfm.coffeemanagement.model.response.SignUpResponse;
import com.cfm.coffeemanagement.model.response.SignInResponse;
import com.cfm.coffeemanagement.model.response.UserResponse;
import com.cfm.coffeemanagement.repository.UserRepository;
import com.cfm.coffeemanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        return userService.signUp(request);
    }

    @PostMapping("/signin")
    public SignInResponse signIn(@RequestBody SignInRequest request) {
        return userService.signIn(request);
    }

    @GetMapping("/active/{id}")
    public ActiveResponse active(@PathVariable Integer id) {
        return userService.activeUser(id);
    }

    @PostMapping("/change-password")
    public UserResponse changePassword(@RequestBody ChangePasswordRequest request) {
        return userService.changePassword(request);
    }

    @PostMapping("/forgot-password")
    public UserResponse forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return userService.forgotPassword(request);
    }
}
