package com.cfm.coffeemanagement.utils;

import com.google.common.base.Preconditions;

import static com.cfm.coffeemanagement.constants.Constants.INCORRECT_EMAIL_FORMAT;

public class EmailValidation {
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public static void validate(String email) {
        Preconditions.checkArgument(email.matches(EMAIL_REGEX), INCORRECT_EMAIL_FORMAT);
    }
}
