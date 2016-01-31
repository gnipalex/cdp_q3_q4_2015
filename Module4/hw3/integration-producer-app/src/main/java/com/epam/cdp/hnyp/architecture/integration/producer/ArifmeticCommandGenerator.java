package com.epam.cdp.hnyp.architecture.integration.producer;

import java.util.List;
import java.util.Random;

public class ArifmeticCommandGenerator {

    private List<String> supportedOperators;
    
    public ArifmeticCommandGenerator(List<String> supportedOperators) {
        if (supportedOperators == null || supportedOperators.isEmpty()) {
            throw new IllegalArgumentException("list of supported operators should not be empty");
        }
        this.supportedOperators = supportedOperators;
    }
    
    public ArifmeticCommand generate() {
        Random rand = new Random();
        int operatorIndex = rand.nextInt(supportedOperators.size());
        ArifmeticCommand command = new ArifmeticCommand();
        command.setOperation(supportedOperators.get(operatorIndex));
        command.setInt1(rand.nextInt());
        command.setInt2(rand.nextInt());
        return command;
    }
    
}
