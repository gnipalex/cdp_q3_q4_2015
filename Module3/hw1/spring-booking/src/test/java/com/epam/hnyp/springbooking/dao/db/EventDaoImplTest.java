package com.epam.hnyp.springbooking.dao.db;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.epam.hnyp.springbooking.model.Event;

@RunWith(MockitoJUnitRunner.class)
public class EventDaoImplTest {

    private static final String SELECT_BY_ID = "SELECT * FROM `event` AS e WHERE e.id=?";
    private static final String SELECT_ALL_BY_TITLE = "SELECT * FROM `event` AS e WHERE e.title LIKE ? LIMIT ?,?";
    private static final String SELECT_ALL_BY_DATE = "SELECT * FROM `event` AS e WHERE e.date=? LIMIT ?,?";
    private static final String INSERT_EVENT = "INSERT INTO `event`(title,date,ticketPrice) VALUE (?,?,?)";
    private static final String UPDATE_EVENT = "UPDATE `event` SET title=?,date=?,ticketPrice=? WHERE id=?";
    private static final String DELETE_BY_ID = "DELETE FROM `event` WHERE id=?";

    private static final int PAGE_SIZE = 2;
    private static final int PAGE_NUMBER = 3;
    private static final int OFFSET = (PAGE_NUMBER - 1) * PAGE_SIZE;
    
    private static final long EVENT_ID = 10;
    private static final String TITLE = "title";
    private static final String TITLE_FROMATTED = "%" + TITLE + "%";
    private static final Date DATE = new Date();
    private static final BigDecimal PRICE = BigDecimal.valueOf(123.0D);
    
    @Mock
    private JdbcTemplate mockJdbcTemplate;
    @Mock
    private RowMapper<Event> mockEventRowMapper;
    @Mock
    private Event mockEvent;
    
    @Spy
    @InjectMocks
    private EventDaoImpl spyEventDao;
    
    @Test
    public void shouldReturnEvent_whenPerformGetById() {
        doReturn(mockEvent).when(spyEventDao).queryForObject(SELECT_BY_ID, EVENT_ID);
        Event result = spyEventDao.getById(EVENT_ID);
        assertThat(result).isEqualTo(mockEvent);
    }
    
    @Test
    public void shouldReturnEvents_whenPerformGetAllByTitle() {    
        doReturn(asList(mockEvent)).when(spyEventDao).queryForList(SELECT_ALL_BY_TITLE, 
        		TITLE_FROMATTED, OFFSET, PAGE_SIZE);
        List<Event> result = spyEventDao.getAllByTitle(TITLE, PAGE_SIZE, PAGE_NUMBER);
        assertThat(result).containsOnly(mockEvent);
    }
    
    @Test
    public void shouldReturnEvents_whenPerformGetAllForDay() {
        doReturn(asList(mockEvent)).when(spyEventDao).queryForList(SELECT_ALL_BY_DATE,
                DATE, OFFSET, PAGE_SIZE);
        List<Event> result = spyEventDao.getAllForDay(DATE, PAGE_SIZE, PAGE_NUMBER);
        assertThat(result).containsOnly(mockEvent);
    }
    
    @Test
    public void shouldCallUpdateAndGetKeyWithArguments_whenPerformCreate() {
        doReturn(EVENT_ID).when(spyEventDao).updateAndGetKey(anyString(), anyVararg());
        spyEventDao.create(mockEvent);
        verify(spyEventDao).updateAndGetKey(INSERT_EVENT, TITLE, DATE, PRICE);
    }
    
    @Test
    public void shouldSetIdToModel_whenPerformCreate() {
        doReturn(EVENT_ID).when(spyEventDao).updateAndGetKey(INSERT_EVENT, TITLE, DATE, PRICE);
        spyEventDao.create(mockEvent);
        verify(mockEvent).setId(EVENT_ID);
    }
    
    @Test
    public void shouldCallUpdateRow_whenPerformUpdate() {
        doNothing().when(spyEventDao).updateRow(eq(UPDATE_EVENT), anyVararg());
        spyEventDao.update(mockEvent);
        verify(spyEventDao).updateRow(UPDATE_EVENT, TITLE, DATE, PRICE, EVENT_ID);
    }
    
    @Test
    public void shouldCallJdbcTemplateUpdate_whenPerformDelete() {
        spyEventDao.delete(EVENT_ID);
        verify(mockJdbcTemplate).update(DELETE_BY_ID, EVENT_ID);
    }
    
    @Test
    public void shouldReturnTrue_whenPerformDeleteAndThereAreAffectedRows() {
        when(mockJdbcTemplate.update(DELETE_BY_ID, EVENT_ID)).thenReturn(1);
        boolean result = spyEventDao.delete(EVENT_ID);
        assertThat(result).isTrue();
    }
    
    @Test
    public void shouldReturnFalse_whenPerformDeleteAndThereAreNOAffectedRows() {
        when(mockJdbcTemplate.update(DELETE_BY_ID, EVENT_ID)).thenReturn(0);
        boolean result = spyEventDao.delete(EVENT_ID);
        assertThat(result).isFalse();
    }
    
    @Test
    public void shouldReturnInjectedRowMapper_whenPerformGetRowMapper() {
        RowMapper<Event> result = spyEventDao.getRowMapper();
        assertThat(result).isEqualTo(mockEventRowMapper);
    }
    
    @Before
    public void setUp() {
        when(mockEvent.getTitle()).thenReturn(TITLE);
        when(mockEvent.getDate()).thenReturn(DATE);
        when(mockEvent.getTicketPrice()).thenReturn(PRICE);
        when(mockEvent.getId()).thenReturn(EVENT_ID);
    }

}
