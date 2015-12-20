package com.epam.hnyp.springbooking.dao.db;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.epam.hnyp.springbooking.dao.UserAccountDao;
import com.epam.hnyp.springbooking.model.UserAccount;

@Repository("userAccountDao")
public class UserAccountDaoImpl extends AbstractJdbcDao implements UserAccountDao {

    private static final String SELECT_BY_USER_ID = "SELECT * FROM `userAccount` as ua WHERE ua.userId=?";
    private static final String INSERT_ACCOUNT = "INSERT INTO `userAccount`(userId,prepaidAmount) VALUE (?,?)";
    private static final String UPDATE_BY_ID = "UPDATE `userAccount` SET prepaidAmount=? WHERE userId=?";
    private static final String DELETE_BY_ID = "DELETE FROM `userAccount` AS ua WHERE ua.userId=?";

    @Override
    public UserAccount getByUserId(long userId) {
        return getJdbcTemplate().queryForObject(SELECT_BY_USER_ID, getAccountMapper(), userId);
    }
    
    private RowMapper<UserAccount> getAccountMapper() {
        return (rs, rowNumber) -> {
            UserAccount account = createUserAccountInstance();
            account.setUserId(rs.getLong("userId"));
            account.setPrepaidAmount(rs.getBigDecimal("prepaidAmount"));
            return account;
        };
    }
    
    @Lookup
    protected UserAccount createUserAccountInstance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserAccount create(UserAccount userAccount) {
        if (getJdbcTemplate().update(INSERT_ACCOUNT, 
                userAccount.getUserId(), userAccount.getPrepaidAmount()) > 0) {
            return userAccount;
        }                
        return null;
    }

    @Override
    public UserAccount update(UserAccount userAccount) {
        Object[] args = { userAccount.getPrepaidAmount(), userAccount.getUserId() };
        if (getJdbcTemplate().update(UPDATE_BY_ID, args) > 0) {
            return userAccount;
        }
        return null;
    }

    @Override
    public boolean delete(long id) {
        return getJdbcTemplate().update(DELETE_BY_ID, id) > 0;
    }
    
}
