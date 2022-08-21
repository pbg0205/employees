package com.example.employees.employees.domain;

import com.example.employees.employees.web.dto.EmployeeSearchResponseDto;

public interface EmployeeSearchRepository {
    EmployeeSearchResponseDto findByEmployeeId(Long employeeId);
}
