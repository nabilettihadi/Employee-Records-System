package com.employee.recordsystem.ui.service;

import com.employee.recordsystem.dto.auth.LoginRequest;
import com.employee.recordsystem.dto.auth.JwtAuthenticationResponse;
import com.employee.recordsystem.ui.service.api.AuthApi;
import retrofit2.Response;
import java.io.IOException;

public class AuthService {
    private final AuthApi authApi;

    public AuthService() {
        this.authApi = RetrofitConfig.getClient().create(AuthApi.class);
    }

    public JwtAuthenticationResponse login(String username, String password) throws IOException {
        LoginRequest request = new LoginRequest(username, password);
        Response<JwtAuthenticationResponse> response = authApi.login(request).execute();
        
        if (!response.isSuccessful()) {
            throw new IOException("Error: " + response.code() + " " + response.message());
        }
        
        JwtAuthenticationResponse authResponse = response.body();
        if (authResponse != null && authResponse.getToken() != null) {
            RetrofitConfig.setAuthToken(authResponse.getToken());
        }
        
        return authResponse;
    }
}
