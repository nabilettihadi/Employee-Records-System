package com.employee.recordsystem.service.impl;

import com.employee.recordsystem.dto.auth.JwtAuthenticationResponse;
import com.employee.recordsystem.dto.auth.LoginRequest;
import com.employee.recordsystem.security.JwtTokenProvider;
import com.employee.recordsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
            )
        );

        String token = jwtTokenProvider.generateToken(authentication);
        String role = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst()
            .orElse("ROLE_USER");

        return JwtAuthenticationResponse.builder()
            .token(token)
            .username(loginRequest.getUsername())
            .role(role)
            .build();
    }

    @Override
    public void logout() {
        // Implement logout logic if needed
    }
}
