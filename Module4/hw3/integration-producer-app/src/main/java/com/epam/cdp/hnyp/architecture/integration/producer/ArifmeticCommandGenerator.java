package com.epam.cdp.hnyp.architecture.integration.producer;

import java.util.List;
import java.util.Random;

public class ArifmeticCommandGenerator {

    private static final int MAX_GENERATED_INT = 1000;
    
    private List<String> supportedOperators;
    
    private Random randomGenerator = new Random();
    
    public ArifmeticCommandGenerator(List<String> supportedOperators) {
        if (supportedOperators == null || supportedOperators.isEmpty()) {
            throw new IllegalArgumentException("list of supported operators should not be empty");
        }
        this.supportedOperators = supportedOperators;
    }
    
    public ArifmeticCommand generate() {
        int operatorIndex = randomGenerator.nextInt(supportedOperators.size());
        ArifmeticCommand command = new ArifmeticCommand();
        command.setOperation(supportedOperators.get(operatorIndex));
        command.setInt1(randomGenerator.nextInt(MAX_GENERATED_INT));
        command.setInt2(randomGenerator.nextInt(MAX_GENERATED_INT));
        return command;
    }    
    
}
