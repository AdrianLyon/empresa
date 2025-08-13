package com.example.empresa.employees.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.empresa.employees.services.EmployeeService;
import com.example.empresa.employees.models.Employee;
import com.example.empresa.employees.models.dtos.EmployeeReportDTO;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAll();
        return ResponseEntity.ok(employees);
    }
    @GetMapping("/report")
    public ResponseEntity<Map<String, Object>> getReport() {
        Map<String, Object> report = employeeService.report();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reportDTO")
    public ResponseEntity<EmployeeReportDTO> getReportDTO() {
        EmployeeReportDTO report = employeeService.reportDTO();
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reportWithoutDTO")
    public ResponseEntity<Map<String, Object>> getReportWithoutDTO() {
        Map<String, Object> report = employeeService.reportWithoutDTO();
        return ResponseEntity.ok(report);
    }
}
