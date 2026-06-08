package com.app.ad_management.service;

import com.app.ad_management.dto.AuthResponse;
import com.app.ad_management.dto.LoginRequest;
import com.app.ad_management.dto.RegisterRequest;
import com.app.ad_management.model.User;
import com.app.ad_management.repository.UserRepository;
import com.app.ad_management.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("User already exist");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.valueOf((request.getRole().toUpperCase())));
        user.setStatus(User.Status.PENDING);

        userRepository.save(user);
        return "User registration successful";
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponse(token,
                user.getRole().name(),
                user.getName(),
                user.getStatus().name()
        );
    }
}
