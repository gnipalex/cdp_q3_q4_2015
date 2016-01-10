package com.epam.cdp.hnyp.epambook.social.dao.impl;

import com.epam.cdp.hnyp.epambook.social.dao.UserProfileDao;
import com.epam.cdp.hnyp.epambook.social.model.UserProfile;

public class UserProfileDaoImpl extends AbstractUserProfileDaoImpl implements UserProfileDao {
    
    private static final String SELECT_BY_ID = "SELECT * FROM userProfile WHERE id=?";
    private static final String SELECT_BY_USER_NAME = "SELECT * FROM userProfile WHERE userName=?";
    private static final String INSERT = "INSERT INTO userProfile(userName,name,dateOfBirth) VALUE(?,?,?)";
    
    @Override
    public UserProfile getById(long id) {
        return queryForObject(SELECT_BY_ID, id);
    }

    @Override
    public UserProfile getByUserName(String userName) {
        return queryForObject(SELECT_BY_USER_NAME, userName);
    }

    @Override
    public UserProfile create(UserProfile profile) {
        Object[] args = {profile.getUserName(), profile.getName(), profile.getDateOfBirth()};
        Number key = updateAndGetKey(INSERT, args);
        profile.setId(key.longValue());
        return profile;
    } 

}
