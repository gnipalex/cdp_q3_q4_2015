package com.epam.hnyp.springbooking.model;

import java.math.BigDecimal;

public interface UserAccount {

    long getUserId();   
    void setUserId(long userId);    
    BigDecimal getPrepaidAmount();   
    void setPrepaidAmount(BigDecimal prepaidAmount);
    
}
