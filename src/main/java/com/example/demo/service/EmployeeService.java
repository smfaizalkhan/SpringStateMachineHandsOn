package com.example.demo.service;

import com.example.demo.domain.Employee;
import com.example.demo.domain.EmployeeEvents;
import com.example.demo.domain.EmployeeState;
import org.springframework.statemachine.StateMachine;

import java.util.UUID;

public interface EmployeeService {

    Employee addEmployee(Employee employee);

    StateMachine<EmployeeState, EmployeeEvents> checkInEmployee(UUID employeeId);
}
