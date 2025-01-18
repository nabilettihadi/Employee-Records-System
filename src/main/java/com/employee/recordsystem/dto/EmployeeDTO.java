package com.employee.recordsystem.dto;

import com.employee.recordsystem.model.EmploymentStatus;
import lombok.Data;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;
    
    @NotBlank(message = "Employee ID is required")
    @Pattern(regexp = "^EMP\\d{6}$", message = "Employee ID must be in format EMP followed by 6 digits")
    private String employeeId;

    @NotBlank(message = "Full name is required")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "Full name must contain only letters, spaces, and basic punctuation")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Job title is required")
    @Size(max = 100, message = "Job title cannot exceed 100 characters")
    private String jobTitle;

    @NotNull(message = "Department ID is required")
    private Long departmentId;
    
    private String departmentName;

    @NotNull(message = "Hire date is required")
    @Past(message = "Hire date must be in the past")
    private LocalDate hireDate;

    @NotNull(message = "Employment status is required")
    private EmploymentStatus employmentStatus;

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$", message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format. Must start with + and contain 1-14 digits")
    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;
}
