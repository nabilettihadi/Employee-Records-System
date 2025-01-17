package com.employee.recordsystem.ui.service.api;

import com.employee.recordsystem.dto.auth.LoginRequest;
import com.employee.recordsystem.dto.auth.JwtAuthenticationResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("auth/login")
    Call<JwtAuthenticationResponse> login(@Body LoginRequest loginRequest);
}
