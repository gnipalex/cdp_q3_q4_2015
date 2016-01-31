package com.epam.cdp.hnyp.architecture.integration.producer;

import java.text.MessageFormat;

public class ArifmeticCommand {

    private String operation;
    private int int1;
    private int int2;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getInt1() {
        return int1;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    public int getInt2() {
        return int2;
    }

    public void setInt2(int int2) {
        this.int2 = int2;
    }
    
    @Override
    public String toString() {
        return MessageFormat.format("{0} {1} {2}", int1, operation, int2);
    }
    
}
