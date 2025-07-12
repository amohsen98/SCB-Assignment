package com.scb.application.service;

import com.scb.application.dto.request.DepartmentRequest;
import com.scb.application.dto.response.DepartmentResponse;

import java.util.List;

public interface DepartmentService {

    DepartmentResponse createDepartment(DepartmentRequest departmentRequest);

    List<DepartmentResponse> getAllDepartments();

    DepartmentResponse getDepartmentById(Long id);

    DepartmentResponse updateDepartment(Long id, DepartmentRequest departmentRequest);

    void deleteDepartment(Long id);
}