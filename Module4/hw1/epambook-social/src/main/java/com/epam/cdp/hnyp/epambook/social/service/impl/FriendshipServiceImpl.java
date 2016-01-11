package com.epam.cdp.hnyp.epambook.social.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.epam.cdp.hnyp.epambook.social.dao.FriendshipDao;
import com.epam.cdp.hnyp.epambook.social.model.UserProfile;
import com.epam.cdp.hnyp.epambook.social.service.FriendshipService;
import com.epam.cdp.hnyp.epambook.social.service.UserProfileService;

public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipDao friendshipDao;
    @Autowired
    private UserProfileService userProfileService;
    
    @Override
    public void create(String userName, String otherUserName) {
        long userId = getExistingProfileIdByUserName(userName);
        long otherUserId = getExistingProfileIdByUserName(otherUserName);
        assertFriendshipDoesNotExist(userId, otherUserId);
        friendshipDao.create(userId, otherUserId);
    }
    
    private long getExistingProfileIdByUserName(String userName) {
        UserProfile userProfile = userProfileService.getExistingUserProfile(userName);
        return userProfile.getId();
    }
    
    private void assertFriendshipDoesNotExist(long userId, long otherUserId) {
        if (friendshipDao.checkIfFriendshipExists(userId, otherUserId)) {
            throw new IllegalStateException("friendship already exists");
        }
    }  

    @Override
    public List<UserProfile> getFriends(String userName) {
        UserProfile userProfile = userProfileService.getExistingUserProfile(userName);
        return friendshipDao.getFriends(userProfile.getId());
    }

    @Override
    public UserProfile getFriendProfile(String userName, String friendName) {
        long userId = getExistingProfileIdByUserName(userName);
        UserProfile friendProfile = userProfileService.getByUserName(friendName);
        long friendId = friendProfile.getId();
        assertFriendshipExists(userId, friendId);
        return friendProfile;
    }
    
    private void assertFriendshipExists(long userId, long otherUserId) {
        if (!friendshipDao.checkIfFriendshipExists(userId, otherUserId)) {
            throw new IllegalStateException("friendship does not exist");
        }
    }  

}
