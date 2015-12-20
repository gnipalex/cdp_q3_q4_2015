package com.epam.hnyp.springbooking.dao;

import com.epam.hnyp.springbooking.model.UserAccount;

public interface UserAccountDao {
    
    UserAccount getByUserId(long userId);
    
    UserAccount create(UserAccount userAccount);
    
    UserAccount update(UserAccount userAccount);
    
    boolean delete(long id);
    
}
