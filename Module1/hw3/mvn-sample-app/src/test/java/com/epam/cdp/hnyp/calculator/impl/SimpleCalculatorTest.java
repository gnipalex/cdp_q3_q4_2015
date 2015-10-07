package com.epam.cdp.hnyp.calculator.impl;

import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.epam.cdp.hnyp.calculator.operator.Operator;
import com.epam.cdp.hnyp.calculator.operator.impl.SumOperator;


public class SimpleCalculatorTest {

    private SimpleCalculator simpleCalculator;

    @Before
    public void before() {
        Map<String, Operator> operators = new HashMap<>();
        operators.put("+", new SumOperator());
        simpleCalculator = new SimpleCalculator(operators);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_whenCreatingObjectWithEmptyOperators() {
        new SimpleCalculator(Collections.EMPTY_MAP);
    }

    @Test
    public void shouldSupportPlusOperation_whenInitializedWithPlusOperator() {
        assertTrue(simpleCalculator.supports("+"));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowException_whenEvalWithUnsupportedOperation() {
        simpleCalculator.eval(2, 2, "-");
    }
    
    @Test
    public void shouldReturnSum_WhenEvalWithSumOperation() {
        double result = simpleCalculator.eval(1, 5, "+");
        assertTrue(result == 1 + 5);
    }

}
