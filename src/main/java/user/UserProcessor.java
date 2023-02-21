package user;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import db.DataBase;
import model.User;

public class UserProcessor {

	public static void createUser(Map<String, String> requestBodyMap) {
		User user = makeUser(requestBodyMap);
		DataBase.addUser(user);
	}

	public static boolean isValidUser(Map<String, String> httpRequestBodyMap) {
		String userId = httpRequestBodyMap.get("userId");
		String password = httpRequestBodyMap.get("password");

		User user = DataBase.findUserById(userId);

		if (Objects.isNull(user)) {
			return false;
		}

		return password.equals(user.getPassword());
	}

	public static String getUsers() {
		Collection<User> users = DataBase.findAll();

		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>사용자 목록</title>");
		sb.append("</head>");
		sb.append("<body>");

		for (User user : users) {
			sb.append("<div>사용자명:")
				.append(user.getName())
				.append(", 이메일:")
				.append(user.getEmail())
				.append("</div>");
		}

		sb.append("</body>");
		sb.append("</html>");

		return sb.toString();
	}

	private static User makeUser(Map<String, String> requestBodyMap) {
		String userId = requestBodyMap.get("userId");
		String password = requestBodyMap.get("password");
		String name = requestBodyMap.get("name");
		String email = requestBodyMap.get("email");

		return new User(userId, password, name, email);
	}
}
