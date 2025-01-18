package com.employee.recordsystem.integration;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.Department;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.repository.DepartmentRepository;
import com.employee.recordsystem.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department testDepartment;
    private EmployeeDTO testEmployeeDTO;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();

        // Create test department
        testDepartment = new Department();
        testDepartment.setName("IT");
        testDepartment = departmentRepository.save(testDepartment);

        // Create test employee DTO
        testEmployeeDTO = new EmployeeDTO();
        testEmployeeDTO.setEmployeeId("EMP123456");
        testEmployeeDTO.setFullName("John Doe");
        testEmployeeDTO.setJobTitle("Software Engineer");
        testEmployeeDTO.setDepartmentId(testDepartment.getId());
        testEmployeeDTO.setHireDate(LocalDate.now().minusDays(1));
        testEmployeeDTO.setEmploymentStatus(EmploymentStatus.ACTIVE);
        testEmployeeDTO.setEmail("john.doe@company.com");
        testEmployeeDTO.setPhone("+1234567890");
        testEmployeeDTO.setAddress("123 Main St");
    }

    @Test
    @WithMockUser(roles = "HR")
    void createEmployee_ValidData_Returns201() throws Exception {
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEmployeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.employeeId").value(testEmployeeDTO.getEmployeeId()));
    }

    @Test
    @WithMockUser(roles = "HR")
    void getEmployee_ExistingEmployee_Returns200() throws Exception {
        // First create an employee
        String response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEmployeeDTO)))
                .andReturn().getResponse().getContentAsString();
        
        EmployeeDTO createdEmployee = objectMapper.readValue(response, EmployeeDTO.class);

        // Then retrieve it
        mockMvc.perform(get("/api/employees/" + createdEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(testEmployeeDTO.getEmployeeId()));
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void updateEmployee_ManagerRole_Returns200() throws Exception {
        // First create an employee as HR
        String response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user -> { user.addRole("HR"); return user; })
                .content(objectMapper.writeValueAsString(testEmployeeDTO)))
                .andReturn().getResponse().getContentAsString();
        
        EmployeeDTO createdEmployee = objectMapper.readValue(response, EmployeeDTO.class);

        // Update employee as manager
        testEmployeeDTO.setJobTitle("Senior Software Engineer");
        
        mockMvc.perform(put("/api/employees/" + createdEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEmployeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobTitle").value("Senior Software Engineer"));
    }

    @Test
    @WithMockUser(roles = "HR")
    void searchEmployees_ValidCriteria_Returns200() throws Exception {
        // First create an employee
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEmployeeDTO)))
                .andExpect(status().isCreated());

        // Search for the employee
        mockMvc.perform(get("/api/employees/search")
                .param("name", "John")
                .param("department", "IT")
                .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("John Doe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteEmployee_AdminRole_Returns204() throws Exception {
        // First create an employee
        String response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .with(user -> { user.addRole("HR"); return user; })
                .content(objectMapper.writeValueAsString(testEmployeeDTO)))
                .andReturn().getResponse().getContentAsString();
        
        EmployeeDTO createdEmployee = objectMapper.readValue(response, EmployeeDTO.class);

        // Delete the employee
        mockMvc.perform(delete("/api/employees/" + createdEmployee.getId()))
                .andExpect(status().isNoContent());
    }
}
