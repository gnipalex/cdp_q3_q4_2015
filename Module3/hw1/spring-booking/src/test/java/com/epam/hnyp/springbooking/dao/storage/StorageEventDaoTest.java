package com.epam.hnyp.springbooking.dao.storage;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.hnyp.springbooking.model.Event;

@RunWith(MockitoJUnitRunner.class)
public class StorageEventDaoTest {

	private static final String EVENT_KEY_FORMAT = "event:{0}";
	private static final String EVENT_PREFIX = "event";
	
	private static final String TITLE = "title_AAA";
	private static final String OTHER_TITLE = "AAA";
	private static final Date DAY = new Date(); 
	
	private static final int PAGE_SIZE = 2;
	private static final int PAGE_NUMBER = 1;
	
	private static final long ID_1 = 1L;
	
	@Mock
	private Storage mockStorage;
	@Mock
	private Event mockEvent1;
	@Mock
	private Event mockEvent2;
	@Mock
	private Event mockEvent3;
	
	@InjectMocks
	private StorageEventDao storageEventDao;
	
	@Before
	public void setUp() {
		String key = formatKey(ID_1);
		when(mockStorage.get(key, Event.class)).thenReturn(mockEvent1);
		
		when(mockEvent1.getTitle()).thenReturn(TITLE);
		when(mockEvent2.getTitle()).thenReturn(TITLE);
		when(mockEvent3.getTitle()).thenReturn(OTHER_TITLE);
		when(mockEvent1.getDate()).thenReturn(DAY);
		when(mockEvent2.getDate()).thenReturn(DAY);
		when(mockEvent3.getDate()).thenReturn(DAY);
		when(mockEvent1.getId()).thenReturn(1L);
		when(mockEvent2.getId()).thenReturn(2L);
		when(mockEvent3.getId()).thenReturn(3L);
		when(mockStorage.getAllByPrefix(EVENT_PREFIX, Event.class))
			.thenReturn(asList(mockEvent1, mockEvent2, mockEvent3));
	}
	
	private String formatKey(long id) {
		return MessageFormat.format(EVENT_KEY_FORMAT, id);
	}
	
	@Test
	public void shouldCallStorage_whenPerformGetById() {
		storageEventDao.getById(ID_1);
		String key = formatKey(ID_1);
		Mockito.verify(mockStorage).get(key, Event.class);
	}
	
	@Test
	public void shouldReturnEvent_whenPerformGetByIdAndEventExists() {
		Event storedEvent = storageEventDao.getById(ID_1);
		assertThat(storedEvent).isEqualTo(mockEvent1);
	}
	
	@Test
	public void shouldReturnNull_whenPerformGetByIdAndNoEventFoundById() {
		String key = formatKey(ID_1);
		when(mockStorage.get(key, Event.class)).thenReturn(null);
		Event storedEvent = storageEventDao.getById(ID_1);
		assertThat(storedEvent).isNull();
	}
	
	@Test
	public void shouldReturnEventsWhichTitleContainsProvidedTitle_whenPerformGetAllByTitle() {
		List<Event> eventsByTitle = storageEventDao.getAllByTitle("title", 10, 1);
		assertThat(eventsByTitle).containsOnly(mockEvent1, mockEvent2);
	}
	
	@Test
	public void shouldReturnEmptyList_whenPerformGetAllByTitleAndNoMatchesFound() {
		List<Event> eventsByTitle = storageEventDao.getAllByTitle("some title", 10, 1);
		assertThat(eventsByTitle).isEmpty();
	}
	
	@Test
	public void shouldReturnElementsNotMoreThanPageSize_whenPerformGetAllByTitle() {
		List<Event> eventsByTitle = storageEventDao.getAllByTitle("AAA", PAGE_SIZE, PAGE_NUMBER);
		assertThat(eventsByTitle.size()).isEqualTo(PAGE_SIZE);
	}
	
	@Test
	public void shouldReturnAllEventsForDay_whenPerformGetAllForDay() {
		List<Event> eventsForDay = storageEventDao.getAllForDay(DAY, 10, 1);
		assertThat(eventsForDay).containsOnly(mockEvent1, mockEvent2, mockEvent3);
	}
	
	@Test
	public void shouldReturnElementsNotMoreThanPageSize_whenPerformGetAllForDay() {
		List<Event> eventsForDay = storageEventDao.getAllForDay(DAY, PAGE_SIZE, PAGE_NUMBER);
		assertThat(eventsForDay.size()).isEqualTo(PAGE_SIZE);
	}
	
	@Test
	public void shouldSaveEventWithGeneratedID_whenPerformCreate() {
		Event newEvent = mock(Event.class);
		storageEventDao.create(newEvent);
		
		long expectedGeneratedID = 4L;
		verify(newEvent).setId(expectedGeneratedID);
		verify(mockStorage).put(formatKey(expectedGeneratedID), newEvent);
	}
	
	@Test
	public void shouldRemoveOldAndSaveNewEvent_whenPerformUpdate() {
		String key = formatKey(ID_1);
		when(mockStorage.remove(key)).thenReturn(true);
		storageEventDao.update(mockEvent1);
		verify(mockStorage).put(key, mockEvent1);
	}
	
	@Test
	public void shouldRemoveEvent_whenPerformDelete() {
		storageEventDao.delete(ID_1);
		String key = formatKey(ID_1);
		verify(mockStorage).remove(key);
	}

}
