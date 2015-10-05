package com.epam.cdp.hnyp;

import com.epam.cdp.hnyp.calculator.factory.CalculatorFactory;
import com.epam.cdp.hnyp.calculator.factory.impl.SimpleCalculatorFactory;

public class App {

    private static CalculatorFactory calcFactory = new SimpleCalculatorFactory();
    
    public static void main(String[] args) {
        CalculatorLauncher launcher = new CalculatorLauncher(calcFactory.createCalculator());
        launcher.run(args);
    }
    
    
}
