package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.UserProcessor;
import util.HttpRequestUtils;
import util.IOUtils;

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
			BufferedReader br = new BufferedReader(isr);

			List<String> httpRequestContents = getHttpRequestContents(br);
			String httpRequestPath = getHttpRequestPath(httpRequestContents);
			String httpCookie = getCookie(httpRequestContents);
			Map<String, String> httpRequestBodyMap = getHttpRequestBodyMap(br, httpRequestContents);

			DataOutputStream dos = new DataOutputStream(out);

			switch (httpRequestPath) {
				case "/user/create" -> signUp(dos, httpRequestBodyMap);
				case "/user/login" -> login(httpRequestBodyMap, dos);
				case "/user/list" -> getUserList(httpCookie, dos);
				default -> getResource(httpRequestPath, dos);
			}
		} catch (IOException ioException) {
			log.error(ioException.getMessage());
		}
	}

	private void getResource(String httpRequestPath, DataOutputStream dos) throws IOException {
		byte[] body = Files.readAllBytes(new File("./webapp" + httpRequestPath).toPath());

		if (httpRequestPath.endsWith(".css")) {
			responseCss200Header(dos, body.length);
			responseBody(dos, body);
			return;
		}

		response200Header(dos, body.length);
		responseBody(dos, body);
	}

	private void getUserList(String httpCookie, DataOutputStream dos) throws IOException {
		Map<String, String> cookieMap = HttpRequestUtils.parseCookies(httpCookie);
		String logined = cookieMap.get("logined");

		if (Objects.isNull(logined)) {
			byte[] body = Files.readAllBytes(new File("./webapp/user/login.html").toPath());
			response200Header(dos, body.length);
			responseBody(dos, body);
			return;
		}

		boolean isLogined = Boolean.parseBoolean(logined);

		if (isLogined) {
			String userListHtml = UserProcessor.getUsers();
			byte[] body = userListHtml.getBytes();
			response200Header(dos, body.length);
			responseBody(dos, body);
			return;
		}

		byte[] body = Files.readAllBytes(new File("./webapp/user/login.html").toPath());
		response200Header(dos, body.length);
		responseBody(dos, body);
	}

	private void login(Map<String, String> httpRequestBodyMap, DataOutputStream dos) throws IOException {
		boolean isLoginSuccess = UserProcessor.isValidUser(httpRequestBodyMap);

		if (isLoginSuccess) {
			byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
			response302HeaderWithLoginedCookie(dos, body.length, true, "/index.html");
			responseBody(dos, body);
			return;
		}

		byte[] body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
		response302HeaderWithLoginedCookie(dos, body.length, false, "/user/login_failed.html");
		responseBody(dos, body);
	}

	private void signUp(DataOutputStream dos, Map<String, String> httpRequestBodyMap) throws IOException {
		UserProcessor.createUser(httpRequestBodyMap);

		byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
		response302HeaderLocationIndexHtml(dos, body.length);
		responseBody(dos, body);
	}

	public Map<String, String> getHttpRequestBodyMap(BufferedReader br, List<String> httpRequestContents) throws IOException {
		int contentLength = getContentLength(httpRequestContents);
		String httpRequestBodyString = IOUtils.readData(br, contentLength);

		return HttpRequestUtils.parseRequestBody(httpRequestBodyString);
	}

	private int getContentLength(List<String> httpRequestContents) {
		for (String httpRequestContent : httpRequestContents) {
			if (httpRequestContent.startsWith("Content-Length")) {
				int contentLengthValueStartIndex = httpRequestContent.indexOf(":") + 1;
				String contentLengthValueString = httpRequestContent.substring(contentLengthValueStartIndex).trim();

				return Integer.parseInt(contentLengthValueString);
			}
		}

		return 0;
	}

	private String getCookie(List<String> httpRequestContents) {
		for (String httpRequestContent : httpRequestContents) {
			if (httpRequestContent.startsWith("Cookie")) {
				int cookieValueStartIndex = httpRequestContent.indexOf(":") + 1;

				return httpRequestContent.substring(cookieValueStartIndex).trim();
			}
		}

		return "";
	}

	private String getHttpRequestPath(List<String> httpRequestContents) {
		String httpRequestStartLine = httpRequestContents.get(START_LINE);
		String httpRequestUrl = getHttpRequestUrl(httpRequestStartLine);

		int queryStringStartIndex = httpRequestUrl.indexOf("?");

		if (queryStringStartIndex == -1) {
			return httpRequestUrl;
		}

		return httpRequestUrl.substring(0, queryStringStartIndex);
	}

	private String getHttpRequestUrl(String httpRequestStartLine) {
		String[] tokens = httpRequestStartLine.split(" ");
		return tokens[1];
	}

	private List<String> getHttpRequestContents(BufferedReader br) throws IOException {
		List<String> httpRequestContents = new ArrayList<>();

		String line = br.readLine();

		log.info("HttpRequest print start.");

		while (!"".equals(line)) {
			if (line == null) {
				return httpRequestContents;
			}

			httpRequestContents.add(line);

			log.info(line);

			line = br.readLine();
		}

		log.info("HttpRequest print end.");

		return httpRequestContents;
	}

	private void response302HeaderLocationIndexHtml(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("Location: /index.html\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302HeaderWithLoginedCookie(DataOutputStream dos, int lengthOfBodyContent, boolean isLoginSuccess, String location) {
		try {
			dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("Location: " + location + "\r\n");

			if (isLoginSuccess) {
				dos.writeBytes("Set-Cookie: logined=true\r\n");
			} else {
				dos.writeBytes("Set-Cookie: logined=false\r\n");
			}

			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseCss200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
