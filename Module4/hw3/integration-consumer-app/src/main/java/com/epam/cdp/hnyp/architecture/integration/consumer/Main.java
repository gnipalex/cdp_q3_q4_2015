package com.epam.cdp.hnyp.architecture.integration.consumer;

import java.util.Scanner;

import org.apache.camel.CamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Main {
    
    private static final String APP_CONTEXT = "consumer-context.xml";
    
    @SuppressWarnings("all")
    public static void main(String[] args) throws Exception {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(APP_CONTEXT);
        CamelContext camelContext = appContext.getBean("camelContext", CamelContext.class);
        
        System.out.println("Application now is being listening for arifmetic commands to execute... Press [enter] to exit");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        
        camelContext.stop();
        
        System.out.println("Application stopped on user demand.");
    }
}
