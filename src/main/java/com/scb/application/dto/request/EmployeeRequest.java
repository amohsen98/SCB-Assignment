package com.scb.application.dto.request;

import com.scb.application.constants.RegexConstants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    @Pattern(regexp = RegexConstants.NAME_PATTERN, message = "Name must be valid and contain only letters, spaces, and common name characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Salary is required")
    @PositiveOrZero(message = "Salary cannot be negative")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary cannot be negative")
    @Digits(integer = 10, fraction = 2, message = "Salary must be a valid number")
    private Double salary;

    @NotBlank(message = "Hire date is required")
    @Pattern(regexp = RegexConstants.DATE_PATTERN, message = "Hire date must be in the format yyyy-MM-dd")
    private String hireDate;

    @NotNull(message = "Department ID is required")
    private Long departmentId;
}
