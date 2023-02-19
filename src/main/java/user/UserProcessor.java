package user;

import java.util.Map;

import db.DataBase;
import model.User;

public class UserProcessor {

	public void createUser(Map<String, String> requestBodyMap) {
		User user = makeUser(requestBodyMap);
		DataBase.addUser(user);
	}

	private User makeUser(Map<String, String> requestBodyMap) {
		String userId = requestBodyMap.get("userId");
		String password = requestBodyMap.get("password");
		String name = requestBodyMap.get("name");
		String email = requestBodyMap.get("email");

		return new User(userId, password, name, email);
	}
}
