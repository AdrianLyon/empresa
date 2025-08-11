package com.example.empresa.employees.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.empresa.employees.models.Employee;
import com.example.empresa.employees.repositories.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Map<String, Object> report() {
        List<Employee> employees = employeeRepository.findAll();
        // MUCHA iteración repetida, mapas stringly-typed y sin DTOs
        Map<String, Double> sumByRole = new HashMap<>();
        Map<String, Integer> countByRole = new HashMap<>();

        for (Employee e : employees) {
            if (e.isActive()) {
                if (!sumByRole.containsKey(e.getRole())) {
                    sumByRole.put(e.getRole(), 0.0);
                }
                if (!countByRole.containsKey(e.getRole())) {
                    countByRole.put(e.getRole(), 0);
                }
                sumByRole.put(e.getRole(), sumByRole.get(e.getRole()) + e.getSalary());
                countByRole.put(e.getRole(), countByRole.get(e.getRole()) + 1);
            }
        }

        Map<String, Double> avgByRole = new HashMap<>();
        for (String role : sumByRole.keySet()) {
            avgByRole.put(role, sumByRole.get(role) / countByRole.get(role));
        }

        List<Employee> sorted = new ArrayList<>(employees);
        sorted.sort((a, b) -> Double.compare(b.getSalary(), a.getSalary()));

        List<Employee> top3 = new ArrayList<>();
        for (int i = 0; i < sorted.size() && i < 3; i++) {
            top3.add(sorted.get(i));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("sumByRole", sumByRole);
        result.put("avgByRole", avgByRole);
        result.put("top3", top3);

        return result;
    }
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
}
