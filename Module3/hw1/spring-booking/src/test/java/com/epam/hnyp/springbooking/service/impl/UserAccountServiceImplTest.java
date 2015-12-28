package com.epam.hnyp.springbooking.service.impl;

import static java.math.BigDecimal.valueOf;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.hnyp.springbooking.dao.UserAccountDao;
import com.epam.hnyp.springbooking.model.UserAccount;

@RunWith(MockitoJUnitRunner.class)
public class UserAccountServiceImplTest {

    private static final BigDecimal NEGATIVE_AMOUNT = BigDecimal.ZERO.subtract(valueOf(0.1));
    private static final BigDecimal AMOUNT = BigDecimal.TEN;
    private static final BigDecimal PREPAID_AMOUNT = AMOUNT.add(valueOf(0.1));
    
    @Mock
    private UserAccount mockUserAccount;
    @Mock
    private UserAccountDao mockUserAccountDao;
    
    @InjectMocks
    @Spy
    private UserAccountServiceImpl spyUserAccountService;
    
    @Before
    public void setUp() {
        when(mockUserAccount.getPrepaidAmount()).thenReturn(PREPAID_AMOUNT);
        doReturn(mockUserAccount).when(spyUserAccountService).createUserAccountInstance();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenPerformWithdrawAmountWithNegativeAmount() {
        spyUserAccountService.withdrawAmountFromAccount(mockUserAccount, NEGATIVE_AMOUNT);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenPerformWithdrawAmountWithNullAmount() {
        spyUserAccountService.withdrawAmountFromAccount(mockUserAccount, null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldThrowIllegalStateException_whenPerformWithdrawAmountAndAccountHasNotEnoughFunds() {
        when(mockUserAccount.getPrepaidAmount()).thenReturn(AMOUNT.subtract(valueOf(0.1)));
        spyUserAccountService.withdrawAmountFromAccount(mockUserAccount, AMOUNT);
    }
    
    @Test
    public void shouldDecreaseAccountPrepaidAmount_whenPerformWithdrawAmount() {
        spyUserAccountService.withdrawAmountFromAccount(mockUserAccount, AMOUNT);
        BigDecimal decreasedAmount = PREPAID_AMOUNT.subtract(AMOUNT);
        verify(mockUserAccount).setPrepaidAmount(decreasedAmount);      
    }
    
    @Test
    public void shouldUpdateAccount_whenPerformWithdrawAmount() {
        spyUserAccountService.withdrawAmountFromAccount(mockUserAccount, AMOUNT);
        verify(mockUserAccountDao).update(mockUserAccount);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenPerformRefillAccountWithNegativeAmount() {
        spyUserAccountService.refillAccountWithAmount(mockUserAccount, NEGATIVE_AMOUNT);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentException_whenPerformRefillAccountWithNullAmount() {
        spyUserAccountService.refillAccountWithAmount(mockUserAccount, null);
    }
    
    @Test
    public void shouldIncreaseAccountPrepaidAmount_whenPerformRefillAccount() {
        spyUserAccountService.refillAccountWithAmount(mockUserAccount, AMOUNT);
        BigDecimal increasedAmount = PREPAID_AMOUNT.add(AMOUNT);
        verify(mockUserAccount).setPrepaidAmount(increasedAmount);
    }
    
    @Test
    public void shouldUpdateAccount_whenPerformRefillAccount() {
        spyUserAccountService.refillAccountWithAmount(mockUserAccount, AMOUNT);
        verify(mockUserAccountDao).update(mockUserAccount);
    }

}
