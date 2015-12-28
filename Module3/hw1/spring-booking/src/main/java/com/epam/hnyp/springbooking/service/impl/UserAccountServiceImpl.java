package com.epam.hnyp.springbooking.service.impl;

import java.math.BigDecimal;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;

import com.epam.hnyp.springbooking.dao.UserAccountDao;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.model.UserAccount;
import com.epam.hnyp.springbooking.service.UserAccountService;

public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountDao userAccountDao;
    
    @Override
    public UserAccount getAccountByUserId(long userId) {
        return userAccountDao.getByUserId(userId);
    }

    @Override
    public UserAccount createAccount(User user) {
        UserAccount account = createUserAccountInstance();
        account.setUserId(user.getId());
        return userAccountDao.create(account);
    }

    @Lookup("userAccountInstance")
    protected UserAccount createUserAccountInstance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void withdrawAmountFromAccount(UserAccount account, BigDecimal amount) {
        assertAmountIsPositiveAndIsNotNull(amount);
        BigDecimal actualAmount = account.getPrepaidAmount();
        BigDecimal leftAmount = actualAmount.subtract(amount);
        if (BigDecimal.ZERO.compareTo(leftAmount) > 0) {
            throw new IllegalStateException(MessageFormat.format("account {0} has not enough funds", account.getUserId()));
        }
        account.setPrepaidAmount(leftAmount);
        userAccountDao.update(account);
    }
    
    private void assertAmountIsPositiveAndIsNotNull(BigDecimal amount) {
        if (amount == null)  {
            throw new IllegalArgumentException("amount can't be null");
        }
        if (BigDecimal.ZERO.compareTo(amount) > 0) {
            throw new IllegalArgumentException("amount should be positive"); 
        }
    }

    @Override
    public void refillAccountWithAmount(UserAccount account, BigDecimal amount) {
        assertAmountIsPositiveAndIsNotNull(amount);
        BigDecimal actualAmount = account.getPrepaidAmount();
        BigDecimal refilledAmount = actualAmount.add(amount);
        account.setPrepaidAmount(refilledAmount);
        userAccountDao.update(account);
    }

}
