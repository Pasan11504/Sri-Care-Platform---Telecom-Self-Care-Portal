package com.sricare.userservice.service;

import com.sricare.userservice.dto.*;
import com.sricare.userservice.entity.User;
import com.sricare.userservice.repository.UserRepository;
import com.sricare.userservice.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public UserResponse register(RegisterRequest request) {
        // Check if phone number already exists
        if (userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Phone number " + request.getPhoneNumber() + " is already registered. Please use a different phone number.");
        }
        
        // Check if email already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email " + request.getEmail() + " is already registered. Please use a different email.");
        }

        User user = new User();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setAccountStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User saved = userRepository.save(user);
        return mapToResponse(saved);
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Debug logging with SLF4J
        logger.info("Login attempt for phone: {}", request.getPhoneNumber());
        logger.debug("Stored password: {}", user.getPassword());
        logger.debug("Provided password: {}", request.getPassword());
        
        // For development: support both plain passwords and BCrypt hashes
        boolean passwordMatches = false;
        
        // Check if stored password is plain text (for testing)
        if (user.getPassword().equals(request.getPassword())) {
            passwordMatches = true;
            logger.info("Plain text password match for: {}", request.getPhoneNumber());
        } else if (user.getPassword().startsWith("$2")) {
            // It's a BCrypt hash
            passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
            logger.info("BCrypt password match result: {} for: {}", passwordMatches, request.getPhoneNumber());
        }
        
        logger.info("Final password match result: {} for: {}", passwordMatches, request.getPhoneNumber());

        if (!passwordMatches) {
            logger.warn("Login failed - invalid password for: {}", request.getPhoneNumber());
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getId(), user.getPhoneNumber());
        logger.info("Login successful for: {} with token", request.getPhoneNumber());
        return new LoginResponse(token, user.getId(), user.getPhoneNumber(), user.getEmail());
    }

    public UserResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToResponse(user);
    }

    public UserResponse changePassword(ChangePasswordRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        User updated = userRepository.save(user);
        return mapToResponse(updated);
    }

    public UserResponse resetPassword(String phoneNumber, String newPassword) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        User updated = userRepository.save(user);
        return mapToResponse(updated);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(user.getId(), user.getPhoneNumber(), user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getAccountStatus());
    }
}
