package com.epam.cdp.hnyp.calculator.operator.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.epam.cdp.hnyp.calculator.testgroup.OperatorTestGroup;

@Category(OperatorTestGroup.class)
public class SumOperatorTest {

    private SumOperator sumOperator = new SumOperator();
    
    @Test
    public void shouldSumValues() {
        int a = 5, b = 1;
        double result = sumOperator.operation(a, b);
        assertTrue(a + b == result);
    }

}
