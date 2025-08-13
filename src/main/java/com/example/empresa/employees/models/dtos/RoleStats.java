package com.example.empresa.employees.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleStats {
    private String role;
    private double sum;
    private double avg;
    private long count;
}
