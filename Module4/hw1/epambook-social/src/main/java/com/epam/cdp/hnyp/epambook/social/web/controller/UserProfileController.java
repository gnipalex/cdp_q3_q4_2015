package com.epam.cdp.hnyp.epambook.social.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.epam.cdp.hnyp.epambook.social.model.UserProfile;
import com.epam.cdp.hnyp.epambook.social.service.UserProfileService;

@RequestMapping("/users")
@RestController
public class UserProfileController {
    
    private static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
    
    @Autowired
    private UserProfileService userProfileService;
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserProfile createProfile(UserProfile userProfile) {
        return userProfileService.create(userProfile);
    }
    
    @InitBinder
    public void bindCustomDateEditor(WebDataBinder binder) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        CustomDateEditor customDateEditor = new CustomDateEditor(format, true);
        binder.registerCustomEditor(Date.class, customDateEditor);
    }
    
}
