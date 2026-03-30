package com.example.PortfolioAlert.service;

import com.example.PortfolioAlert.dto.UserRegistrationRequest;
import com.example.PortfolioAlert.model.User;
import com.example.PortfolioAlert.repository.UserRepository;
import com.example.PortfolioAlert.util.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserRegistrationRequest request) {
        logger.info("Registering user: {}", request.getEmail());

        if (!UserValidator.isEmailValid.test(request.getEmail())) {
            throw new RuntimeException("Email id format is not correct");
        }
        if (!UserValidator.isPasswordValid.test(request.getPassword())) {
            throw new RuntimeException("Password is not following rule");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("email/ id exists in DB");
        }
        if (userRepository.existsByName(request.getName())) {
            throw new RuntimeException("User name exists in DB");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        logger.info("User {} saved successfully", request.getName());
    }
}