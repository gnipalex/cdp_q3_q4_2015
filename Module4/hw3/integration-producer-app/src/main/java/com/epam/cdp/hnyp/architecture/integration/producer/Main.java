package com.epam.cdp.hnyp.architecture.integration.producer;

import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    
    private static final String APP_CONTEXT = "app-context.xml";
    
    @SuppressWarnings("all")
    public static void main(String[] args) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext(APP_CONTEXT);
        System.out.println("Application now is being sending generated arifmetic commands to endpoints... Press [enter] to exit");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.println("Application stopped on user demand.");
    }
    
}
