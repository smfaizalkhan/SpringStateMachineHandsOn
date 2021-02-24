package com.example.demo.service;

import com.example.demo.domain.Employee;
import com.example.demo.repo.EmployeeRepo;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class EmployeeServiceImplTest {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepo employeeRepo;

    private  Employee employee;

    @BeforeEach
    public void setUp(){
         employee = Employee.builder().country("IND").doj(LocalDate.now()).name("TestEmployee").build();
    }



    @Test
    @Transactional
    void checkInEmployee() {
        Employee dbSavedEmployee = employeeService.addEmployee(employee);

        System.out.println("dbSavedEmployee state "+dbSavedEmployee);
        employeeService.checkInEmployee(dbSavedEmployee.getId());

        Employee checkInEmployee = employeeRepo.getOne(dbSavedEmployee.getId());

        System.out.println("checkInEmployee state"+checkInEmployee);


    }
}