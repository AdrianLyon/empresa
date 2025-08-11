package com.example.empresa.employees.models;

import jakarta.persistence.*;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    private Long id;
    private String name;
    private String role;
    private boolean active;  // Hibernate mapea 'boolean' a 'BIT' en SQL Server
    private double salary;

    public Employee() {}

    public Employee(Long id, String name, String role, boolean active, double salary) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.active = active;
        this.salary = salary;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}