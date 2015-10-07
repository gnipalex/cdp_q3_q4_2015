package com.epam.cdp.hnyp.calculator.operator.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SubOperatorTest {

    private SubOperator subOperator = new SubOperator();

    @Test
    public void shouldSubtractValues() {
        int a = 5, b = 1;
        double result = subOperator.operation(a, b);
        assertTrue(a - b == result);
    }

}
