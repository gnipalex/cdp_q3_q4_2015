package com.epam.cdp.hnyp.calculator.operator.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SumOperatorTest {

    private SumOperator sumOperator = new SumOperator();
    
    @Test
    public void shouldSumValues() {
        int a = 5, b = 1;
        double result = sumOperator.operation(a, b);
        assertTrue(a + b == result);
    }

}
