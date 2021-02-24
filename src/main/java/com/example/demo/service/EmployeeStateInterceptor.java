package com.example.demo.service;

import com.example.demo.domain.Employee;
import com.example.demo.domain.EmployeeEvents;
import com.example.demo.domain.EmployeeState;
import com.example.demo.repo.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmployeeStateInterceptor extends StateMachineInterceptorAdapter<EmployeeState, EmployeeEvents> {

    private final EmployeeRepo employeeRepo;

    @Override
    public void preStateChange(State<EmployeeState, EmployeeEvents> state, Message<EmployeeEvents> message, Transition<EmployeeState, EmployeeEvents> transition, StateMachine<EmployeeState, EmployeeEvents> stateMachine, StateMachine<EmployeeState, EmployeeEvents> rootStateMachine) {

        Optional.ofNullable(message).ifPresent(employeeEventsMessage -> {
           UUID employeeId = UUID.class.cast(employeeEventsMessage.getHeaders().get("EMPLOYEE_ID"));
           Employee employee = employeeRepo.getOne(employeeId);
           employee.setEmployeeState(state.getId());
           employeeRepo.save(employee);
        });
    }
}
