package com.epam.cdp.hnyp.calculator.factory.impl;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.epam.cdp.hnyp.calculator.Calculator;
import com.epam.cdp.hnyp.calculator.testgroup.AssembleTestGroup;

@Category(AssembleTestGroup.class)
public class SimpleCalculatorFactoryTest {
    
    private SimpleCalculatorFactory factory = new SimpleCalculatorFactory();
    
    @Test
    public void shouldBuildSingleInstanceOfCalculatorForEachFactory_whenCreatingInstance() {
        Calculator instance1 = factory.createCalculator();
        Calculator instance2 = factory.createCalculator();
        
        assertSame(instance1, instance2);
    }
    
    @Test
    public void shouldBuildDifferentInstance_whenNewFactoryIsUsed() {
        Calculator instance = factory.createCalculator();
        SimpleCalculatorFactory otherFactory = new SimpleCalculatorFactory();
        Calculator otherInstance = otherFactory.createCalculator();
        
        assertNotSame(instance, otherInstance);
    }
    
    private void buildAndAssertSupportsOperation(String operation) {
        Calculator instance = factory.createCalculator();
        assertTrue("operation not supported", instance.supports(operation));
    }
    
    @Test
    public void shouldBuildInstanceWithSumSupport_whenCreatingInstance() {
        buildAndAssertSupportsOperation("+");
    }
    
    @Test
    public void shouldBuildInstanceWithSubSupport_whenCreatingInstance() {
        buildAndAssertSupportsOperation("-");
    }
    
    @Test
    public void shouldBuildInstanceWithMulSupport_whenCreatingInstance() {
        buildAndAssertSupportsOperation("*");
    }
    
    @Test
    public void shouldBuildInstanceWithDivSupport_whenCreatingInstance() {
        buildAndAssertSupportsOperation("/");
    }  

}
