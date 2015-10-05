package com.epam.cdp.hnyp;

import java.io.PrintStream;
import java.text.MessageFormat;

import com.epam.cdp.hnyp.calculator.Calculator;

public class CalculatorLauncher {
    
    private PrintStream out = System.out;
    private PrintStream err = System.err;
    
    private Calculator calculator;
    
    public CalculatorLauncher(Calculator calculator) {
        this.calculator = calculator;
    }
    
    public void run(String[] args) {
        try {
            body(args);
        } catch (Exception e) {
            err.println(e.getMessage());
        }
    }
    
    private void body(String[] args) {
        if (args.length < 3) {
            printResult(2, 2, "+", 4);
            return;
        }
        int arg1 = parseArgumentOrThrow(args[0]);
        int arg2 = parseArgumentOrThrow(args[1]);
        String operation = args[2]; 
        if (!calculator.supports(operation)) {
            out.println(operation + " not supported operation");
            return;
        }
        double result = calculator.eval(arg1, arg2, operation);
        printResult(arg1, arg2, operation, result);
    }
    
    private void printResult(int a, int b, String operation, double result) {
        out.println(MessageFormat.format("{0} {1} {2} = {3}", a, operation, b, result));
    }

    private int parseArgumentOrThrow(String argument) {
        try {
            return Integer.parseInt(argument);
        } catch (NumberFormatException e) {
            throw new RuntimeException(argument + " format error", e);
        }
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }
    public void setErr(PrintStream err) {
        this.err = err;
    }
    public void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }
   
}
