package com.example.employees.employees.application;

import com.example.employees.employees.domain.EmployeeSearchRepository;
import com.example.employees.employees.web.dto.EmployeeSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeSearchServiceImpl implements EmployeeSearchService {

    private final EmployeeSearchRepository employeeSearchRepository;

    @Override
    public EmployeeSearchResponseDto searchEmployeeById(Long employeeId) {
        return employeeSearchRepository.findByEmployeeId(employeeId);
    }
}
