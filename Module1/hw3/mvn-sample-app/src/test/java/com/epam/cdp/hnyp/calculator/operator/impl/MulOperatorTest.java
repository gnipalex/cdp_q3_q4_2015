package com.epam.cdp.hnyp.calculator.operator.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MulOperatorTest {

    private MulOperator mulOperator = new MulOperator();

    @Test
    public void shouldMultiplicateValues() {
        int a = 5, b = 3;
        double result = mulOperator.operation(a, b);
        assertTrue(a * b == result);
    }

}
