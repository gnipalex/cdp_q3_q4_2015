package com.epam.cdp.hnyp.epambook.social.dao.impl;

import java.util.List;

import com.epam.cdp.hnyp.epambook.social.dao.FriendshipDao;
import com.epam.cdp.hnyp.epambook.social.model.UserProfile;

public class FriendshipDaoImpl extends AbstractUserProfileDaoImpl implements FriendshipDao {

    private static final String CREATE = "INSERT INTO friendship(userId,otherUserId) VALUE (?,?)";
    private static final String SELECT_ALL_FRIENDS_PROFILES_FOR_USER_ID = "SELECT * FROM userProfile WHERE id IN ( "
            + "SELECT DISTINCT * FROM ( "
            + "SELECT userId FROM friendship WHERE otherUserId=? "
            + "UNION "
            + "SELECT otherUserId FROM friendship WHERE userId=? "
            + ") )";
    private static final String SELECT_FRIENDSHIP_COUNT_FOR_USERS = "SELECT count(userId) FROM friendship "
            + "WHERE (userId=? AND otherUserId=?) OR (userId=? AND otherUserId=?)";

    @Override
    public void create(long userId, long otherUserId) {
        updateRow(CREATE, userId, otherUserId);
    }

    @Override
    public List<UserProfile> getFriends(long userId) {
        return queryForList(SELECT_ALL_FRIENDS_PROFILES_FOR_USER_ID, userId, userId);
    }

    @Override
    public boolean checkIfFriendshipExists(long userId, long otherUserId) {
        Object[] args = {userId, otherUserId, otherUserId, userId};
        Integer count = getJdbcTemplate().queryForObject(SELECT_FRIENDSHIP_COUNT_FOR_USERS, Integer.class, args);
        return count.intValue() > 0;
    }
    
}
