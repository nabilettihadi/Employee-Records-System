package com.employee.recordsystem.service;

import com.employee.recordsystem.dto.auth.JwtAuthenticationResponse;
import com.employee.recordsystem.dto.auth.LoginRequest;

public interface AuthService {
    JwtAuthenticationResponse login(LoginRequest loginRequest);
    void logout();
}
