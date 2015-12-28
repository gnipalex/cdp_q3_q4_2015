package com.epam.hnyp.springbooking.dao.db;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
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

import com.epam.hnyp.springbooking.model.Ticket;
import com.epam.hnyp.springbooking.model.Ticket.Category;

@RunWith(MockitoJUnitRunner.class)
public class TicketDaoImplTest {

    private static final String SELECT_ALL_BY_USER_ID_SORTED_BY_EVENT_DATE = "SELECT * FROM `ticket` AS t "
            + "JOIN `event` AS e ON t.eventId = e.id "
            + "WHERE t.userId = ? "
            + "ORDER BY e.date DESC " + "LIMIT ?,?";
    private static final String SELECT_ALL_BY_EVENT_ID_SORTED_BY_USER_EMAIL = 
            "SELECT * FROM `ticket` AS t "
            + "JOIN `user` AS u ON t.userId = u.id "
            + "WHERE t.eventId=? "
            + "ORDER BY u.email ASC "
            + "LIMIT ?,?";
    private static final String SELECT_BY_ID_AND_PLACE_AND_CATEGORY = "SELECT * FROM `ticket` AS t "
            + "WHERE t.id=? AND t.place=? AND t.category=?";
    private static final String INSERT_TICKET = "INSERT INTO `ticket`(place,category,eventId,userId) "
            + "VALUE (?,?,?,?)";
    private static final String DELETE_BY_ID = "DELETE FROM `ticket` WHERE id=?";
    
    private static final int PAGE_SIZE = 2;
    private static final int PAGE_NUMBER = 3;
    private static final int OFFSET = (PAGE_NUMBER - 1) * PAGE_SIZE;
    
    private static final long TICKET_ID = 11L;
    private static final long EVENT_ID = 5L;
    private static final long USER_ID = 7L;
    private static final Ticket.Category CATEGORY = Category.BAR;
    private static final int PLACE = 88;
    
    @Mock
    private JdbcTemplate mockJdbcTemplate;
    @Mock
    private RowMapper<Ticket> mockTicketRowMapper;
    @Mock
    private Ticket mockTicket;
    
    @InjectMocks
    @Spy
    private TicketDaoImpl spyTicketDao;
    
    @Test
    public void shouldCallUpdateAndGetKeyWithArguments_whenPerformCreate() {
        doReturn(TICKET_ID).when(spyTicketDao).updateAndGetKey(anyString(), anyVararg());
        spyTicketDao.create(mockTicket);
        verify(spyTicketDao).updateAndGetKey(INSERT_TICKET, PLACE, valueOf(CATEGORY), EVENT_ID, USER_ID);
    }
    
    @Test
    public void shouldSetIdToModel_whenPerformCreate() {
        doReturn(TICKET_ID).when(spyTicketDao).updateAndGetKey(INSERT_TICKET, PLACE, valueOf(CATEGORY), EVENT_ID, USER_ID);
        spyTicketDao.create(mockTicket);
        verify(mockTicket).setId(TICKET_ID);
    }
    
    @Test(expected = IncorrectResultSizeDataAccessException.class)
    public void shouldThrowIncorrectResultSizeDataAccessException_whenPerformCreateAndNoRowsAffected() {
        when(mockJdbcTemplate.update(anyString(), anyVararg())).thenReturn(0);
        spyTicketDao.create(mockTicket);
    }
    
    @Test
    public void shouldReturnTicketsList_whenPerformGetAllByUserIdSortedByEventDate() {
        doReturn(asList(mockTicket)).when(spyTicketDao).queryForList(SELECT_ALL_BY_USER_ID_SORTED_BY_EVENT_DATE, 
                USER_ID, OFFSET, PAGE_SIZE);
        List<Ticket> result = spyTicketDao.getAllByUserIdSortedByEventDate(USER_ID, PAGE_SIZE, PAGE_NUMBER);
        assertThat(result).containsOnly(mockTicket);
    }
    
    @Test
    public void shouldReturnTicketsList_whenPerformGetAllByEventIdSortedByUserEmail() {
        doReturn(asList(mockTicket)).when(spyTicketDao).queryForList(SELECT_ALL_BY_EVENT_ID_SORTED_BY_USER_EMAIL, 
                EVENT_ID, OFFSET, PAGE_SIZE);
        List<Ticket> result = spyTicketDao.getAllByEventIdSortedByUserEmail(EVENT_ID, PAGE_SIZE, PAGE_NUMBER);
        assertThat(result).containsOnly(mockTicket);
    }
    
    @Test
    public void shouldReturnTicket_whenPerformGetByEventIdAndPlace() {
        doReturn(mockTicket).when(spyTicketDao).queryForObject(SELECT_BY_ID_AND_PLACE_AND_CATEGORY, 
                TICKET_ID, PLACE, valueOf(CATEGORY));
        Ticket result = spyTicketDao.getByIdAndPlaceAndCategory(TICKET_ID, PLACE, CATEGORY);
        assertThat(result).isEqualTo(mockTicket);
    }
    
    @Test
    public void shouldCallJdbcTemplateUpdate_whenPerformDelete() {
        spyTicketDao.delete(TICKET_ID);
        verify(mockJdbcTemplate).update(DELETE_BY_ID, TICKET_ID);
    }
    
    @Test
    public void shouldReturnTrue_whenPerformDeleteAndThereAreAffectedRows() {
        when(mockJdbcTemplate.update(DELETE_BY_ID, TICKET_ID)).thenReturn(1);
        boolean result = spyTicketDao.delete(TICKET_ID);
        assertThat(result).isTrue();
    }
    
    @Test
    public void shouldReturnFalse_whenPerformDeleteAndThereAreNOAffectedRows() {
        when(mockJdbcTemplate.update(DELETE_BY_ID, TICKET_ID)).thenReturn(0);
        boolean result = spyTicketDao.delete(TICKET_ID);
        assertThat(result).isFalse();
    }
    
    @Test
    public void shouldReturnInjectedRowMapper_whenPerformGetRowMapper() {
        RowMapper<Ticket> result = spyTicketDao.getRowMapper();
        assertThat(result).isEqualTo(mockTicketRowMapper);
    }
    
    @Before
    public void setUp() {
        when(mockTicket.getCategory()).thenReturn(CATEGORY);
        when(mockTicket.getEventId()).thenReturn(EVENT_ID);
        when(mockTicket.getPlace()).thenReturn(PLACE);
        when(mockTicket.getUserId()).thenReturn(USER_ID);
        when(mockTicket.getId()).thenReturn(TICKET_ID);       
    }

}
