package com.epam.hnyp.springbooking.dao.storage;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.MessageFormat;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.epam.hnyp.springbooking.model.User;

@RunWith(MockitoJUnitRunner.class)
public class StorageUserDaoTest {

	private static final String USER_KEY_FORMAT = "user:{0}";
	private static final String USER_PREFIX = "user";
	
	private static final long ID_1 = 1L;
	
	private static final int PAGE_SIZE = 2;
	private static final int PAGE_NUMBER = 1;
	
	@Mock
	private Storage mockStorage;
	@Mock
	private User mockUser1;
	@Mock
	private User mockUser2;
	@Mock
	private User mockUser3;
	
	@InjectMocks
	private StorageUserDao storageUserDao;
	
	@Before
	public void setUp() {
		when(mockStorage.get(formatKey(ID_1), User.class)).thenReturn(mockUser1);

		when(mockUser1.getEmail()).thenReturn("email_1");
		when(mockUser2.getEmail()).thenReturn("email_2");
		when(mockUser3.getEmail()).thenReturn("email_3");
		when(mockUser1.getId()).thenReturn(1L);
		when(mockUser2.getId()).thenReturn(2L);
		when(mockUser3.getId()).thenReturn(3L);
		when(mockUser1.getName()).thenReturn("first_name");
		when(mockUser2.getName()).thenReturn("first_name_name");
		when(mockUser3.getName()).thenReturn("sur_name");
		when(mockStorage.getAllByPrefix(USER_PREFIX, User.class))
			.thenReturn(asList(mockUser1, mockUser2, mockUser3));
	}
	
	private String formatKey(long id) {
		return MessageFormat.format(USER_KEY_FORMAT, id);
	}
	
	@Test
	public void shouldReturnUser_whenPerformGetByIdAndUserExists() {
		User storedUser = storageUserDao.getById(ID_1);
		assertThat(storedUser).isEqualTo(mockUser1);
	}
	
	@Test
	public void shouldReturnNull_whenPerformGetByIdAndNoUserFoundById() {
		String key = formatKey(ID_1);
		when(mockStorage.get(key, User.class)).thenReturn(null);
		User storedUser = storageUserDao.getById(ID_1);
		assertThat(storedUser).isNull();
	}
	
	@Test
	public void shouldReturnUsersWhichNameContainsProvidedName_whenPerformGetAllByName() {
		List<User> usersByName = storageUserDao.getAllByName("first_name", 10, 1);
		assertThat(usersByName).containsOnly(mockUser1, mockUser2);
	}
	
	@Test
	public void shouldReturnEmptyList_whenPerformGetAllByNameAndNoUsersFound() {
		List<User> usersByName = storageUserDao.getAllByName("some name", 10, 1);
		assertThat(usersByName).isEmpty();
	}
	
	@Test
	public void shouldReturnElementsNotMoreThanPageSize_whenPerformGetAllByName() {
		List<User> usersByName = storageUserDao.getAllByName("name", PAGE_SIZE, PAGE_NUMBER);
		assertThat(usersByName.size()).isEqualTo(PAGE_SIZE);
	}
	
	@Test
	public void shouldReturnUser_whenPerformGetByEmailAndUserExists() {
		User storedUser = storageUserDao.getByEmail("email_1");
		assertThat(storedUser).isEqualTo(mockUser1);
	}
	
	@Test
	public void shouldReturnNull_whenPerformGetByEmailAndUserNotFound() {
		User storedUser = storageUserDao.getByEmail("some@email");
		assertThat(storedUser).isNull();
	}
	
	@Test
	public void shouldSaveUserWithGeneratedID_whenPerformCreate() {
		User newUser = mock(User.class);
		storageUserDao.create(newUser);
		
		long expectedGeneratedID = 4L;
		verify(newUser).setId(expectedGeneratedID);
		verify(mockStorage).put(formatKey(expectedGeneratedID), newUser);
	}
	
	@Test
	public void shouldRemoveOldAndSaveNewUser_whenPerformUpdate() {
		String key = formatKey(ID_1);
		when(mockStorage.remove(key)).thenReturn(true);
		
		User updatedUser = storageUserDao.update(mockUser1);
		
		assertThat(updatedUser).isEqualTo(mockUser1);
		verify(mockStorage).put(key, mockUser1);
	}
	
	@Test
	public void shouldRemoveEvent_whenPerformDelete() {
		storageUserDao.delete(ID_1);
		String key = formatKey(ID_1);
		verify(mockStorage).remove(key);
	}

}
