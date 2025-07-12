package com.scb.application.service;

import com.scb.application.dto.request.EmployeeRequest;
import com.scb.application.dto.response.EmployeeResponse;

public interface EmployeeService {

    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);
}