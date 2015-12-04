package com.epam.hnyp.springbooking.dao.storage;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.epam.hnyp.springbooking.dao.UserDao;
import com.epam.hnyp.springbooking.model.User;
import com.epam.hnyp.springbooking.utils.BookingUtils;

public class StorageUserDao implements UserDao {

	private static final String USER_KEY_FORMAT = "user:{0}";
	private static final String USER_PREFIX = "user";
	
	private Storage storage;

	@Override
	public User getById(long userId) {
		return storage.get(getKey(userId), User.class);
	}
	
	private String getKey(long eventId) {
		return MessageFormat.format(USER_KEY_FORMAT, eventId);
	}

	@Override
	public User getByEmail(String email) {
		return storage.getAllByPrefix(USER_PREFIX, User.class).stream()
				.filter(u -> StringUtils.equals(email, u.getEmail()))
				.findFirst()
				.orElse(null);
	}

	@Override
	public List<User> getAllByName(String name, int pageSize, int pageNum) {
		Stream<User> userStream = storage.getAllByPrefix(USER_PREFIX, User.class).stream()
									.filter(u -> StringUtils.contains(u.getName(), name));
		return BookingUtils.getPageFromStream(userStream, pageSize, pageNum);
	}

	@Override
	public User create(User user) {
		long id = BookingUtils.getNextToMaxLong(storage.getAllByPrefix(USER_PREFIX, User.class), User::getId);
		user.setId(id);
		return storage.put(getKey(id), user) ? user : null;
	}

	@Override
	public User update(User user) {
		String key = getKey(user.getId());
		User updatedUser = null;
		if (storage.remove(key)) {
			storage.put(key, user);
			updatedUser = user;
		}
		return updatedUser;
	}

	@Override
	public boolean delete(long userId) {
		return storage.remove(getKey(userId));
	}

}
