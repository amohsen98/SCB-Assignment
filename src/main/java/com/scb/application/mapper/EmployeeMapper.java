package com.scb.application.mapper;

import com.scb.application.constants.RoleConstants;
import com.scb.application.dto.request.EmployeeRequest;
import com.scb.application.dto.response.EmployeeResponse;
import com.scb.application.entity.Department;
import com.scb.application.entity.Employee;
import com.scb.application.exception.ApiException;
import com.scb.application.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final PasswordEncoder passwordEncoder;

    @Value("${employee.default.password}")
    private String defaultPassword;

    public Employee toEntity(EmployeeRequest employeeRequest, Department department) {
        Employee employee = new Employee();
        employee.setName(employeeRequest.getName());
        employee.setEmail(employeeRequest.getEmail());
        employee.setRole(RoleConstants.USER);
        employee.setSalary(employeeRequest.getSalary());

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false); // Strict parsing to ensure valid dates
            Date hireDate = dateFormat.parse(employeeRequest.getHireDate());
            employee.setHireDate(hireDate);
        } catch (ParseException e) {
            throw new ApiException("Invalid hire date format. Expected format: yyyy-MM-dd", ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST.value());
        }

        employee.setDepartment(department);
        employee.setPassword(passwordEncoder.encode(defaultPassword));
        return employee;
    }

    public EmployeeResponse toResponseDto(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .role(employee.getRole())
                .salary(employee.getSalary())
                .hireDate(employee.getHireDate())
                .departmentId(employee.getDepartment().getId())
                .departmentName(employee.getDepartment().getName())
                .build();
    }
}
