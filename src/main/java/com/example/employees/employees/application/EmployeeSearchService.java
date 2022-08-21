package com.example.employees.employees.application;

import com.example.employees.employees.web.dto.EmployeeSearchResponseDto;

public interface EmployeeSearchService {
    EmployeeSearchResponseDto searchEmployeeById(final Long employeeId);
}
