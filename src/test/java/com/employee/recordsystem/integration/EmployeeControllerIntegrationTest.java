package com.employee.recordsystem.integration;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.Department;
import com.employee.recordsystem.model.Employee;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EmployeeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department department;
    private Employee employee;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        departmentRepository.deleteAll();

        department = new Department();
        department.setName("IT Department");
        department = departmentRepository.save(department);

        employee = new Employee();
        employee.setEmployeeId("EMP001");
        employee.setFullName("John Doe");
        employee.setJobTitle("Software Engineer");
        employee.setDepartment(department);
        employee.setHireDate(LocalDate.now());
        employee.setEmploymentStatus(EmploymentStatus.ACTIVE);
        employee.setEmail("john.doe@company.com");
        employee.setPhone("+1234567890");
        employee.setAddress("123 Main St, City");
        employee = employeeRepository.save(employee);
    }

    @Test
    @WithMockUser(roles = "HR")
    void getAllEmployees() throws Exception {
        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].employeeId", is(employee.getEmployeeId())))
                .andExpect(jsonPath("$[0].fullName", is(employee.getFullName())));
    }

    @Test
    @WithMockUser(roles = "HR")
    void getEmployeeById() throws Exception {
        mockMvc.perform(get("/api/v1/employees/{id}", employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(employee.getEmployeeId())))
                .andExpect(jsonPath("$.fullName", is(employee.getFullName())));
    }

    @Test
    @WithMockUser(roles = "HR")
    void createEmployee() throws Exception {
        EmployeeDTO newEmployee = new EmployeeDTO();
        newEmployee.setEmployeeId("EMP002");
        newEmployee.setFullName("Jane Smith");
        newEmployee.setJobTitle("Senior Developer");
        newEmployee.setDepartmentId(department.getId());
        newEmployee.setHireDate(LocalDate.now());
        newEmployee.setEmploymentStatus(EmploymentStatus.ACTIVE);
        newEmployee.setEmail("jane.smith@company.com");
        newEmployee.setPhone("+1987654321");
        newEmployee.setAddress("456 Oak St, City");

        mockMvc.perform(post("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId", is(newEmployee.getEmployeeId())))
                .andExpect(jsonPath("$.fullName", is(newEmployee.getFullName())));
    }

    @Test
    @WithMockUser(roles = "HR")
    void updateEmployee() throws Exception {
        employee.setJobTitle("Senior Software Engineer");
        
        mockMvc.perform(put("/api/v1/employees/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobTitle", is("Senior Software Engineer")));
    }

    @Test
    @WithMockUser(roles = "HR")
    void deleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/{id}", employee.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/employees/{id}", employee.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "MANAGER")
    void managerCannotDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/{id}", employee.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedUserCannotAccessEmployees() throws Exception {
        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isUnauthorized());
    }
}
