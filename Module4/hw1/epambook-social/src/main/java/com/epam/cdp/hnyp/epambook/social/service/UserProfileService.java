package com.epam.cdp.hnyp.epambook.social.service;

import com.epam.cdp.hnyp.epambook.social.model.UserProfile;

public interface UserProfileService {
    
    /**
     * Creates new user profile. User should have unique userName
     * @param profile to be created
     * @return created UserProfile with generated id
     * @throws IllegalStateException if user with provided userName already exists
     */
    UserProfile create(UserProfile profile);
    
    /**
     * Gets user profile by its id
     * @param id
     * @return userProfile or null if userProfile not found
     */
    UserProfile getById(long id);
    
    /**
     * Gets userProfile by its userName
     * @param userName
     * @return userProfile or null if userProfile not found
     */
    UserProfile getByUserName(String userName);
    
    /**
     * Gets userProfile by its userName. Expected that profile exists
     * @param userName
     * @return userProfile
     * @throws IllegalArgumentException if profile doesn't exist
     */
    UserProfile getExistingUserProfile(String userName);
}
