package com.epam.hnyp.simplecaching;

import java.text.MessageFormat;

public class Human {
    private String name;
    private int age;
    private boolean male;

    public Human(String name, int age, boolean male) {
        this.name = name;
        this.age = age;
        this.male = male;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public boolean isMale() {
        return male;
    }
    public void setMale(boolean male) {
        this.male = male;
    }
    
    @Override
    public String toString() {
        return MessageFormat.format("[name:{0};age:{1};male:{2}]", name, age, male);
    }
    
}
