package com.epam.hnyp.springbooking.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.epam.hnyp.springbooking.dao.UserDao;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.service.UserService;

public class UserServiceImpl implements UserService {

    @Autowired
	private UserDao userDao;
	
	@Override
	public User getUserById(long userId) {
		return userDao.getById(userId);
	}

	@Override
	public User getUserByEmail(String email) {
		return userDao.getByEmail(email);
	}

	@Override
	public List<User> getUsersByName(String name, int pageSize, int pageNum) {
		return userDao.getAllByName(name, pageSize, pageNum);
	}

	@Override
	public User createUser(User user) {
		return userDao.create(user);
	}

	@Override
	public void updateUser(User user) {
		userDao.update(user);
	}

	@Override
	public boolean deleteUser(long userId) {
		return userDao.delete(userId);
	}

}
