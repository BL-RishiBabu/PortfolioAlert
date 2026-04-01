package com.example.PortfolioAlert.controller;

import com.example.PortfolioAlert.dto.ApiResponse;
import com.example.PortfolioAlert.dto.AuthResponse;
import com.example.PortfolioAlert.dto.LoginRequest;
import com.example.PortfolioAlert.dto.UserRegistrationRequest;
import com.example.PortfolioAlert.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRegistrationRequest request) {
        try {
            userService.registerUser(request);
            return ResponseEntity.ok(new ApiResponse("Registration Successful", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = userService.loginUser(request);
            return ResponseEntity.ok(new AuthResponse(token, "Login Successful"));
        } catch (Exception e) {
            // 401 Unauthorized for failed login
            return ResponseEntity.status(401).body(new ApiResponse(e.getMessage(), false));
        }
    }
}