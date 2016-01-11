package com.epam.cdp.hnyp.epambook.social.service;

import java.util.List;

import com.epam.cdp.hnyp.epambook.social.model.UserProfile;

public interface FriendshipService {
    
    /**
     * Creates friendship between two userProfiles
     * @param userName
     * @param otherUserName
     * @throws IllegalStateException if friendship already exists
     * @throws IllegalArgumentException if any of userProfile doesn't exist
     */
    void create(String userName, String otherUserName);
    
    /**
     * Gets list of user's friends
     * @param userName of userProfile friends is being looking for
     * @return list of UserProfile's
     * @throws IllegalArgumentException if user profile for userId doesn't exist
     */
    List<UserProfile> getFriends(String userName);
    
    /**
     * Gets profile of friend. Supposed that friendship exists
     * @param userName 
     * @param friendName
     * @return user profile
     * @throws IllegalStateException if friendship doesn't exist
     * @throws IllegalArgumentException if any of specified profiles not found
     */
    UserProfile getFriendProfile(String userName, String friendName);
    
}
