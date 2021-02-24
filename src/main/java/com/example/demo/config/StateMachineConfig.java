package com.example.demo.config;

import com.example.demo.domain.EmployeeEvents;
import com.example.demo.domain.EmployeeState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@EnableStateMachineFactory
@Slf4j
public class StateMachineConfig extends StateMachineConfigurerAdapter<EmployeeState, EmployeeEvents> {


    /**
     * Initial state
     * @param states
     * @throws Exception
     */
    @Override
    public void configure(StateMachineStateConfigurer<EmployeeState, EmployeeEvents> states) throws Exception {
        states.withStates().initial(EmployeeState.ADDED)
                .states(EnumSet.allOf(EmployeeState.class))
                .end(EmployeeState.ACTIVE);
    }

    /**
     *  Transition configure ,in short RULES for Transition change
     * @param transitions
     * @throws Exception
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeState, EmployeeEvents> transitions) throws Exception {
//.withExternal().source(EmployeeState.NOT_ADDED).target(EmployeeState.ADDED).event(EmployeeEvents.ADD)
//                .and()
        transitions.withExternal().source(EmployeeState.ADDED).target(EmployeeState.IN_CHECK).event(EmployeeEvents.CHECK_IN)
                .and().withExternal().source(EmployeeState.IN_CHECK).target(EmployeeState.ACTIVE).event(EmployeeEvents.ACTIVATE);

    }

    /**
     * Log congurer for MAchien config ,can be anything ,kafkaevent ,Notofication event
     * @param config
     * @throws Exception
     */
    @Override
    public void configure(StateMachineConfigurationConfigurer<EmployeeState, EmployeeEvents> config) throws Exception {
        StateMachineListenerAdapter<EmployeeState,EmployeeEvents> listenerAdapter = new StateMachineListenerAdapter<EmployeeState, EmployeeEvents>(){
            @Override
            public void stateChanged(State<EmployeeState, EmployeeEvents> from, State<EmployeeState, EmployeeEvents> to) {
              log.info(String.format("state changed from %s to %s",from ,to));
            }
        };
        config.withConfiguration().listener(listenerAdapter);
    }
}
