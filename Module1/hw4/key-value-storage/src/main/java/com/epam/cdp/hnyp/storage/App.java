package com.epam.cdp.hnyp.storage;

import java.util.Date;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        //testGson();
        testGsonWithClassesAB();
    }
    
    public static void testGson() {
        Gson gson = new Gson();
        String json = gson.toJson(new Date());
        System.out.println(json);
        Date parsed = gson.fromJson(json, Date.class);
        System.out.println(parsed);
    }
    
    public static void testGsonWithClassesAB() {
        Gson gson = new Gson();
        A a = new A("this is a", 15);
        B b = new B("this is b", 12, new Date());
        String jsonA = gson.toJson(a);
        String jsonB = gson.toJson(b);
        System.out.println("encoded A : " + jsonA);
        System.out.println("encoded B : " + jsonB);
        A aFromJsonB = gson.fromJson(jsonB, A.class);
        Object bFromJsonA = gson.fromJson(jsonA, Object.class);
    }
}

class A {
    private String text;
    private int age;
    
    public A() {
    }
    
    public A(String text, int age) {
        super();
        this.text = text;
        this.age = age;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
}

class B {
    private String text;
    private int age;
    private Date time;
    
    public B() {
    }
    
    public B(String text, int age, Date time) {
        super();
        this.text = text;
        this.age = age;
        this.time = time;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    
    
}
