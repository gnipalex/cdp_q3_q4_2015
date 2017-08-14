package com.epam.cdp.hnyp.epambook.social.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.cdp.hnyp.epambook.social.model.UserProfile;
import com.epam.cdp.hnyp.epambook.social.service.FriendshipService;

@RequestMapping("users/{userName}/friends")
@RestController
public class FriendsController {

    @Autowired
    private FriendshipService friendshipService;
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserProfile> getAllFriends(@PathVariable String userName) {
        return friendshipService.getFriends(userName);
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void addToFriends(@PathVariable String userName, 
            @RequestParam String friendName) {
        friendshipService.create(userName, friendName);
    }
    
}
