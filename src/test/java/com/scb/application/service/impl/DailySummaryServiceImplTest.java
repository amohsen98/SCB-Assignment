package com.scb.application.service.impl;

import com.scb.application.entity.DailySummary;
import com.scb.application.entity.Department;
import com.scb.application.repository.DailySummaryRepository;
import com.scb.application.repository.DepartmentRepository;
import com.scb.application.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailySummaryServiceImplTest {

    @Mock
    private DailySummaryRepository dailySummaryRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private DailySummaryServiceImpl dailySummaryService;

    private LocalDate summaryDate;
    private Department department1;
    private Department department2;
    private DailySummary dailySummary1;
    private DailySummary dailySummary2;
    private List<Object[]> employeeCountsByDepartment;

    @BeforeEach
    void setUp() {
        // Setup common test data
        summaryDate = LocalDate.of(2023, 1, 1);
        
        department1 = new Department();
        department1.setId(1L);
        department1.setName("IT");
        
        department2 = new Department();
        department2.setId(2L);
        department2.setName("HR");
        
        dailySummary1 = new DailySummary(summaryDate, department1, 5);
        dailySummary2 = new DailySummary(summaryDate, department2, 3);
        
        // Setup employee counts by department
        employeeCountsByDepartment = new ArrayList<>();
        employeeCountsByDepartment.add(new Object[]{1L, 5L});
        employeeCountsByDepartment.add(new Object[]{2L, 3L});
    }

    @Test
    void generateDailySummary_NewSummaries() {
        
        List<Department> departments = Arrays.asList(department1, department2);
        
        when(departmentRepository.findAll()).thenReturn(departments);
        when(employeeRepository.countEmployeesByDepartment()).thenReturn(employeeCountsByDepartment);
        when(dailySummaryRepository.findBySummaryDateAndDepartment(any(LocalDate.class), any(Department.class)))
            .thenReturn(Optional.empty());
        when(dailySummaryRepository.save(any(DailySummary.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

   
        List<DailySummary> result = dailySummaryService.generateDailySummary(summaryDate);


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getEmployeeCount());
        assertEquals(3, result.get(1).getEmployeeCount());
        
        verify(departmentRepository).findAll();
        verify(employeeRepository).countEmployeesByDepartment();
        verify(dailySummaryRepository, times(2)).findBySummaryDateAndDepartment(any(LocalDate.class), any(Department.class));
        verify(dailySummaryRepository, times(2)).save(any(DailySummary.class));
    }

    @Test
    void generateDailySummary_UpdateExistingSummaries() {
        
        List<Department> departments = Arrays.asList(department1, department2);
        
        when(departmentRepository.findAll()).thenReturn(departments);
        when(employeeRepository.countEmployeesByDepartment()).thenReturn(employeeCountsByDepartment);
        when(dailySummaryRepository.findBySummaryDateAndDepartment(eq(summaryDate), eq(department1)))
            .thenReturn(Optional.of(dailySummary1));
        when(dailySummaryRepository.findBySummaryDateAndDepartment(eq(summaryDate), eq(department2)))
            .thenReturn(Optional.of(dailySummary2));
        when(dailySummaryRepository.save(any(DailySummary.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

   
        List<DailySummary> result = dailySummaryService.generateDailySummary(summaryDate);


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getEmployeeCount());
        assertEquals(3, result.get(1).getEmployeeCount());
        
        verify(departmentRepository).findAll();
        verify(employeeRepository).countEmployeesByDepartment();
        verify(dailySummaryRepository, times(2)).findBySummaryDateAndDepartment(any(LocalDate.class), any(Department.class));
        verify(dailySummaryRepository, times(2)).save(any(DailySummary.class));
    }

    @Test
    void generateDailySummary_MixedNewAndExistingSummaries() {
        
        List<Department> departments = Arrays.asList(department1, department2);
        
        when(departmentRepository.findAll()).thenReturn(departments);
        when(employeeRepository.countEmployeesByDepartment()).thenReturn(employeeCountsByDepartment);
        when(dailySummaryRepository.findBySummaryDateAndDepartment(eq(summaryDate), eq(department1)))
            .thenReturn(Optional.of(dailySummary1));
        when(dailySummaryRepository.findBySummaryDateAndDepartment(eq(summaryDate), eq(department2)))
            .thenReturn(Optional.empty());
        when(dailySummaryRepository.save(any(DailySummary.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

   
        List<DailySummary> result = dailySummaryService.generateDailySummary(summaryDate);


        assertNotNull(result);
        assertEquals(2, result.size());
        
        verify(departmentRepository).findAll();
        verify(employeeRepository).countEmployeesByDepartment();
        verify(dailySummaryRepository, times(2)).findBySummaryDateAndDepartment(any(LocalDate.class), any(Department.class));
        verify(dailySummaryRepository, times(2)).save(any(DailySummary.class));
    }

    @Test
    void getSummariesByDate() {
        
        List<DailySummary> summaries = Arrays.asList(dailySummary1, dailySummary2);
        when(dailySummaryRepository.findBySummaryDate(any(LocalDate.class))).thenReturn(summaries);

   
        List<DailySummary> result = dailySummaryService.getSummariesByDate(summaryDate);


        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(dailySummary1, result.get(0));
        assertEquals(dailySummary2, result.get(1));
        
        verify(dailySummaryRepository).findBySummaryDate(summaryDate);
    }

    @Test
    void getSummaryByDateAndDepartment_Found() {
        
        when(dailySummaryRepository.findBySummaryDateAndDepartment(any(LocalDate.class), any(Department.class)))
            .thenReturn(Optional.of(dailySummary1));

   
        DailySummary result = dailySummaryService.getSummaryByDateAndDepartment(summaryDate, department1);


        assertNotNull(result);
        assertEquals(dailySummary1, result);
        
        verify(dailySummaryRepository).findBySummaryDateAndDepartment(summaryDate, department1);
    }

    @Test
    void getSummaryByDateAndDepartment_NotFound() {
        
        when(dailySummaryRepository.findBySummaryDateAndDepartment(any(LocalDate.class), any(Department.class)))
            .thenReturn(Optional.empty());

   
        DailySummary result = dailySummaryService.getSummaryByDateAndDepartment(summaryDate, department1);


        assertNull(result);
        
        verify(dailySummaryRepository).findBySummaryDateAndDepartment(summaryDate, department1);
    }
}