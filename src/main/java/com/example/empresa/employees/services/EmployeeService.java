package com.example.empresa.employees.services;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.empresa.employees.models.Employee;
import com.example.empresa.employees.models.dtos.EmployeeReportDTO;
import com.example.empresa.employees.models.dtos.RoleStats;
import com.example.empresa.employees.repositories.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Map<String, Object> report() {
        long start = System.nanoTime();
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

        long end = System.nanoTime(); // Fin
        System.out.println("Tiempo de ejecución sin refactor: " + (end - start) / 1_000_000 + " ms");
        return result;
    }

    public EmployeeReportDTO reportDTO() {

        long start = System.nanoTime();
        List<Employee> employees = employeeRepository.findAll();
        
        Map<String, DoubleSummaryStatistics> stats = employees.stream()
            .filter(Employee::isActive)
            .collect(Collectors.groupingBy(
                Employee::getRole,
                Collectors.summarizingDouble(Employee::getSalary)
            ));
        
        List<RoleStats> roleStats = stats.entrySet().stream()
            .map(entry -> new RoleStats(
                entry.getKey(),
                entry.getValue().getSum(),
                entry.getValue().getAverage(),
                entry.getValue().getCount()
            )).toList();

             List<Employee> top3 = employees.stream()
                .sorted((a, b) -> Double.compare(b.getSalary(), a.getSalary()))
                .limit(3)
                .toList();
        long end = System.nanoTime(); // Fin
        System.out.println("Tiempo de ejecución con dto: " + (end - start) / 1_000_000 + " ms");
        return new EmployeeReportDTO(roleStats, top3);
    }

    public Map<String, Object> reportWithoutDTO() {
        long start = System.nanoTime();
        List<Employee> employees = employeeRepository.findAll();

        Map<String, DoubleSummaryStatistics> stats = employees.stream()
            .filter(Employee::isActive)
            .collect(Collectors.groupingBy(
                Employee::getRole,
                Collectors.summarizingDouble(Employee::getSalary)
            ));
        Map<String, Double> sumByRole = new HashMap<>();
        Map<String, Double> avgByRole = new HashMap<>();

        stats.forEach((role, summary) -> {
            sumByRole.put(role, summary.getSum());
            avgByRole.put(role, summary.getAverage());
        });

        // Top 3 empleados
        List<Employee> top3 = employees.stream()
        .sorted((a, b) -> Double.compare(b.getSalary(), a.getSalary()))
        .limit(3)
        .toList();

        // Resultado final en Map como tenías antes
        Map<String, Object> result = new HashMap<>();
        result.put("sumByRole", sumByRole);
        result.put("avgByRole", avgByRole);
        result.put("top3", top3);
        long end = System.nanoTime(); // Fin
        System.out.println("Tiempo de ejecución sin dto: " + (end - start) / 1_000_000 + " ms");
        return result;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }
}
