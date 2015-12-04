package com.epam.hnyp.springbooking.dao;

import java.util.List;

import com.epam.hnyp.springbooking.model.User;

public interface UserDao {

    User getById(long userId);

	User getByEmail(String email);

	List<User> getAllByName(String name, int pageSize, int pageNum);

	User create(User user);

	User update(User user);

	boolean delete(long userId);
	
}
