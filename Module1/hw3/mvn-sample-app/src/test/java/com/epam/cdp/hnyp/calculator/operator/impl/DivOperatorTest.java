package com.epam.cdp.hnyp.calculator.operator.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DivOperatorTest {
    
    private DivOperator divOperator = new DivOperator();
    
    @Test
    public void shouldDivideValues() {
        final int a = 9, b = 2;
        double result = divOperator.operation(a, b);
        assertTrue((double)a / b == result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_whenSecondArgumentIsZero() {
        final int a = 10, b = 0;
        divOperator.operation(a, b);
    }

}
