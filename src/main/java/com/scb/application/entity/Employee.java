package com.scb.application.entity;

import com.scb.application.constants.RoleConstants;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "salary", nullable = false)
    private Double salary;

    @Column(name = "hire_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    @ToString.Exclude
    private Department department;

    public Employee(String name, String email, Double salary, Date hireDate, Department department) {
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.hireDate = hireDate;
        this.department = department;
        this.role = RoleConstants.USER; // Default role
    }
}
