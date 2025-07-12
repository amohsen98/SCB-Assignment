package com.scb.application.service.impl;

import com.scb.application.dto.request.EmployeeRequest;
import com.scb.application.dto.response.EmployeeResponse;
import com.scb.application.entity.Department;
import com.scb.application.entity.Employee;
import com.scb.application.exception.ApiException;
import com.scb.application.mapper.EmployeeMapper;
import com.scb.application.repository.DepartmentRepository;
import com.scb.application.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private EmployeeRequest employeeRequest;
    private Employee employee;
    private Department department;
    private EmployeeResponse employeeResponse;
    private Date hireDate;

    @BeforeEach
    void setUp() throws ParseException {
        // Setup common test data
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        hireDate = dateFormat.parse("2023-01-01");

        department = new Department();
        department.setId(1L);
        department.setName("IT");

        employee = new Employee();
        employee.setId(1L);
        employee.setName("ali");
        employee.setEmail("ali@example.com");
        employee.setSalary(50000.0);
        employee.setHireDate(hireDate);
        employee.setDepartment(department);

        employeeRequest = new EmployeeRequest();
        employeeRequest.setName("mohamed");
        employeeRequest.setEmail("mohamed@example.com");
        employeeRequest.setSalary(50000.0);
        employeeRequest.setHireDate("2023-01-01");
        employeeRequest.setDepartmentId(1L);

        employeeResponse = new EmployeeResponse();
        employeeResponse.setId(1L);
        employeeResponse.setName("ahmed");
        employeeResponse.setEmail("ahmede@example.com");
        employeeResponse.setSalary(50000.0);
        employeeResponse.setHireDate(hireDate);
        employeeResponse.setDepartmentId(1L);
        employeeResponse.setDepartmentName("IT");
    }

    @Test
    void createEmployee_Success() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(employeeMapper.toEntity(any(EmployeeRequest.class), any(Department.class))).thenReturn(employee);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponse);

      
        EmployeeResponse result = employeeService.createEmployee(employeeRequest);
        
        assertNotNull(result);
        assertEquals(employeeResponse.getId(), result.getId());
        assertEquals(employeeResponse.getName(), result.getName());
        assertEquals(employeeResponse.getEmail(), result.getEmail());

        verify(employeeRepository).existsByEmail(employeeRequest.getEmail());
        verify(departmentRepository).findById(employeeRequest.getDepartmentId());
        verify(employeeMapper).toEntity(employeeRequest, department);
        verify(employeeRepository).save(employee);
        verify(employeeMapper).toResponseDto(employee);
    }

    @Test
    void createEmployee_EmailAlreadyExists() {
        
        when(employeeRepository.existsByEmail(anyString())).thenReturn(true);


        ApiException exception = assertThrows(ApiException.class, () -> {
            employeeService.createEmployee(employeeRequest);
        });

        assertEquals("Employee with email " + employeeRequest.getEmail() + " already exists", exception.getMessage());

        verify(employeeRepository).existsByEmail(employeeRequest.getEmail());
        verify(departmentRepository, never()).findById(anyLong());
        verify(employeeMapper, never()).toEntity(any(), any());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void createEmployee_DepartmentNotFound() {
        when(employeeRepository.existsByEmail(anyString())).thenReturn(false);
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        ApiException exception = assertThrows(ApiException.class, () -> {
            employeeService.createEmployee(employeeRequest);
        });

        assertEquals("Department with ID " + employeeRequest.getDepartmentId() + " not found", exception.getMessage());

        verify(employeeRepository).existsByEmail(employeeRequest.getEmail());
        verify(departmentRepository).findById(employeeRequest.getDepartmentId());
        verify(employeeMapper, never()).toEntity(any(), any());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void getAllEmployees_Success() {
        
        List<Employee> employees = Arrays.asList(employee);
        when(employeeRepository.findAll()).thenReturn(employees);
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponse);

   
        List<EmployeeResponse> result = employeeService.getAllEmployees();


        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(employeeResponse.getId(), result.get(0).getId());

        verify(employeeRepository).findAll();
        verify(employeeMapper, times(1)).toResponseDto(any(Employee.class));
    }

    @Test
    void getEmployeeById_Success() {

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        OngoingStubbing<EmployeeResponse> employeeResponseOngoingStubbing = when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponse);


        EmployeeResponse result = employeeService.getEmployeeById(1L);
        
        assertNotNull(result);
        assertEquals(employeeResponse.getId(), result.getId());

        verify(employeeRepository).findById(1L);
        verify(employeeMapper).toResponseDto(employee);
    }

    @Test
    void getEmployeeById_NotFound() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        
        ApiException exception = assertThrows(ApiException.class, () -> {
            employeeService.getEmployeeById(1L);
        });

        assertEquals("Employee with ID 1 not found", exception.getMessage());

        verify(employeeRepository).findById(1L);
        verify(employeeMapper, never()).toResponseDto(any());
    }

    @Test
    void updateEmployee_Success() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(employeeMapper.toResponseDto(any(Employee.class))).thenReturn(employeeResponse);
        
        EmployeeResponse result = employeeService.updateEmployee(1L, employeeRequest);
        
        assertNotNull(result);
        assertEquals(employeeResponse.getId(), result.getId());

        verify(employeeRepository).findById(1L);
        verify(departmentRepository).findById(employeeRequest.getDepartmentId());
        verify(employeeRepository).save(employee);
        verify(employeeMapper).toResponseDto(employee);
    }

    @Test
    void updateEmployee_NotFound() {

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());


        ApiException exception = assertThrows(ApiException.class, () -> {
            employeeService.updateEmployee(1L, employeeRequest);
        });

        assertEquals("Employee with ID 1 not found", exception.getMessage());

        verify(employeeRepository).findById(1L);
        verify(departmentRepository, never()).findById(anyLong());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployee_EmailAlreadyExists() {

        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);
        existingEmployee.setEmail("old.email@example.com");

        employeeRequest.setEmail("new.email@example.com");

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.existsByEmail(employeeRequest.getEmail())).thenReturn(true);


        ApiException exception = assertThrows(ApiException.class, () -> {
            employeeService.updateEmployee(1L, employeeRequest);
        });

        assertEquals("Employee with email " + employeeRequest.getEmail() + " already exists", exception.getMessage());

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).existsByEmail(employeeRequest.getEmail());
        verify(departmentRepository, never()).findById(anyLong());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void deleteEmployee_Success() {
 
        when(employeeRepository.existsById(anyLong())).thenReturn(true);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));


        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn("different.email@example.com");

        employeeService.deleteEmployee(1L);


        verify(employeeRepository).existsById(1L);
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).deleteById(1L);
    }

    @Test
    void deleteEmployee_NotFound() {
        when(employeeRepository.existsById(anyLong())).thenReturn(false);

        ApiException exception = assertThrows(ApiException.class, () -> {
            employeeService.deleteEmployee(1L);
        });

        assertEquals("Employee with ID 1 not found", exception.getMessage());

        verify(employeeRepository).existsById(1L);
        verify(employeeRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteEmployee_CannotDeleteOwnAccount() {
        
        when(employeeRepository.existsById(anyLong())).thenReturn(true);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getName()).thenReturn(employee.getEmail());
        
        ApiException exception = assertThrows(ApiException.class, () -> {
            employeeService.deleteEmployee(1L);
        });

        assertEquals("You cannot delete your own account", exception.getMessage());

        verify(employeeRepository).existsById(1L);
        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}
