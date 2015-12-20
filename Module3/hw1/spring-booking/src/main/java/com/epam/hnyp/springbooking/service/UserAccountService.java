package com.epam.hnyp.springbooking.service;

import java.math.BigDecimal;

import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.UserAccount;

public interface UserAccountService {
    
    UserAccount getAccountByUserId(long userId);
    
    UserAccount createAccount(User user);
    
    /**
     * Decreases user account on provided amount
     * @param account
     * @param amount
     * @throws IllegalArgumentException if amount is negative or null
     * @throws IllegalStateException if account amount is less than required amount
     */
    void withdrawAmountFromAccount(UserAccount account, BigDecimal amount);
    
    /**
     * Adds provided amount to account
     * @param account
     * @param amount
     * @return true if account was refilled
     * @throws IllegalArgumentException if amount is negative or null
     */
    void refillAccountWithAmount(UserAccount account, BigDecimal amount);
    
}
