package com.example.employees.employees.web;

import com.example.employees.employees.application.EmployeeSearchService;
import com.example.employees.employees.web.dto.EmployeeSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeSearchController {

    private final EmployeeSearchService employeeSearchService;

    @GetMapping("/{employeeId}")
    public final ResponseEntity<EmployeeSearchResponseDto> searchEmployeeById(@PathVariable final Long employeeId) {
        return ResponseEntity.ok(employeeSearchService.searchEmployeeById(employeeId));
    }
}
