package com.example.employees.employees.web.dto;

import com.example.employees.employees.domain.Employee;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class EmployeeSearchResponseDto {
    private final Long employeeId;
    private final LocalDate birthDate;
    private final String firstName;
    private final String lastName;
    private final String gender;
    private final LocalDate hireDate;

    public static EmployeeSearchResponseDto fromEntity(Employee employee) {
        return new EmployeeSearchResponseDto(
                employee.getId(),
                employee.getBirthDate(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getGender().toString(),
                employee.getHireDate()
        );
    }
}
