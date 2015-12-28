package com.epam.hnyp.springbooking.dao.db;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

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

import com.epam.hnyp.springbooking.model.User;

@RunWith(MockitoJUnitRunner.class)
public class UserDaoImplTest {

    private static final String SELECT_BY_ID = "SELECT * FROM `user` AS u WHERE u.id=?";
    private static final String SELECT_BY_EMAIL = "SELECT * FROM `user` AS u WHERE u.email=?";
    private static final String SELECT_BY_NAME = "SELECT * FROM `user` AS u WHERE u.name LIKE ? LIMIT ?,?";
    private static final String INSERT_USER = "INSERT INTO `user`(name,email) VALUES (?,?)";
    private static final String UPDATE_BY_ID = "UPDATE `user` SET name=?, email=? WHERE id=?";
    private static final String DELETE_BY_ID = "DELETE FROM `user` WHERE id=?";
    
    private static final int PAGE_SIZE = 2;
    private static final int PAGE_NUMBER = 3;
    private static final int OFFSET = (PAGE_NUMBER - 1) * PAGE_SIZE;
    
    private static final long USER_ID = 5L;
    private static final String EMAIL = "email";
    private static final String NAME = "name";
    private static final String NAME_FORMATTED = "%" + NAME + "%";
    
    @Mock
    private RowMapper<User> mockUserRowMapper;
    @Mock
    private User mockUser;
    @Mock
    private JdbcTemplate mockJdbcTemplate;
    
    @InjectMocks
    @Spy
    private UserDaoImpl spyUserDao;
    
    @Before
    public void setUp() {
        when(mockUser.getId()).thenReturn(USER_ID);
        when(mockUser.getEmail()).thenReturn(EMAIL);
        when(mockUser.getName()).thenReturn(NAME);
    }
    
    @Test
    public void shouldCallQueryForObject_whenPerformGetById() {
        spyUserDao.getById(USER_ID);
        verify(spyUserDao).queryForObject(SELECT_BY_ID, USER_ID);
    }
    
    @Test
    public void shouldReturnUser_whenPerformGetById() {
        doReturn(mockUser).when(spyUserDao).queryForObject(SELECT_BY_ID, USER_ID);
        User result = spyUserDao.getById(USER_ID);
        assertThat(result).isEqualTo(mockUser);
    }
    
    @Test
    public void shouldCallQueryForObject_whenPerformGetByEmail() {
        spyUserDao.getByEmail(EMAIL);
        verify(spyUserDao).queryForObject(SELECT_BY_EMAIL, EMAIL);
    }
    
    @Test
    public void shouldReturnUser_whenPerformGetByEmail() {
        doReturn(mockUser).when(spyUserDao).queryForObject(SELECT_BY_EMAIL, EMAIL);
        User result = spyUserDao.getByEmail(EMAIL);
        assertThat(result).isEqualTo(mockUser);
    }
    
    @Test
    public void shouldCallQueryForList_whenPerformGetAllByName() {
        spyUserDao.getAllByName(NAME, PAGE_SIZE, PAGE_NUMBER);
        verify(spyUserDao).queryForList(SELECT_BY_NAME, NAME_FORMATTED, OFFSET, PAGE_SIZE);
    }
    
    @Test
    public void shouldReturnUsersList_whenPerformGetAllByName() {
        doReturn(asList(mockUser)).when(spyUserDao).queryForList(SELECT_BY_NAME, NAME_FORMATTED, OFFSET, PAGE_SIZE);
        List<User> result = spyUserDao.getAllByName(NAME, PAGE_SIZE, PAGE_NUMBER);
        assertThat(result).containsOnly(mockUser);
    }
    
    @Test
    public void shouldCallUpdateAndGetKey_whenPerformCreate() {
        doReturn(USER_ID).when(spyUserDao).updateAndGetKey(INSERT_USER, NAME, EMAIL);
        spyUserDao.create(mockUser);
        verify(spyUserDao).updateAndGetKey(INSERT_USER, NAME, EMAIL);
    }
    
    @Test
    public void shouldSetIdToModel_whenPerformCreate() {
        doReturn(USER_ID).when(spyUserDao).updateAndGetKey(INSERT_USER, NAME, EMAIL);
        spyUserDao.create(mockUser);
        verify(mockUser).setId(USER_ID);
    }
    
    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void shouldThrowIncorrectResultSizeDataAccessException_whenPerformCreateAndNoRowsAffected() {
        when(mockJdbcTemplate.update(INSERT_USER, NAME, EMAIL)).thenReturn(0);
        spyUserDao.create(mockUser);
    }
    
    @Test
    public void shouldCallUpdateRow_whenPerformUpdate() {
        doNothing().when(spyUserDao).updateRow(UPDATE_BY_ID, NAME, EMAIL, USER_ID);
        spyUserDao.update(mockUser);
        verify(spyUserDao).updateRow(UPDATE_BY_ID, NAME, EMAIL, USER_ID);
    }
    
    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void shouldThrowIncorrectResultSizeDataAccessException_whenPerformUpdateAndNORowsAffected() {
        when(mockJdbcTemplate.update(UPDATE_BY_ID, NAME, EMAIL, USER_ID)).thenReturn(0);
        spyUserDao.update(mockUser);
    }
    
    @Test
    public void shouldCallJdbcTemplateUpdate_whenPerformDelete() {
        spyUserDao.delete(USER_ID);
        verify(mockJdbcTemplate).update(DELETE_BY_ID, USER_ID);
    }
    
    @Test
    public void shouldReturnTrue_whenPerformDeleteAndThereAreAffectedRows() {
        when(mockJdbcTemplate.update(DELETE_BY_ID, USER_ID)).thenReturn(1);
        boolean result = spyUserDao.delete(USER_ID);
        assertThat(result).isTrue();
    }
    
    @Test
    public void shouldReturnFalse_whenPerformDeleteAndThereAreNOAffectedRows() {
        when(mockJdbcTemplate.update(DELETE_BY_ID, USER_ID)).thenReturn(0);
        boolean result = spyUserDao.delete(USER_ID);
        assertThat(result).isFalse();
    }
    
    @Test
    public void shouldReturnInjectedRowMapper_whenPerformGetRowMapper() {
        RowMapper<User> result = spyUserDao.getRowMapper();
        assertThat(result).isEqualTo(mockUserRowMapper);
    } 

}
