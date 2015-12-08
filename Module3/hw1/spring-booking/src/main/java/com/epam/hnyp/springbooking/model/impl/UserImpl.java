package com.epam.hnyp.springbooking.model.impl;

import java.text.MessageFormat;

import com.epam.hnyp.springbooking.model.User;

public class UserImpl implements User {

    private long id;
    private String name;
    private String email;
    
    @Override
	public long getId() {
        return id;
    }

    @Override
	public void setId(long id) {
        this.id = id;        
    }

    @Override
	public String getName() {
        return name;
    }

    @Override
	public void setName(String name) {
        this.name = name;
    }

    @Override
	public String getEmail() {
        return email;
    }

    @Override
	public void setEmail(String email) {
    	this.email = email;
    }
    
    @Override
    public String toString() {
    	return MessageFormat.format("User[id={0}; name={1}; email={2}]", id, name, email);
    }

}
