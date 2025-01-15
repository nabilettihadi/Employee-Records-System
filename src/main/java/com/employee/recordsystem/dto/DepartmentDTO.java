package com.employee.recordsystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class DepartmentDTO {
    private Long id;
    private String name;
    private String description;
    private Long managerId;
    private String managerName;
    private List<EmployeeDTO> employees;
}
