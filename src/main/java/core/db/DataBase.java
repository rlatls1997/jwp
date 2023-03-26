package core.db;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;

import com.google.common.collect.Maps;

import next.model.User;

public class DataBase {
	private static Map<String, User> users = Maps.newHashMap();

	public static void addUser(User user) {
		users.put(user.getUserId(), user);
	}

	public static User findUserById(String userId) {
		return users.get(userId);
	}

	public static void updateUser(User user) {
		String userId = user.getUserId();

		User registeredUser = findUserById(userId);

		if (Objects.isNull(registeredUser)) {
			throw new NoSuchElementException("update user not exist. userId:" + userId);
		}

		addUser(user);
	}

	public static Collection<User> findAll() {
		return users.values();
	}
}
