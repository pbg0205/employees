package com.example.employees.employees.web;

import com.example.employees.employees.application.EmployeeSearchService;
import com.example.employees.employees.domain.Employee;
import com.example.employees.employees.domain.Gender;
import com.example.employees.employees.web.dto.EmployeeSearchResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = EmployeeSearchController.class)
class EmployeeSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeSearchService employeeSearchService;

    @DisplayName("id를 통해 사원을 조회한다")
    @Test
    void searchEmployeeById() throws Exception {
        //given
        Employee employee = new Employee(
                10001L,
                LocalDate.of(1999, 1, 1),
                "firstName",
                "lastName",
                Gender.M,
                LocalDate.of(2022, 8, 21)
        );

        EmployeeSearchResponseDto employeeSearchResponseDto = EmployeeSearchResponseDto.fromEntity(employee);
        given(employeeSearchService.searchEmployeeById(any())).willReturn(employeeSearchResponseDto);

        //when
        ResultActions result = mockMvc.perform(get("/api/v1/employees/{employeeId}", 10001L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        ///then
        result.andExpectAll(
                status().isOk(),
                jsonPath("$.employeeId").value("10001"),
                jsonPath("$.birthDate").value("1999-01-01"),
                jsonPath("$.firstName").value("firstName"),
                jsonPath("$.lastName").value("lastName"),
                jsonPath("$.gender").value("M"),
                jsonPath("$.hireDate").value("2022-08-21"));
    }

}