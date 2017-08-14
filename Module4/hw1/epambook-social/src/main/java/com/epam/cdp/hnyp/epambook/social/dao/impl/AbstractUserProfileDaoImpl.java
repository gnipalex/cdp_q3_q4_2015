package com.epam.cdp.hnyp.epambook.social.dao.impl;

import org.springframework.jdbc.core.RowMapper;

import com.epam.cdp.hnyp.epambook.social.model.UserProfile;

public abstract class AbstractUserProfileDaoImpl extends AbstractJdbcDao<UserProfile>  {

    protected UserProfile createEntity() {
        return new UserProfile();
    }

    @Override
    protected RowMapper<UserProfile> getRowMapper() {
        return (rs, i) -> {
            UserProfile userProfile = createEntity();
            userProfile.setId(rs.getLong("id"));
            userProfile.setName(rs.getString("name"));
            userProfile.setUserName(rs.getString("userName"));
            userProfile.setDateOfBirth(rs.getDate("dateOfBirth"));
            return userProfile;
        };
    }
    
}
