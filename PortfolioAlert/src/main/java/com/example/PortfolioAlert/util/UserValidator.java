package com.example.PortfolioAlert.util;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class UserValidator {
    // Requirements: Min 8 chars, mixed case, one number, one special char from [@, #, $, %, ^, *, -, _]
    private static final String PWD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^*\\-_]).{8,}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static final Predicate<String> isPasswordValid =
            pwd -> pwd != null && Pattern.compile(PWD_REGEX).matcher(pwd).matches();

    public static final Predicate<String> isEmailValid =
            email -> email != null && Pattern.compile(EMAIL_REGEX).matcher(email).matches();
}