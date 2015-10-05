package com.epam.cdp.hnyp.calculator;

public interface Calculator {
    
    double eval(int a, int b, String operation);
    
    boolean supports(String operation);
    
}
