package com.scb.application.mapper;

import com.scb.application.dto.request.DepartmentRequest;
import com.scb.application.dto.response.DepartmentResponse;
import com.scb.application.entity.Department;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public Department toEntity(DepartmentRequest departmentRequest) {
        Department department = new Department();
        department.setName(departmentRequest.getName());
        return department;
    }

    public DepartmentResponse toResponseDto(Department department) {
        return DepartmentResponse.builder()
                .id(department.getId())
                .name(department.getName())
                .build();
    }
}