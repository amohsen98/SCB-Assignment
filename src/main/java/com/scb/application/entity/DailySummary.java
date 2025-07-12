package com.scb.application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "daily_summary")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DailySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "summary_date", nullable = false)
    private LocalDate summaryDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "employee_count", nullable = false)
    private Integer employeeCount;

    public DailySummary(LocalDate summaryDate, Department department, Integer employeeCount) {
        this.summaryDate = summaryDate;
        this.department = department;
        this.employeeCount = employeeCount;
    }
}