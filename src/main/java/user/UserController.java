package user;

import java.util.Collection;
import java.util.Objects;

import db.DataBase;
import model.User;
import webserver.AbstractController;
import webserver.RequestEntity;
import webserver.ResponseEntity;

public class UserController extends AbstractController {

	@Override
	public void doGet(RequestEntity requestEntity, ResponseEntity responseEntity) {
		String path = requestEntity.getPath();

		if (path.equals("/user/list")) {
			getUserList(requestEntity, responseEntity);
			return;
		}

		responseEntity.forward(path);
	}

	@Override
	public void doPost(RequestEntity requestEntity, ResponseEntity responseEntity) {
		String path = requestEntity.getPath();

		if (path.equals("/user/create")) {
			signUp(requestEntity, responseEntity);
			return;
		}

		if (path.equals("/user/login")) {
			login(requestEntity, responseEntity);
			return;
		}

		throw new IllegalStateException("Request path not found. path:" + path);
	}

	private void getUserList(RequestEntity requestEntity, ResponseEntity responseEntity) {
		String loginedCookie = requestEntity.getCookie("logined");
		boolean isLogined = Boolean.parseBoolean(loginedCookie);

		if (isLogined) {
			byte[] userListHtmlByte = getUsers().getBytes();
			responseEntity.forward(userListHtmlByte);
			return;
		}

		responseEntity.forward("/user/login.html");
	}

	private void login(RequestEntity requestEntity, ResponseEntity responseEntity) {
		String userId = requestEntity.getParameter("userId");
		String password = requestEntity.getParameter("password");

		boolean isLoginSuccess = isExistUser(userId, password);

		if (isLoginSuccess) {
			responseEntity.addHeader("Set-Cookie", "logined=true;path=/;");
			responseEntity.sendRedirect("/index.html");
			return;
		}

		responseEntity.addHeader("Set-Cookie", "logined=false;path=/;");
		responseEntity.sendRedirect("/user/login_failed.html");
	}

	private void signUp(RequestEntity requestEntity, ResponseEntity responseEntity) {
		String userId = requestEntity.getParameter("userId");
		String password = requestEntity.getParameter("password");
		String name = requestEntity.getParameter("name");
		String email = requestEntity.getParameter("email");

		createUser(userId, password, name, email);

		responseEntity.sendRedirect("/index.html");
	}

	private void createUser(String userId, String password, String name, String email) {
		User user = new User(userId, password, name, email);
		DataBase.addUser(user);
	}

	private boolean isExistUser(String userId, String password) {
		User user = DataBase.findUserById(userId);

		if (Objects.isNull(user)) {
			return false;
		}

		return password.equals(user.getPassword());
	}

	private String getUsers() {
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
