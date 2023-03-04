package user;

import java.util.Collection;
import java.util.Objects;

import db.DataBase;
import model.User;

public final class UserProcessor {
	private UserProcessor() {
	}

	public static void createUser(String userId, String password, String name, String email) {
		User user = new User(userId, password, name, email);
		DataBase.addUser(user);
	}

	public static boolean isExistUser(String userId, String password) {
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
}
