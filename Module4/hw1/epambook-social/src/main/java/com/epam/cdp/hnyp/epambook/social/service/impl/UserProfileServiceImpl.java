package com.epam.cdp.hnyp.epambook.social.service.impl;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;

import com.epam.cdp.hnyp.epambook.social.dao.UserProfileDao;
import com.epam.cdp.hnyp.epambook.social.model.UserProfile;
import com.epam.cdp.hnyp.epambook.social.service.UserProfileService;

public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileDao userProfileDao;
    
    @Override
    public UserProfile create(UserProfile profile) {
        assertProfileHasUniqueUserName(profile);
        return userProfileDao.create(profile);
    }
    
    private void assertProfileHasUniqueUserName(UserProfile profile) {
        String userName = profile.getUserName();
        if (userProfileDao.getByUserName(userName) != null) {
            String errorMessage = MessageFormat.format("profile with userName {0} already exists", userName);
            throw new IllegalStateException(errorMessage);
        }
    }

    @Override
    public UserProfile getByUserName(String userName) {
        return userProfileDao.getByUserName(userName);
    }

    @Override
    public UserProfile getExistingUserProfile(String userName) {
        UserProfile profile = getByUserName(userName); 
        assertProfileExists(profile, userName);
        return profile;
    }
    
    private void assertProfileExists(UserProfile profile, String userName) {
        if (profile == null) {
            String errorMessage = MessageFormat.format("profile {0} does not exist", userName);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    public UserProfile getById(long id) {
        return userProfileDao.getById(id);
    }

}
