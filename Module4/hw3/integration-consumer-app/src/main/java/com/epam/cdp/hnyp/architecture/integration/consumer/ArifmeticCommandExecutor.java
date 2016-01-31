package com.epam.cdp.hnyp.architecture.integration.consumer;

import org.springframework.beans.factory.annotation.Autowired;

import com.epam.cdp.hnyp.architecture.integration.producer.ArifmeticCommand;
import com.epam.cdp.hnyp.calculator.Calculator;

public class ArifmeticCommandExecutor {

    @Autowired
    private Calculator calculator;
    
    public String executeCommand(ArifmeticCommand command) {
        String operation = command.getOperation();
        int leftArgument = command.getInt1();
        int rightArgument = command.getInt2();
        double result = calculator.eval(leftArgument, rightArgument, operation);
        return command.toString() + " = " + result;
    }
    
}
