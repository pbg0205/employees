package com.example.employees.employees.infra;

import com.example.employees.employees.domain.EmployeeSearchRepository;
import com.example.employees.employees.web.dto.EmployeeSearchResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.employees.employees.domain.QEmployee.employee;

@Repository
@RequiredArgsConstructor
public class EmployeeSearchRepositoryImpl implements EmployeeSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public EmployeeSearchResponseDto findByEmployeeId(Long employeeId) {
        return jpaQueryFactory.select(Projections.constructor(EmployeeSearchResponseDto.class, employee.id, employee.birthDate, employee.firstName, employee.lastName, employee.gender, employee.hireDate))
                .from(employee)
                .where(employee.id.eq(employeeId))
                .fetchOne();
    }
}
