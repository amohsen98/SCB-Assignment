package com.scb.application.service.impl;

import com.scb.application.entity.DailySummary;
import com.scb.application.entity.Department;
import com.scb.application.repository.DailySummaryRepository;
import com.scb.application.repository.DepartmentRepository;
import com.scb.application.repository.EmployeeRepository;
import com.scb.application.service.DailySummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DailySummaryServiceImpl implements DailySummaryService {

    private final DailySummaryRepository dailySummaryRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public List<DailySummary> generateDailySummary(LocalDate summaryDate) {
        log.info("Generating daily summary for date: {}", summaryDate);
        
        List<Department> departments = departmentRepository.findAll();
        
        Map<Long, Long> employeeCountsByDepartmentId = employeeRepository.countEmployeesByDepartment()
                .stream()
                .collect(Collectors.toMap(
                        arr -> (Long) arr[0],
                        arr -> (Long) arr[1]
                ));
        
        List<DailySummary> summaries = new ArrayList<>();
        
        for (Department department : departments) {
            int employeeCount = employeeCountsByDepartmentId.getOrDefault(department.getId(), 0L).intValue();
            
            Optional<DailySummary> existingSummary = dailySummaryRepository.findBySummaryDateAndDepartment(summaryDate, department);
            
            DailySummary summary;
            if (existingSummary.isPresent()) {
                summary = existingSummary.get();
                summary.setEmployeeCount(employeeCount);
            } else {
                summary = new DailySummary(summaryDate, department, employeeCount);
            }
            summary = dailySummaryRepository.save(summary);
            summaries.add(summary);
            
            log.info("Generated summary for department {}: {} employees", department.getName(), employeeCount);
        }
        
        return summaries;
    }

    @Override
    public List<DailySummary> getSummariesByDate(LocalDate summaryDate) {
        return dailySummaryRepository.findBySummaryDate(summaryDate);
    }

    @Override
    public DailySummary getSummaryByDateAndDepartment(LocalDate summaryDate, Department department) {
        return dailySummaryRepository.findBySummaryDateAndDepartment(summaryDate, department).orElse(null);
    }
}