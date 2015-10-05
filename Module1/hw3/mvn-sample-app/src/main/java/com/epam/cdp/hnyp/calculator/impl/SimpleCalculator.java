package com.epam.cdp.hnyp.calculator.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.epam.cdp.hnyp.calculator.Calculator;
import com.epam.cdp.hnyp.calculator.operator.Operator;

public class SimpleCalculator implements Calculator {

    private static final String UNSUPPORTED_ERROR_MESSAGE = "operation {0} is not supported";
    private static final String OPERATORS_EMPTY_ERROR_MESSGE = "cant create calculator with empty values";
    
    private Map<String, Operator> operators;

    public SimpleCalculator(Map<String, Operator> operators) {
        if (operators == null || operators.isEmpty()) {
            throw new IllegalArgumentException(OPERATORS_EMPTY_ERROR_MESSGE);
        }
        this.operators = new HashMap<>(operators);
    }
    
    public double eval(int a, int b, String operation) {
        if (!supports(operation)) {
            throw new UnsupportedOperationException(MessageFormat.format(UNSUPPORTED_ERROR_MESSAGE, operation));
        }
        return operators.get(operation).operation(a, b);
    }

    public boolean supports(String operation) {
        if (StringUtils.isEmpty(operation)) {
            return false;
        }
        return operators.containsKey(operation);
    }

}
