package com.example.PortfolioAlert.service;

import com.example.PortfolioAlert.dto.LoginRequest;
import com.example.PortfolioAlert.dto.UserRegistrationRequest;
import com.example.PortfolioAlert.model.User;
import com.example.PortfolioAlert.repository.UserRepository;
import com.example.PortfolioAlert.util.JwtUtils;
import com.example.PortfolioAlert.util.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // <-- This is CRITICAL. Without this, Spring won't find the "Bean"
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public void registerUser(UserRegistrationRequest request) {
        if (!UserValidator.isEmailValid.test(request.getEmail())) {
            throw new RuntimeException("Email id format is not correct");
        }
        if (!UserValidator.isPasswordValid.test(request.getPassword())) {
            throw new RuntimeException("Password is not following rule");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("email/ id exists in DB");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public String loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtUtils.generateToken(user.getEmail());
    }
}