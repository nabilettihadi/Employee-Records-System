package com.employee.recordsystem.model;

import lombok.Data;
import java.time.LocalDate;

@Data
public class EmployeeDTO {
    private Long id;
    private String employeeId;
    private String fullName;
    private String jobTitle;
    private Long departmentId;
    private String departmentName;
    private LocalDate hireDate;
    private EmploymentStatus employmentStatus;
    private String email;
    private String phone;
    private String address;
}
