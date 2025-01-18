package com.employee.recordsystem.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.Set;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private Set<String> roles;
    private String employeeId;
}
