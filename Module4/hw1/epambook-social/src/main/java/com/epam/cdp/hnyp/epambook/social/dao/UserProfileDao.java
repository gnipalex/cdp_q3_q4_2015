package com.epam.cdp.hnyp.epambook.social.dao;

import com.epam.cdp.hnyp.epambook.social.model.UserProfile;

public interface UserProfileDao {
    
    UserProfile getById(long id);
    
    UserProfile getByUserName(String userName);
    
    UserProfile create(UserProfile profile);
       
}
