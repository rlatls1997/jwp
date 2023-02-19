package user;

import java.util.Map;

import db.DataBase;
import model.User;

public class UserProcessor {

	public void createUser(Map<String, String> queryStringMap) {
		User user = makeUser(queryStringMap);
		DataBase.addUser(user);
	}

	private User makeUser(Map<String, String> queryStringMap) {
		String userId = queryStringMap.get("userId");
		String password = queryStringMap.get("password");
		String name = queryStringMap.get("name");
		String email = queryStringMap.get("email");

		return new User(userId, password, name, email);
	}
}
