package com.scb.application.service.impl;

import com.scb.application.dto.request.DepartmentRequest;
import com.scb.application.dto.response.DepartmentResponse;
import com.scb.application.entity.Department;
import com.scb.application.exception.ApiException;
import com.scb.application.exception.ErrorCode;
import com.scb.application.mapper.DepartmentMapper;
import com.scb.application.repository.DepartmentRepository;
import com.scb.application.repository.EmployeeRepository;
import com.scb.application.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    @Transactional
    public DepartmentResponse createDepartment(DepartmentRequest departmentRequest) {
        log.info("Creating new department with name: {}", departmentRequest.getName());

        if (departmentRepository.findAll().stream()
                .anyMatch(dept -> dept.getName().equalsIgnoreCase(departmentRequest.getName()))) {
            log.error("Department with name {} already exists", departmentRequest.getName());
            throw new ApiException(
                    "Department with name " + departmentRequest.getName() + " already exists",
                    ErrorCode.DUPLICATE_RESOURCE,
                    HttpStatus.CONFLICT.value()
            );
        }

        Department department = departmentMapper.toEntity(departmentRequest);
        Department savedDepartment = departmentRepository.save(department);
        log.info("Department created successfully with ID: {}", savedDepartment.getId());

        return departmentMapper.toResponseDto(savedDepartment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getAllDepartments() {
        log.info("Fetching all departments");
        List<Department> departments = departmentRepository.findAll();
        log.info("Found {} departments", departments.size());
        return departments.stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartmentById(Long id) {
        log.info("Fetching department with ID: {}", id);
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Department with ID {} not found", id);
                    return new ApiException(
                            "Department with ID " + id + " not found",
                            ErrorCode.RESOURCE_NOT_FOUND,
                            HttpStatus.NOT_FOUND.value()
                    );
                });
        log.info("Found department with ID: {}", id);
        return departmentMapper.toResponseDto(department);
    }

    @Override
    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest departmentRequest) {
        log.info("Updating department with ID: {}", id);

        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Department with ID {} not found", id);
                    return new ApiException(
                            "Department with ID " + id + " not found",
                            ErrorCode.RESOURCE_NOT_FOUND,
                            HttpStatus.NOT_FOUND.value()
                    );
                });

        if (!existingDepartment.getName().equalsIgnoreCase(departmentRequest.getName()) &&
                departmentRepository.findAll().stream()
                        .anyMatch(dept -> dept.getName().equalsIgnoreCase(departmentRequest.getName()))) {
            log.error("Department with name {} already exists", departmentRequest.getName());
            throw new ApiException(
                    "Department with name " + departmentRequest.getName() + " already exists",
                    ErrorCode.DUPLICATE_RESOURCE,
                    HttpStatus.CONFLICT.value()
            );
        }

        existingDepartment.setName(departmentRequest.getName());
        Department updatedDepartment = departmentRepository.save(existingDepartment);
        log.info("Department with ID: {} updated successfully", id);

        return departmentMapper.toResponseDto(updatedDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(Long id) {
        log.info("Deleting department with ID: {}", id);

        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Department with ID {} not found", id);
                    return new ApiException(
                            "Department with ID " + id + " not found",
                            ErrorCode.RESOURCE_NOT_FOUND,
                            HttpStatus.NOT_FOUND.value()
                    );
                });

        if (employeeRepository.countByDepartment(department) > 0) {
            log.error("Cannot delete department with ID {} because it has employees assigned to it", id);
            throw new ApiException(
                    "Cannot delete department because it has employees assigned to it",
                    ErrorCode.VALIDATION_ERROR,
                    HttpStatus.BAD_REQUEST.value()
            );
        }

        departmentRepository.deleteById(id);
        log.info("Department with ID: {} deleted successfully", id);
    }
}
