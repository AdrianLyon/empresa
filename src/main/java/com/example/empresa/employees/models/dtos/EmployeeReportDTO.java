package com.example.empresa.employees.models.dtos;

import java.util.List;

import com.example.empresa.employees.models.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeReportDTO {
    private List<RoleStats> roleStats;
    private List<Employee> top3HighestSalaries;
}
