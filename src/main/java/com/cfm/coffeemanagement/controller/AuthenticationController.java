package com.cfm.coffeemanagement.controller;

import com.cfm.coffeemanagement.model.request.ChangePasswordRequest;
import com.cfm.coffeemanagement.model.request.ForgotPasswordRequest;
import com.cfm.coffeemanagement.model.request.SignInRequest;
import com.cfm.coffeemanagement.model.request.SignUpRequest;
import com.cfm.coffeemanagement.model.response.ActiveResponse;
import com.cfm.coffeemanagement.model.response.SignInResponse;
import com.cfm.coffeemanagement.model.response.SignUpResponse;
import com.cfm.coffeemanagement.model.response.UserResponse;
import com.cfm.coffeemanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
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
