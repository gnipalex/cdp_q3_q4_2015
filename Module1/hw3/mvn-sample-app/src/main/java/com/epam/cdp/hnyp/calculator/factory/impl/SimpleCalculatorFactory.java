package com.epam.cdp.hnyp.calculator.factory.impl;

import java.util.HashMap;
import java.util.Map;

import com.epam.cdp.hnyp.calculator.Calculator;
import com.epam.cdp.hnyp.calculator.factory.CalculatorFactory;
import com.epam.cdp.hnyp.calculator.impl.SimpleCalculator;
import com.epam.cdp.hnyp.calculator.operator.Operator;
import com.epam.cdp.hnyp.calculator.operator.impl.DivOperator;
import com.epam.cdp.hnyp.calculator.operator.impl.MulOperator;
import com.epam.cdp.hnyp.calculator.operator.impl.SubOperator;
import com.epam.cdp.hnyp.calculator.operator.impl.SumOperator;

public class SimpleCalculatorFactory implements CalculatorFactory {

    private Calculator instance;
    
    public SimpleCalculatorFactory() {
        Map<String, Operator> operators = new HashMap<>();
        operators.put("+", new SumOperator());
        operators.put("-", new SubOperator());
        operators.put("*", new MulOperator());
        operators.put("/", new DivOperator());
        instance = new SimpleCalculator(operators);
    }
    
    @Override
    public Calculator createCalculator() {
        return instance;
    }

}
