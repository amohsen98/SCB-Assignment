package com.scb.application.repository;

import com.scb.application.entity.DailySummary;
import com.scb.application.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailySummaryRepository extends JpaRepository<DailySummary, Long> {
    
    List<DailySummary> findBySummaryDate(LocalDate summaryDate);
    
    Optional<DailySummary> findBySummaryDateAndDepartment(LocalDate summaryDate, Department department);
}