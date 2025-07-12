package com.scb.application.service.impl;

import com.scb.application.dto.request.DepartmentRequest;
import com.scb.application.dto.response.DepartmentResponse;
import com.scb.application.entity.Department;
import com.scb.application.exception.ApiException;
import com.scb.application.mapper.DepartmentMapper;
import com.scb.application.repository.DepartmentRepository;
import com.scb.application.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentServiceImpl departmentService;

    private DepartmentRequest departmentRequest;
    private Department department;
    private DepartmentResponse departmentResponse;

    @BeforeEach
    void setUp() {
        // Setup common test data
        departmentRequest = new DepartmentRequest();
        departmentRequest.setName("IT");

        department = new Department();
        department.setId(1L);
        department.setName("IT");

        departmentResponse = new DepartmentResponse();
        departmentResponse.setId(1L);
        departmentResponse.setName("IT");
    }

    @Test
    void createDepartment_Success() {
        
        Department otherDept = new Department();
        otherDept.setId(3L);
        otherDept.setName("Finance");
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(otherDept));
        when(departmentMapper.toEntity(any(DepartmentRequest.class))).thenReturn(department);
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toResponseDto(any(Department.class))).thenReturn(departmentResponse);

   
        DepartmentResponse result = departmentService.createDepartment(departmentRequest);


        assertNotNull(result);
        assertEquals(departmentResponse.getId(), result.getId());
        assertEquals(departmentResponse.getName(), result.getName());

        verify(departmentRepository).findAll();
        verify(departmentMapper).toEntity(departmentRequest);
        verify(departmentRepository).save(department);
        verify(departmentMapper).toResponseDto(department);
    }

    @Test
    void createDepartment_NameAlreadyExists() {
        
        Department existingDepartment = new Department();
        existingDepartment.setId(2L);
        existingDepartment.setName("IT");

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(existingDepartment));
        
        ApiException exception = assertThrows(ApiException.class, () -> {
            departmentService.createDepartment(departmentRequest);
        });

        assertEquals("Department with name " + departmentRequest.getName() + " already exists", exception.getMessage());

        verify(departmentRepository).findAll();
        verify(departmentMapper, never()).toEntity(any());
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void getAllDepartments_Success() {
        
        List<Department> departments = Arrays.asList(department);
        when(departmentRepository.findAll()).thenReturn(departments);
        when(departmentMapper.toResponseDto(any(Department.class))).thenReturn(departmentResponse);

   
        List<DepartmentResponse> result = departmentService.getAllDepartments();


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(departmentResponse.getId(), result.get(0).getId());

        verify(departmentRepository).findAll();
        verify(departmentMapper, times(1)).toResponseDto(any(Department.class));
    }

    @Test
    void getDepartmentById_Success() {
        
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(departmentMapper.toResponseDto(any(Department.class))).thenReturn(departmentResponse);

   
        DepartmentResponse result = departmentService.getDepartmentById(1L);


        assertNotNull(result);
        assertEquals(departmentResponse.getId(), result.getId());

        verify(departmentRepository).findById(1L);
        verify(departmentMapper).toResponseDto(department);
    }

    @Test
    void getDepartmentById_NotFound() {
        
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        ApiException exception = assertThrows(ApiException.class, () -> {
            departmentService.getDepartmentById(1L);
        });

        assertEquals("Department with ID 1 not found", exception.getMessage());

        verify(departmentRepository).findById(1L);
        verify(departmentMapper, never()).toResponseDto(any());
    }

    @Test
    void updateDepartment_Success() {
        
        Department existingDepartment = new Department();
        existingDepartment.setId(1L);
        existingDepartment.setName("HR");

        // Change the name in the request
        departmentRequest.setName("IT");

        Department otherDept = new Department();
        otherDept.setId(3L);
        otherDept.setName("Finance");

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(otherDept));
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        when(departmentMapper.toResponseDto(any(Department.class))).thenReturn(departmentResponse);

   
        DepartmentResponse result = departmentService.updateDepartment(1L, departmentRequest);


        assertNotNull(result);
        assertEquals(departmentResponse.getId(), result.getId());

        verify(departmentRepository).findById(1L);
        verify(departmentRepository).findAll();
        verify(departmentRepository).save(any(Department.class));
        verify(departmentMapper).toResponseDto(any(Department.class));
    }

    @Test
    void updateDepartment_NotFound() {
        
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        ApiException exception = assertThrows(ApiException.class, () -> {
            departmentService.updateDepartment(1L, departmentRequest);
        });

        assertEquals("Department with ID 1 not found", exception.getMessage());

        verify(departmentRepository).findById(1L);
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void updateDepartment_NameAlreadyExists() {
        
        Department existingDepartment = new Department();
        existingDepartment.setId(1L);
        existingDepartment.setName("HR");

        Department anotherDepartment = new Department();
        anotherDepartment.setId(2L);
        anotherDepartment.setName("IT");

        departmentRequest.setName("IT");

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(existingDepartment));
        when(departmentRepository.findAll()).thenReturn(Arrays.asList(existingDepartment, anotherDepartment));


        ApiException exception = assertThrows(ApiException.class, () -> {
            departmentService.updateDepartment(1L, departmentRequest);
        });

        assertEquals("Department with name " + departmentRequest.getName() + " already exists", exception.getMessage());

        verify(departmentRepository).findById(1L);
        verify(departmentRepository).findAll();
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void deleteDepartment_Success() {
        
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(employeeRepository.countByDepartment(any(Department.class))).thenReturn(0);

   
        departmentService.deleteDepartment(1L);


        verify(departmentRepository).findById(1L);
        verify(employeeRepository).countByDepartment(department);
        verify(departmentRepository).deleteById(1L);
    }

    @Test
    void deleteDepartment_NotFound() {
        
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());


        ApiException exception = assertThrows(ApiException.class, () -> {
            departmentService.deleteDepartment(1L);
        });

        assertEquals("Department with ID 1 not found", exception.getMessage());

        verify(departmentRepository).findById(1L);
        verify(employeeRepository, never()).countByDepartment(any());
        verify(departmentRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteDepartment_HasEmployees() {
        
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(employeeRepository.countByDepartment(any(Department.class))).thenReturn(5);


        ApiException exception = assertThrows(ApiException.class, () -> {
            departmentService.deleteDepartment(1L);
        });

        assertEquals("Cannot delete department because it has employees assigned to it", exception.getMessage());

        verify(departmentRepository).findById(1L);
        verify(employeeRepository).countByDepartment(department);
        verify(departmentRepository, never()).deleteById(anyLong());
    }
}
