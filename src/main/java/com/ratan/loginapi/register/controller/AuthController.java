package com.ratan.loginapi.register.controller;


import com.ratan.loginapi.exception.InvalidPasswordException;
import com.ratan.loginapi.exception.UsernameNotFoundException;
import com.ratan.loginapi.jwt.JwtUtil;
import com.ratan.loginapi.register.dto.LoginRequest;
import com.ratan.loginapi.register.dto.RegisterRequest;
import com.ratan.loginapi.register.dto.Role;
import com.ratan.loginapi.register.entity.User;
import com.ratan.loginapi.register.repo.UserRepository;
import com.ratan.loginapi.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        Optional<User> userOptional = userRepository.findByUsername(request.getUsername());

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid password");
        }

        // Bump token version to invalidate old tokens
        user.setTokenVersion(user.getTokenVersion() + 1);
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(),user.getTokenVersion());
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("token", "Bearer " + token);
        return ResponseEntity.ok(
                ApiResponse.<Map<String, String>>builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.OK.value())
                        .message("Login successful")
                        .path(httpRequest.getRequestURI())
                        .data(tokenResponse)
                        .build()
        );
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new UsernameNotFoundException("Username already exists");
        }

        Role finalRole = Role.USER;
        if (request.getRole() != null) {
            finalRole = Role.fromValue(request.getRole());
        }

        User newUser = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword())) // 👈 hash here
                .role(finalRole.name())
                .tokenVersion(1L)
                .build();

        userRepository.save(newUser);
        Map<String, String> userResponse = new HashMap<>();
        userResponse.put("username", newUser.getUsername());
        userResponse.put("role", newUser.getRole());
        userResponse.put("password", newUser.getPassword());
        return ResponseEntity.ok(
                ApiResponse.<Map<String, String>>builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.OK.value())
                        .message("User registered successfully")
                        .path(httpRequest.getRequestURI())
                        .data(userResponse)
                        .build()
        );
    }

}