package com.epam.cdp.hnyp.calculator.operator.impl;

import com.epam.cdp.hnyp.calculator.operator.Operator;

public class DivOperator implements Operator {

    public double operation(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("div by zero is imposible");
        }
        return (double)a / b;
    }

}
