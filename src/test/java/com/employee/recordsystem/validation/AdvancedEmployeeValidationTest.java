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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdvancedEmployeeValidationTest {
    private Validator validator;
    private EmployeeDTO employee;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        employee = new EmployeeDTO();
        employee.setEmployeeId("EMP123456");
        employee.setFullName("John Doe");
        employee.setJobTitle("Software Engineer");
        employee.setDepartmentId(1L);
        employee.setHireDate(LocalDate.now().minusDays(1));
        employee.setEmploymentStatus(EmploymentStatus.ACTIVE);
        employee.setEmail("john.doe@company.com");
        employee.setPhone("+1234567890");
        employee.setAddress("123 Main St");
    }

    // Edge Cases for Employee ID
    @ParameterizedTest
    @ValueSource(strings = {
        "EMP0000001", // Too long
        "EMP12345",   // Too short
        "emp123456",  // Wrong case
        "ABC123456",  // Wrong prefix
        "EMP12345A",  // Contains letter
        "EMP-12345"   // Contains special character
    })
    void invalidEmployeeId_ShouldFailValidation(String invalidId) {
        employee.setEmployeeId(invalidId);
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors for: " + invalidId);
    }

    // Email Format Edge Cases
    @ParameterizedTest
    @ValueSource(strings = {
        "john.doe@",           // Incomplete
        "@company.com",        // No local part
        "john.doe@company",    // No TLD
        "john.doe@.com",       // Missing domain
        "john.doe@com.",       // Trailing dot
        "john..doe@company.com", // Double dot
        "john.doe@company..com"  // Double dot in domain
    })
    void invalidEmailFormat_ShouldFailValidation(String invalidEmail) {
        employee.setEmail(invalidEmail);
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors for: " + invalidEmail);
    }

    // Phone Number Format Edge Cases
    @ParameterizedTest
    @ValueSource(strings = {
        "+",            // Just plus
        "12345",       // No plus
        "+abcdefghij", // Letters
        "++12345678",  // Double plus
        "+123456789012345", // Too long
        "+0123456789"  // Starts with 0
    })
    void invalidPhoneFormat_ShouldFailValidation(String invalidPhone) {
        employee.setPhone(invalidPhone);
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors for: " + invalidPhone);
    }

    // Full Name Edge Cases
    @ParameterizedTest
    @ValueSource(strings = {
        "A",                    // Too short
        "J",                    // Single character
        "John123",             // Contains numbers
        "John@Doe",            // Contains special characters
        "John                Doe", // Multiple spaces
        "    John",            // Leading spaces
        "Doe    "              // Trailing spaces
    })
    void invalidFullName_ShouldFailValidation(String invalidName) {
        employee.setFullName(invalidName);
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors for: " + invalidName);
    }

    // Hire Date Edge Cases
    @Test
    void futureDateHire_ShouldFailValidation() {
        employee.setHireDate(LocalDate.now().plusDays(1));
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors for future hire date");
    }

    @Test
    void veryOldHireDate_ShouldFailValidation() {
        employee.setHireDate(LocalDate.now().minusYears(100));
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors for very old hire date");
    }

    // Address Edge Cases
    @ParameterizedTest
    @ValueSource(strings = {
        "",                     // Empty
        " ",                    // Just space
        "   ",                  // Multiple spaces
        "A",                    // Too short
        "123"                   // Just numbers
    })
    void invalidAddress_ShouldFailValidation(String invalidAddress) {
        employee.setAddress(invalidAddress);
        var violations = validator.validate(employee);
        assertFalse(violations.isEmpty(), "Should have validation errors for: " + invalidAddress);
    }

    // Multiple Field Validation
    @Test
    void multipleInvalidFields_ShouldReturnAllErrors() {
        employee.setEmployeeId("invalid");
        employee.setEmail("invalid");
        employee.setPhone("invalid");
        
        var violations = validator.validate(employee);
        assertEquals(3, violations.size(), "Should have exactly 3 validation errors");
    }

    // Unicode Character Tests
    @Test
    void unicodeCharactersInName_ShouldBeValid() {
        employee.setFullName("José María García");
        var violations = validator.validate(employee);
        assertTrue(violations.isEmpty(), "Should accept valid Unicode characters in name");
    }

    @Test
    void unicodeCharactersInAddress_ShouldBeValid() {
        employee.setAddress("123 Calle España, München");
        var violations = validator.validate(employee);
        assertTrue(violations.isEmpty(), "Should accept valid Unicode characters in address");
    }
}
