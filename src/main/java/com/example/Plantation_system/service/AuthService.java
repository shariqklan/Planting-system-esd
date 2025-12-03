package com.example.Plantation_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Plantation_system.dto.LoginRequest;
import com.example.Plantation_system.dto.LoginResponse;
import com.example.Plantation_system.dto.RegisterRequest;
import com.example.Plantation_system.entities.Role;
import com.example.Plantation_system.entities.User;
import com.example.Plantation_system.repositories.UserRepository;
import com.example.Plantation_system.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public void register(RegisterRequest request) throws Exception {
        // Check if user already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new Exception("Username already exists");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new Exception("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        
        // Set role
        try {
            user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        } catch (IllegalArgumentException e) {
            user.setRole(Role.VOLUNTEER);
        }

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) throws AuthenticationException {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Get user from database
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationException("User not found") {});

        // Generate JWT token
        String token = jwtUtil.generateTokenWithRole(user.getUsername(), user.getRole().name());

        // Return login response
        return new LoginResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getUserId()
        );
    }
}