package com.example.employees.employees.web.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class EmployeeSearchResponseDto {
    private final Long employeeId;
    private final LocalDate birthDate;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final LocalDate hireDate;

    @QueryProjection
    public EmployeeSearchResponseDto(
            Long employeeId,
            LocalDate birthDate,
            String firstName,
            String lastName,
            String gender,
            LocalDate hireDate
    ) {
        this.employeeId = employeeId;
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.hireDate = hireDate;
    }
}
