package com.epam.hnyp.springbooking.dao.db;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Mockito.doNothing;
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
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.epam.hnyp.springbooking.model.UserAccount;

@RunWith(MockitoJUnitRunner.class) 
public class UserAccountDaoImplTest {

    private static final String SELECT_BY_USER_ID = "SELECT * FROM `userAccount` as ua WHERE ua.userId=?";
    private static final String INSERT_ACCOUNT = "INSERT INTO `userAccount`(userId,prepaidAmount) VALUE (?,?)";
    private static final String UPDATE_BY_ID = "UPDATE `userAccount` SET prepaidAmount=? WHERE userId=?";
    private static final String DELETE_BY_ID = "DELETE FROM `userAccount` WHERE userId=?";  
    
    private static final long USER_ID = 10L;
    private static final BigDecimal PREPAID_AMOUNT = BigDecimal.ZERO;
    
    @Mock
    private JdbcTemplate mockJdbcTemplate;
    @Mock
    private UserAccount mockUserAccount;
    @Mock
    private RowMapper<UserAccount> mockUserAccountRowMapper;
    
    @InjectMocks
    @Spy
    private UserAccountDaoImpl spyUserAccountDao;
    
    @Before
    public void setUp() {
        when(mockUserAccount.getUserId()).thenReturn(USER_ID);
        when(mockUserAccount.getPrepaidAmount()).thenReturn(PREPAID_AMOUNT);
    }
    
    @Test
    public void shouldCallQueryForObject_whenPerformGetByUserId() {
        spyUserAccountDao.getByUserId(USER_ID);
        verify(spyUserAccountDao).queryForObject(SELECT_BY_USER_ID, USER_ID);
    }
    
    @Test
    public void shouldReturnUserAccount_whenPerformGetByUserId() {
        doReturn(mockUserAccount).when(spyUserAccountDao).queryForObject(SELECT_BY_USER_ID, USER_ID);
        UserAccount result = spyUserAccountDao.getByUserId(USER_ID);
        assertThat(result).isEqualTo(mockUserAccount);
    }
    
    @Test
    public void shouldCallUpdateRow_whenPerformCreate() {
        doNothing().when(spyUserAccountDao).updateRow(INSERT_ACCOUNT, USER_ID, PREPAID_AMOUNT);
        spyUserAccountDao.create(mockUserAccount);
        verify(spyUserAccountDao).updateRow(INSERT_ACCOUNT, USER_ID, PREPAID_AMOUNT);
    }
    
    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void shouldThrowIncorrectResultSizeDataAccessException_whenPerformCreateAndNoRowsAffected() {
        when(mockJdbcTemplate.update(anyString(), anyVararg())).thenReturn(0);
        spyUserAccountDao.create(mockUserAccount);
    }
    
    @Test
    public void shouldCallUpdateRow_whenPerformUpdate() {
        doNothing().when(spyUserAccountDao).updateRow(UPDATE_BY_ID, PREPAID_AMOUNT, USER_ID);
        spyUserAccountDao.update(mockUserAccount);
        verify(spyUserAccountDao).updateRow(UPDATE_BY_ID, PREPAID_AMOUNT, USER_ID);
    } 
    
    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void shouldThrowIncorrectResultSizeDataAccessException_whenPerformUpdateAndNoRowsAffected() {
        when(mockJdbcTemplate.update(anyString(), anyVararg())).thenReturn(0);
        spyUserAccountDao.update(mockUserAccount);
    }
    
    @Test
    public void shouldCallJdbcTempdateUpdate_whenPerformDelete() {
        spyUserAccountDao.delete(USER_ID);
        verify(mockJdbcTemplate).update(DELETE_BY_ID, USER_ID);
    }
    
    @Test
    public void shouldReturnTrue_whenPerformDeleteAndThereAreAffectedRows() {
        when(mockJdbcTemplate.update(DELETE_BY_ID, USER_ID)).thenReturn(1);
        boolean result = spyUserAccountDao.delete(USER_ID);
        assertThat(result).isTrue();
    }
    
    @Test
    public void shouldReturnFalse_whenPerformDeleteAndThereAreNOAffectedRows() {
        when(mockJdbcTemplate.update(DELETE_BY_ID, USER_ID)).thenReturn(0);
        boolean result = spyUserAccountDao.delete(USER_ID);
        assertThat(result).isFalse();
    }
    
    @Test
    public void shouldReturnInjectedRowMapper_whenPerformGetRowMapper() {
        RowMapper<UserAccount> result = spyUserAccountDao.getRowMapper();
        assertThat(result).isEqualTo(mockUserAccountRowMapper);
    }
  
}
