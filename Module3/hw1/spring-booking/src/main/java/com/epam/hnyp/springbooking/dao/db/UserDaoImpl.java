package com.epam.hnyp.springbooking.dao.db;

import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.epam.hnyp.springbooking.dao.UserDao;
import com.epam.hnyp.springbooking.model.User;

@Repository("userDao")
public class UserDaoImpl extends AbstractJdbcDao implements UserDao {
    
    private static final String SELECT_BY_ID = "SELECT * FROM `user` AS u WHERE u.id=?";
    private static final String SELECT_BY_EMAIL = "SELECT * FROM `user` AS u WHERE u.email=?";
    private static final String SELECT_BY_NAME = "SELECT * FROM `user` AS u WHERE u.name LIKE ? LIMIT ?,?";
    private static final String INSERT_USER = "INSERT INTO `user`(name,email) VALUES (?,?)";
    private static final String UPDATE_BY_ID = "UPDATE `user` SET name=?, email=? WHERE id=?";
    private static final String DELETE_BY_ID = "DELETE FROM `user` AS u WHERE u.id=?";
    
    @Override
    public User getById(long userId) {
        return getJdbcTemplate().queryForObject(SELECT_BY_ID, getUserMapper(), userId);
    }
    
    private RowMapper<User> getUserMapper() {
        return (rs, rowNumber) -> {
            User user = createUserInstance();
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setName(rs.getString("name"));
            return user;
        };
    }
    
    protected User createUserInstance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public User getByEmail(String email) {
        return getJdbcTemplate().queryForObject(SELECT_BY_EMAIL, getUserMapper(), email);
    }

    @Override
    public List<User> getAllByName(String name, int pageSize, int pageNum) {
        Object[] args = { name, getOffset(pageSize, pageNum), pageSize };
        return getJdbcTemplate().query(SELECT_BY_NAME, getUserMapper(), args);
    }

    @Override
    public User create(User user) {
        Object[] args = { user.getName(), user.getEmail() };
        Number key = updateAndGetKey(INSERT_USER, args);
        user.setId(key.longValue());
        return user;
    }

    @Override
    public User update(User user) {
        Object[] args = { user.getName(), user.getEmail(), user.getId() };
        if (getJdbcTemplate().update(UPDATE_BY_ID, args) > 1) {
            return user;
        }
        return null;
    }

    @Override
    public boolean delete(long userId) {
        return getJdbcTemplate().update(DELETE_BY_ID, userId) > 0;
    }

}
