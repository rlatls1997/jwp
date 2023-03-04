package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.UserProcessor;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private static final int START_LINE = 0;

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
			connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			InputStreamReader isr = new InputStreamReader(in);

			DataOutputStream dataOutputStream = new DataOutputStream(out);

			RequestEntity requestEntity = RequestEntity.from(isr);
			String requestPath = requestEntity.getPath();
			String requestMethod = requestEntity.getMethod();

			if (requestPath.equals("/user/create") && requestMethod.equals("POST")) {
				signUp(dataOutputStream, requestEntity);
			} else if (requestPath.equals("/user/login") && requestMethod.equals("POST")) {
				login(dataOutputStream, requestEntity);
			} else if (requestPath.equals("/user/list") && requestMethod.equals("GET")) {
				getUserList(dataOutputStream, requestEntity);
			} else if ((requestPath.equals("/") || requestPath.isBlank()) && requestMethod.equals("GET")) {
				getRoot(dataOutputStream);
			} else {
				getResource(dataOutputStream, requestEntity);
			}
		} catch (IOException ioException) {
			log.error(ioException.getMessage());
		}
	}

	private void getRoot(DataOutputStream dataOutputStream) throws IOException {
		ResponseEntity responseEntity = new ResponseEntity(dataOutputStream);

		responseEntity.forward("/index.html");
	}

	private void getResource(DataOutputStream dataOutputStream, RequestEntity requestEntity) throws IOException {
		ResponseEntity responseEntity = new ResponseEntity(dataOutputStream);
		String path = requestEntity.getPath();

		responseEntity.forward(path);
	}

	private void getUserList(DataOutputStream dataOutputStream, RequestEntity requestEntity) throws IOException {
		String loginedCookie = requestEntity.getCookie("logined");
		boolean isLogined = Boolean.parseBoolean(loginedCookie);

		ResponseEntity responseEntity = new ResponseEntity(dataOutputStream);

		if (isLogined) {
			byte[] userListHtmlByte = UserProcessor.getUsers().getBytes();
			responseEntity.forward(userListHtmlByte);
			return;
		}

		responseEntity.forward("/user/login.html");
	}

	private void login(DataOutputStream dataOutputStream, RequestEntity requestEntity) throws IOException {
		String userId = requestEntity.getParameter("userId");
		String password = requestEntity.getParameter("password");

		boolean isLoginSuccess = UserProcessor.isExistUser(userId, password);

		ResponseEntity responseEntity = new ResponseEntity(dataOutputStream);

		if (isLoginSuccess) {
			responseEntity.addHeader("Set-Cookie", "logined=true;path=/;");
			responseEntity.sendRedirect("/index.html");
			return;
		}

		responseEntity.addHeader("Set-Cookie", "logined=false;path=/;");
		responseEntity.sendRedirect("/user/login_failed.html");
	}

	private void signUp(DataOutputStream dataOutputStream, RequestEntity requestEntity) throws IOException {
		String userId = requestEntity.getParameter("userId");
		String password = requestEntity.getParameter("password");
		String name = requestEntity.getParameter("name");
		String email = requestEntity.getParameter("email");

		UserProcessor.createUser(userId, password, name, email);

		ResponseEntity responseEntity = new ResponseEntity(dataOutputStream);
		responseEntity.sendRedirect("/index.html");
	}
}
