package com.example.demo.service;

import com.example.demo.domain.Employee;
import com.example.demo.domain.EmployeeEvents;
import com.example.demo.domain.EmployeeState;
import com.example.demo.repo.EmployeeRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private final StateMachineFactory<EmployeeState,EmployeeEvents> stateMachineFactory;
    private final EmployeeStateInterceptor employeeStateInterceptor;

    /**
     *   Added state here as it is initial sate
     * @param employee
     * @return
     */
    @Override
    public Employee addEmployee(Employee employee) {
        employee.setEmployeeState(EmployeeState.ADDED);
        return employeeRepo.save(employee);
    }

    @Override
    @Transactional
    public StateMachine<EmployeeState, EmployeeEvents> checkInEmployee(UUID employeeId) {
        StateMachine<EmployeeState, EmployeeEvents> stateMachine = resetStateMachineToDbState(employeeId);
        sendEvent(employeeId,stateMachine,EmployeeEvents.CHECK_IN);
        return null;
    }

    /**
     * Send event uses MessageBjildeer coz we have to save the state in DB using interceprot for which we need unique id
     * When sent as an event ,we dont have have id,so use the overloaded message with Message and not event
     * @param empoyeeId
     * @param stateMachine
     * @param employeeEvent
     */
    private void sendEvent(UUID empoyeeId,StateMachine<EmployeeState, EmployeeEvents> stateMachine,EmployeeEvents employeeEvent){
           Message message = MessageBuilder.withPayload(employeeEvent)
                   .setHeader("EMPLOYEE_ID",empoyeeId).build();
           stateMachine.sendEvent(message);
    }

    /**
     * USed to  set the machine from the DB state
     * @param employeeId
     * @return
     */
    private StateMachine<EmployeeState,EmployeeEvents> resetStateMachineToDbState(UUID employeeId) {
        Employee employee = employeeRepo.getOne(employeeId);

        StateMachine<EmployeeState,EmployeeEvents> stateMachine = stateMachineFactory.getStateMachine(employee.getId());

        /**
         *   To make sure it is in stopped tate for this instant
          */

        stateMachine.stop();

        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(employeeStateEmployeeEventsStateMachineAccess -> {

                    employeeStateEmployeeEventsStateMachineAccess.addStateMachineInterceptor(employeeStateInterceptor);
                    employeeStateEmployeeEventsStateMachineAccess
                            .resetStateMachine
                                    (new DefaultStateMachineContext<>(employee.getEmployeeState(), null, null, null));
                });
           stateMachine.start();
        return stateMachine;
    }
}
