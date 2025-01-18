package com.employee.recordsystem.performance;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.Department;
import com.employee.recordsystem.model.EmploymentStatus;
import com.employee.recordsystem.repository.DepartmentRepository;
import com.employee.recordsystem.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class EmployeePerformanceTest {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Department testDepartment;

    @BeforeEach
    void setUp() {
        testDepartment = new Department();
        testDepartment.setName("Performance Test Dept");
        testDepartment = departmentRepository.save(testDepartment);
    }

    @Test
    void bulkCreateEmployees_ShouldCompleteWithinTimeLimit() {
        int numberOfEmployees = 1000;
        Instant start = Instant.now();

        List<EmployeeDTO> employees = new ArrayList<>();
        for (int i = 0; i < numberOfEmployees; i++) {
            employees.add(createTestEmployee(i));
        }

        employees.forEach(employeeService::createEmployee);

        Duration duration = Duration.between(start, Instant.now());
        assertTrue(duration.getSeconds() < 30, 
            "Bulk creation of " + numberOfEmployees + " employees took too long: " + duration.getSeconds() + " seconds");
    }

    @Test
    void concurrentEmployeeSearch_ShouldHandleMultipleRequests() throws Exception {
        // First create some test data
        int numberOfEmployees = 100;
        for (int i = 0; i < numberOfEmployees; i++) {
            employeeService.createEmployee(createTestEmployee(i));
        }

        int numberOfConcurrentSearches = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<CompletableFuture<List<EmployeeDTO>>> futures = new ArrayList<>();

        Instant start = Instant.now();

        // Perform concurrent searches
        for (int i = 0; i < numberOfConcurrentSearches; i++) {
            CompletableFuture<List<EmployeeDTO>> future = CompletableFuture.supplyAsync(() ->
                employeeService.searchEmployees("Test", null, "Performance Test Dept", 
                    null, EmploymentStatus.ACTIVE, null, null),
                executorService
            );
            futures.add(future);
        }

        // Wait for all searches to complete
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        Duration duration = Duration.between(start, Instant.now());
        assertTrue(duration.getSeconds() < 10, 
            "Concurrent searches took too long: " + duration.getSeconds() + " seconds");

        executorService.shutdown();
    }

    @Test
    @Transactional
    void largeResultSetPagination_ShouldBeEfficient() {
        // Create large dataset
        int totalEmployees = 1000;
        IntStream.range(0, totalEmployees)
            .forEach(i -> employeeService.createEmployee(createTestEmployee(i)));

        int pageSize = 50;
        Instant start = Instant.now();

        // Test pagination performance
        for (int page = 0; page < totalEmployees/pageSize; page++) {
            List<EmployeeDTO> employees = employeeService.getEmployeesPaginated(page, pageSize);
            assertEquals(pageSize, employees.size());
        }

        Duration duration = Duration.between(start, Instant.now());
        assertTrue(duration.getSeconds() < 15, 
            "Pagination of " + totalEmployees + " employees took too long: " + duration.getSeconds() + " seconds");
    }

    private EmployeeDTO createTestEmployee(int index) {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeId(String.format("EMP%06d", index));
        employee.setFullName("Test Employee " + index);
        employee.setJobTitle("Performance Tester");
        employee.setDepartmentId(testDepartment.getId());
        employee.setHireDate(LocalDate.now().minusDays(1));
        employee.setEmploymentStatus(EmploymentStatus.ACTIVE);
        employee.setEmail("test" + index + "@company.com");
        employee.setPhone("+1234567890");
        employee.setAddress("123 Performance Street");
        return employee;
    }
}
