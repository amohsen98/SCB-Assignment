package com.scb.application.service;

import com.scb.application.entity.DailySummary;
import com.scb.application.entity.Department;

import java.time.LocalDate;
import java.util.List;

public interface DailySummaryService {
    
    /**
     * Generate daily summary for all departments
     * @param summaryDate the date for which to generate the summary
     * @return list of generated summaries
     */
    List<DailySummary> generateDailySummary(LocalDate summaryDate);
    
    /**
     * Get all summaries for a specific date
     * @param summaryDate the date for which to get summaries
     * @return list of summaries
     */
    List<DailySummary> getSummariesByDate(LocalDate summaryDate);
    
    /**
     * Get summary for a specific department on a specific date
     * @param summaryDate the date for which to get the summary
     * @param department the department for which to get the summary
     * @return the summary if found, otherwise null
     */
    DailySummary getSummaryByDateAndDepartment(LocalDate summaryDate, Department department);
}