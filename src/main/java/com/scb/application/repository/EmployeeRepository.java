package com.scb.application.repository;

import com.scb.application.entity.Department;
import com.scb.application.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    boolean existsByEmail(String email);

    int countByDepartment(Department department);

    @Query("SELECT e.department.id, COUNT(e) FROM Employee e GROUP BY e.department.id")
    List<Object[]> countEmployeesByDepartment();
}
