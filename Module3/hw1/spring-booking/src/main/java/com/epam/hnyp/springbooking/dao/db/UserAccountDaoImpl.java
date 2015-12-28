package com.epam.hnyp.springbooking.dao.db;

import javax.annotation.Resource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.epam.hnyp.springbooking.dao.UserAccountDao;
import com.epam.hnyp.springbooking.model.UserAccount;

@Repository("userAccountDao")
public class UserAccountDaoImpl extends AbstractJdbcDao<UserAccount> implements UserAccountDao {

    private static final String SELECT_BY_USER_ID = "SELECT * FROM `userAccount` as ua WHERE ua.userId=?";
    private static final String INSERT_ACCOUNT = "INSERT INTO `userAccount`(userId,prepaidAmount) VALUE (?,?)";
    private static final String UPDATE_BY_ID = "UPDATE `userAccount` SET prepaidAmount=? WHERE userId=?";
    private static final String DELETE_BY_ID = "DELETE FROM `userAccount` WHERE userId=?";

    @Resource
    private RowMapper<UserAccount> userAccountRowMapper;
    
    @Override
    public UserAccount getByUserId(long userId) {
        return queryForObject(SELECT_BY_USER_ID, userId);
    }

    @Override
    public UserAccount create(UserAccount userAccount) {
        updateRow(INSERT_ACCOUNT, userAccount.getUserId(), userAccount.getPrepaidAmount());
        return userAccount;
    }

    @Override
    public void update(UserAccount userAccount) {
        Object[] args = { userAccount.getPrepaidAmount(), userAccount.getUserId() };
        updateRow(UPDATE_BY_ID, args);
    }

    @Override
    public boolean delete(long id) {
        return getJdbcTemplate().update(DELETE_BY_ID, id) > 0;
    }

    @Override
    protected RowMapper<UserAccount> getRowMapper() {
        return userAccountRowMapper;
    }
    
}
