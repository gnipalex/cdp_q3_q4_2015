package com.epam.hnyp.springbooking.model.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Objects;

import com.epam.hnyp.springbooking.model.UserAccount;

public class UserAccountImpl implements UserAccount {

    private long userId;
    private BigDecimal prepaidAmount = BigDecimal.ZERO; 

    public UserAccountImpl() {
    }
    
    public UserAccountImpl(long userId, BigDecimal prepaidAmount) {
        this.userId = userId;
        this.prepaidAmount = prepaidAmount;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public BigDecimal getPrepaidAmount() {
        return prepaidAmount;
    }

    @Override
    public void setPrepaidAmount(BigDecimal prepaidAmount) {
        this.prepaidAmount = prepaidAmount;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, prepaidAmount);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UserAccount)) {
            return false;
        }
        UserAccount other = (UserAccount) obj;
        return this == other || Objects.equals(userId, other.getUserId())
                && Objects.equals(prepaidAmount, other.getPrepaidAmount());
    }
    
    @Override
    public String toString() {
        return MessageFormat.format("UserAccount[userId={0}; prepaidAmount={1}]", userId, prepaidAmount);
    }
    
}
