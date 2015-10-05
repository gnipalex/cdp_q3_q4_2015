package com.epam.cdp.hnyp.calculator.operator.impl;

import com.epam.cdp.hnyp.calculator.operator.Operator;

public class SumOperator implements Operator {

    @Override
    public double operation(int a, int b) {
        return a + b;
    }

}
