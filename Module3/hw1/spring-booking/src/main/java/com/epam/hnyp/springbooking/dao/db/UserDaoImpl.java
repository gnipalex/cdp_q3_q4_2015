package com.epam.hnyp.springbooking.dao.db;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.epam.hnyp.springbooking.dao.UserDao;
import com.epam.hnyp.springbooking.model.User;

@Repository("userDao")
public class UserDaoImpl extends AbstractJdbcDao<User> implements UserDao {
    
    private static final String SELECT_BY_ID = "SELECT * FROM `user` AS u WHERE u.id=?";
    private static final String SELECT_BY_EMAIL = "SELECT * FROM `user` AS u WHERE u.email=?";
    private static final String SELECT_BY_NAME = "SELECT * FROM `user` AS u WHERE u.name LIKE ? LIMIT ?,?";
    private static final String INSERT_USER = "INSERT INTO `user`(name,email) VALUES (?,?)";
    private static final String UPDATE_BY_ID = "UPDATE `user` SET name=?, email=? WHERE id=?";
    private static final String DELETE_BY_ID = "DELETE FROM `user` WHERE id=?";
    
    @Resource
    private RowMapper<User> userRowMapper;
    
    @Override
    public User getById(long userId) {
        return queryForObject(SELECT_BY_ID, userId);
    }

    @Override
    public User getByEmail(String email) {
        return queryForObject(SELECT_BY_EMAIL, email);
    }

    @Override
    public List<User> getAllByName(String name, int pageSize, int pageNum) {
        Object[] args = { formatName(name), getOffset(pageSize, pageNum), pageSize };
        return queryForList(SELECT_BY_NAME, args);
    }
    
    private String formatName(String name) {
        return "%" + name + "%";
    }

    @Override
    public User create(User user) {
        Object[] args = { user.getName(), user.getEmail() };
        Number key = updateAndGetKey(INSERT_USER, args);
        user.setId(key.longValue());
        return user;
    }

    @Override
    public void update(User user) {
        Object[] args = { user.getName(), user.getEmail(), user.getId() };
        updateRow(UPDATE_BY_ID, args);
    }

    @Override
    public boolean delete(long userId) {
        return getJdbcTemplate().update(DELETE_BY_ID, userId) > 0;
    }

    @Override
    protected RowMapper<User> getRowMapper() {
        return userRowMapper;
    }
    
}
