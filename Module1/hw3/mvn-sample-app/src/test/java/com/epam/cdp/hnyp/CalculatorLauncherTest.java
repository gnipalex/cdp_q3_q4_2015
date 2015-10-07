package com.epam.cdp.hnyp;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.experimental.categories.Category;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.epam.cdp.hnyp.calculator.impl.SimpleCalculator;
import com.epam.cdp.hnyp.calculator.operator.Operator;
import com.epam.cdp.hnyp.calculator.testgroup.CalculatorTestGroup;

public class CalculatorLauncherTest {
    
    private CalculatorLauncher launcher;
    
    private PrintStreamWrapper out;
    private PrintStreamWrapper err;
    
    
    @BeforeClass
    public void beforeAll() {
        Map<String, Operator> operators = new HashMap<>();
        operators.put("+", (a, b) -> a + b);
        SimpleCalculator calculator = new SimpleCalculator(operators);
        launcher = new CalculatorLauncher(calculator);
    }
    
    @BeforeMethod
    public void before() {
        out = new PrintStreamWrapper();
        err = new PrintStreamWrapper();
        launcher.setErr(err);
        launcher.setOut(out);
    }
    
    @Test(groups = {"paramGroup"})
    public void shouldShowSample_whenLessThanThreeParams() {
        launcher.run(new String[] {"1", "2"});
        assertFalse(out.getLines().isEmpty());
        assertTrue(err.getLines().isEmpty());
        assertEquals(out.getLines().get(0), "2 + 2 = 4");
    }
    
    @Test(groups = {"paramGroup"})
    public void shouldShowErrorMessage_whenArgumentIncorrect() {
        launcher.run(new String[] {"aa", "2", "+"});
        assertTrue(out.getLines().isEmpty());
        assertFalse(err.getLines().isEmpty());
        assertEquals(err.getLines().get(0), "aa format error");
    }
    
    @Test(groups = {"paramGroup"})
    public void shouldShowMessage_whenOperationNotSupported() {
        launcher.run(new String[] {"2", "2", "AA"});
        assertFalse(out.getLines().isEmpty());
        assertTrue(err.getLines().isEmpty());
        assertEquals(out.getLines().get(0), "AA not supported operation");
    }
    
    @Test(groups = {"calculationGroup"})
    public void shouldShowResult_whenInputIsCorrect() {
        launcher.run(new String[] {"7", "2", "+"});
        assertFalse(out.getLines().isEmpty());
        assertTrue(err.getLines().isEmpty());
        assertEquals(out.getLines().get(0), "7 + 2 = 9");
    }
    
    @Test(groups = {"calculationGroup"})
    public void shouldShowResult_IfPassedMoreThanThreeArgsAndFirstArgsAreCorrect () {
        launcher.run(new String[] {"7", "2", "+", "100"});
        assertFalse(out.getLines().isEmpty());
        assertTrue(err.getLines().isEmpty());
        assertEquals(out.getLines().get(0), "7 + 2 = 9");
    }    
    
    private static class PrintStreamWrapper extends PrintStream {
        
        private List<String> lines = new ArrayList<>();
        
        public PrintStreamWrapper() {
            super(new ByteArrayOutputStream(0));
        }
        
        @Override
        public void println(String x) {
            lines.add(x);
        }
        
        public List<String> getLines() {
            return lines;
        } 
    }
}
