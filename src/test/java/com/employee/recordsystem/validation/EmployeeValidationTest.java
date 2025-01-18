package com.employee.recordsystem.validation;

import com.employee.recordsystem.dto.EmployeeDTO;
import com.employee.recordsystem.model.EmploymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeValidationTest {
    private Validator validator;
    private EmployeeDTO employee;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        // Create a valid employee for testing
        employee = new EmployeeDTO();
        employee.setEmployeeId("EMP123456");
        employee.setFullName("John Doe");
        employee.setJobTitle("Software Engineer");
        employee.setDepartmentId(1L);
        employee.setHireDate(LocalDate.now().minusDays(1));
        employee.setEmploymentStatus(EmploymentStatus.ACTIVE);
        employee.setEmail("john.doe@company.com");
        employee.setPhone("+1234567890");
        employee.setAddress("123 Main St, City, Country");
    }

    @Test
    void whenAllFieldsValid_thenNoValidationErrors() {
        var violations = validator.validate(employee);
        assertTrue(violations.isEmpty(), "Should have no validation errors");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "EMP12", "123456", "EMPL123456", "EMP12345A"})
    void whenInvalidEmployeeId_thenValidationError(String invalidId) {
        employee.setEmployeeId(invalidId);
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors");
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("employeeId")),
            "Should have employee ID validation error");
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "A"})
    void whenInvalidFullName_thenValidationError(String invalidName) {
        employee.setFullName(invalidName);
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors");
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("fullName")),
            "Should have full name validation error");
    }

    @ParameterizedTest
    @CsvSource({
        "john.doe@,false",
        "@company.com,false",
        "john.doe@company,false",
        "john.doe@company.com,true",
        "john.doe+work@company.co.uk,true"
    })
    void testEmailValidation(String email, boolean isValid) {
        employee.setEmail(email);
        var violations = validator.validate(employee);
        assertEquals(isValid, violations.isEmpty(),
            String.format("Email '%s' validity should be %s", email, isValid));
    }

    @ParameterizedTest
    @CsvSource({
        "+1234567890,true",
        "1234567890,true",
        "+123,false",
        "abc1234567,false",
        "+12345678901234567,false"
    })
    void testPhoneValidation(String phone, boolean isValid) {
        employee.setPhone(phone);
        var violations = validator.validate(employee);
        assertEquals(isValid, violations.isEmpty(),
            String.format("Phone '%s' validity should be %s", phone, isValid));
    }

    @Test
    void whenFutureDateHireDate_thenValidationError() {
        employee.setHireDate(LocalDate.now().plusDays(1));
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors");
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("hireDate")),
            "Should have hire date validation error");
    }

    @Test
    void whenNullEmploymentStatus_thenValidationError() {
        employee.setEmploymentStatus(null);
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors");
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("employmentStatus")),
            "Should have employment status validation error");
    }

    @Test
    void whenAddressTooLong_thenValidationError() {
        employee.setAddress("A".repeat(256));
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors");
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("address")),
            "Should have address validation error");
    }
}
