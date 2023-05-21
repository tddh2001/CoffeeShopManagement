package com.cfm.coffeemanagement.model.response;

import lombok.Data;

@Data
public class SignInResponse {
    private String message;
    private String token;
}
