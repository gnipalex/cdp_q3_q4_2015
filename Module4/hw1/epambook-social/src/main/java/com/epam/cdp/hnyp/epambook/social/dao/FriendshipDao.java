package com.epam.cdp.hnyp.epambook.social.dao;

import java.util.List;

import com.epam.cdp.hnyp.epambook.social.model.UserProfile;

public interface FriendshipDao {
    
    void create(long userId, long otherUserId);
    
    List<UserProfile> getFriends(long userId);
    
    boolean checkIfFriendshipExists(long userId, long otherUserId); 

}
