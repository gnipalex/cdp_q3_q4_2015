package com.epam.cdp.hnyp.storage;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.mockito.cglib.core.ReflectUtils;

import com.epam.cdp.hnyp.storage.value.block.impl.FileMappingBlockStorage;
import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        //testGson();
        //testGsonWithClassesAB();
        //testBlockStorage();
        testClassTypeSerialize();
    }
    
    public static void testBlockStorage() {
        try {
            final int blockSz = 50;
            FileMappingBlockStorage storage = new FileMappingBlockStorage(new File("test_storage"), blockSz);
            String str = "this is text";
//            storage.writeBlock(str.getBytes(), 1);
            byte[] blockData = storage.readBlock(1);
            String readedString = new String(blockData);
            System.out.println(readedString);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
        b.setListOfA(Arrays.asList(a, new A("new a", 99)));
        String jsonB = gson.toJson(b);
        System.out.println("encoded B : " + jsonB);
        B aFromJsonB = gson.fromJson(jsonB, B.class);
    }
    
    public static void testClassTypeSerialize() {
        Gson gson = new Gson();
        Class<?> clazz = A.class;
//        String json = gson.toJson(clazz);
        System.out.println(clazz.getCanonicalName());
        System.out.println(clazz.getName());
        System.out.println(clazz.getSimpleName());
        System.out.println(clazz.getPackage());
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
    
    private List<A> listOfA;
    
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

    public List<A> getListOfA() {
        return listOfA;
    }
    public void setListOfA(List<A> listOfA) {
        this.listOfA = listOfA;
    }
}
