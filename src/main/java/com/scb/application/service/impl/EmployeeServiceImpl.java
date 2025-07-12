package com.scb.application.service.impl;

import com.scb.application.dto.request.EmployeeRequest;
import com.scb.application.dto.response.EmployeeResponse;
import com.scb.application.entity.Department;
import com.scb.application.entity.Employee;
import com.scb.application.exception.ApiException;
import com.scb.application.exception.ErrorCode;
import com.scb.application.mapper.EmployeeMapper;
import com.scb.application.repository.DepartmentRepository;
import com.scb.application.repository.EmployeeRepository;
import com.scb.application.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        log.info("Creating new employee with email: {}", employeeRequest.getEmail());

        if (employeeRepository.existsByEmail(employeeRequest.getEmail())) {
            log.error("Employee with email {} already exists", employeeRequest.getEmail());
            throw new ApiException(
                "Employee with email " + employeeRequest.getEmail() + " already exists",
                ErrorCode.DUPLICATE_RESOURCE,
                HttpStatus.CONFLICT.value()
            );
        }

        Department department = departmentRepository.findById(employeeRequest.getDepartmentId())
            .orElseThrow(() -> {
                log.error("Department with ID {} not found", employeeRequest.getDepartmentId());
                return new ApiException(
                    "Department with ID " + employeeRequest.getDepartmentId() + " not found",
                    ErrorCode.RESOURCE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
                );
            });

        // Create new employee
        Employee employee = employeeMapper.toEntity(employeeRequest, department);

        Employee savedEmployee = employeeRepository.save(employee);
        log.info("Employee created successfully with ID: {}", savedEmployee.getId());

        return employeeMapper.toResponseDto(savedEmployee);
    }
}
