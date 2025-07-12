package com.scb.application.controller;

import com.scb.application.annotation.AdminOnly;
import com.scb.application.dto.request.EmployeeRequest;
import com.scb.application.dto.response.EmployeeResponse;
import com.scb.application.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @AdminOnly
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
        EmployeeResponse createdEmployee = employeeService.createEmployee(employeeRequest);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }
}
