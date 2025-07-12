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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {
        log.info("Fetching all employees");
        List<Employee> employees = employeeRepository.findAll();
        log.info("Found {} employees", employees.size());
        return employees.stream()
                .map(employeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {
        log.info("Fetching employee with ID: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee with ID {} not found", id);
                    return new ApiException(
                            "Employee with ID " + id + " not found",
                            ErrorCode.RESOURCE_NOT_FOUND,
                            HttpStatus.NOT_FOUND.value()
                    );
                });
        log.info("Found employee with ID: {}", id);
        return employeeMapper.toResponseDto(employee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest employeeRequest) {
        log.info("Updating employee with ID: {}", id);

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee with ID {} not found", id);
                    return new ApiException(
                            "Employee with ID " + id + " not found",
                            ErrorCode.RESOURCE_NOT_FOUND,
                            HttpStatus.NOT_FOUND.value()
                    );
                });

        // Check if email is being changed and if the new email already exists
        if (!existingEmployee.getEmail().equals(employeeRequest.getEmail()) && 
                employeeRepository.existsByEmail(employeeRequest.getEmail())) {
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

        existingEmployee.setName(employeeRequest.getName());
        existingEmployee.setEmail(employeeRequest.getEmail());
        existingEmployee.setSalary(employeeRequest.getSalary());
        existingEmployee.setDepartment(department);
        try {
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            java.util.Date hireDate = dateFormat.parse(employeeRequest.getHireDate());
            existingEmployee.setHireDate(hireDate);
        } catch (java.text.ParseException e) {
            throw new ApiException(
                    "Invalid hire date format. Expected format: yyyy-MM-dd",
                    ErrorCode.VALIDATION_ERROR,
                    HttpStatus.BAD_REQUEST.value()
            );
        }

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        log.info("Employee with ID: {} updated successfully", id);

        return employeeMapper.toResponseDto(updatedEmployee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with ID: {}", id);

        if (!employeeRepository.existsById(id)) {
            log.error("Employee with ID {} not found", id);
            throw new ApiException(
                    "Employee with ID " + id + " not found",
                    ErrorCode.RESOURCE_NOT_FOUND,
                    HttpStatus.NOT_FOUND.value()
            );
        }

        Employee employeeToDelete = employeeRepository.findById(id).get();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String currentUserEmail = authentication.getName();

            // Check if the employee being deleted is the current user
            if (employeeToDelete.getEmail().equals(currentUserEmail)) {
                log.error("Cannot delete your own account");
                throw new ApiException(
                        "You cannot delete your own account",
                        ErrorCode.VALIDATION_ERROR,
                        HttpStatus.BAD_REQUEST.value()
                );
            }
        }

        employeeRepository.deleteById(id);
        log.info("Employee with ID: {} deleted successfully", id);
    }
}
