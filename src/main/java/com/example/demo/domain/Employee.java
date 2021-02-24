package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private LocalDate doj;
    private String country;
    @Enumerated(EnumType.STRING)
    private  EmployeeState employeeState;
}
