package com.employee.recordsystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class DepartmentDTO {
    private Long id;

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    private String name;

    private String description;
    private String location;
    private LocalDate createdDate;
    private Long managerId;
    private String managerName;
    private EmployeeDTO manager;
    private int employeeCount;
    private List<EmployeeDTO> employees;
}
