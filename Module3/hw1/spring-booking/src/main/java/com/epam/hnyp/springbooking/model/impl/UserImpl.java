package com.epam.hnyp.springbooking.model.impl;

import java.text.MessageFormat;
import java.util.Objects;

import com.epam.hnyp.springbooking.model.User;

public class UserImpl implements User {

    private long id;
    private String name;
    private String email;
    
    public UserImpl() {
	}

    public UserImpl(String name, String email) {
		this.name = name;
		this.email = email;
	}

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
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        return this == other || Objects.equals(id, other.getId())
                && Objects.equals(email, other.getEmail())
                && Objects.equals(name, other.getName());
    }

}
