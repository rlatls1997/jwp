package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

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
		byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());

		ResponseEntity.response200Html()
			.body(body)
			.build()
			.response(dataOutputStream);
	}

	private void getResource(DataOutputStream dataOutputStream, RequestEntity requestEntity) throws IOException {
		String path = requestEntity.getPath();
		byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

		String contentType = path.endsWith(".css") ? "text/css;charset=utf-8" : "text/html;charset=utf-8";

		ResponseEntity.response200()
			.contentType(contentType)
			.body(body)
			.build()
			.response(dataOutputStream);
	}

	private void getUserList(DataOutputStream dataOutputStream, RequestEntity requestEntity) throws IOException {
		String loginedCookie = requestEntity.getCookie("logined");

		boolean isLogined = Boolean.parseBoolean(loginedCookie);

		byte[] userListHtmlByte = UserProcessor.getUsers().getBytes();
		byte[] body = isLogined ? userListHtmlByte : Files.readAllBytes(new File("./webapp/user/login.html").toPath());

		ResponseEntity.response200Html()
			.body(body)
			.build()
			.response(dataOutputStream);
	}

	private void login(DataOutputStream dataOutputStream, RequestEntity requestEntity) throws IOException {
		String userId = requestEntity.getParameter("userId");
		String password = requestEntity.getParameter("password");

		boolean isLoginSuccess = UserProcessor.isExistUser(userId, password);

		byte[] body = isLoginSuccess ? Files.readAllBytes(new File("./webapp/index.html").toPath()) : Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
		String location = isLoginSuccess ? "/index.html" : "/user/login_failed.html";

		ResponseEntity.response302Html()
			.body(body)
			.location(location)
			.addCookie("logined", String.valueOf(isLoginSuccess))
			.build()
			.response(dataOutputStream);
	}

	private void signUp(DataOutputStream dataOutputStream, RequestEntity requestEntity) throws IOException {
		String userId = requestEntity.getParameter("userId");
		String password = requestEntity.getParameter("password");
		String name = requestEntity.getParameter("name");
		String email = requestEntity.getParameter("email");

		UserProcessor.createUser(userId, password, name, email);

		byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
		ResponseEntity.response302Html()
			.body(body)
			.location("/index.html")
			.build()
			.response(dataOutputStream);
	}
}
