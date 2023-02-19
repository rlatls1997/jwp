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

import auth.AuthProcessor;
import user.UserProcessor;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private static final int START_LINE = 0;

	private Socket connection;

	private final UserProcessor userProcessor;
	private final AuthProcessor authProcessor;

	public RequestHandler(Socket connectionSocket, UserProcessor userProcessor, AuthProcessor authProcessor) {
		this.connection = connectionSocket;
		this.userProcessor = userProcessor;
		this.authProcessor = authProcessor;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
			connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			List<String> httpRequestContents = getHttpRequestContents(br);
			String httpRequestStartLine = httpRequestContents.get(START_LINE);
			String httpRequestPath = getHttpRequestPath(httpRequestStartLine);
			String httpRequestQueryString = getHttpQueryString(httpRequestStartLine);
			String httpCookie = getCookie(httpRequestContents);
			int contentLength = getContentLength(httpRequestContents);
			String httpRequestBodyString = IOUtils.readData(br, contentLength);
			Map<String, String> httpRequestBodyMap = HttpRequestUtils.parseRequestBody(httpRequestBodyString);

			DataOutputStream dos = new DataOutputStream(out);

			if (httpRequestPath.equals("/user/create")) { // 회원가입
				userProcessor.createUser(httpRequestBodyMap);

				byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
				response302HeaderLocationIndexHtml(dos, body.length);
				responseBody(dos, body);
			} else if (httpRequestPath.equals("/user/login")) { // 로그인
				boolean isLoginSuccess = authProcessor.login(httpRequestBodyMap);

				if (isLoginSuccess) {
					byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
					response302HeaderWithLoginedCookie(dos, body.length, true, "/index.html");
					responseBody(dos, body);
				} else {
					byte[] body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
					response302HeaderWithLoginedCookie(dos, body.length, false, "/user/login_failed.html");
					responseBody(dos, body);
				}
			} else {
				byte[] body = Files.readAllBytes(new File("./webapp" + httpRequestPath).toPath());
				response200Header(dos, body.length);
				responseBody(dos, body);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
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

	private String getCookie(List<String> httpRequestContents){
		for (String httpRequestContent : httpRequestContents) {
			if (httpRequestContent.startsWith("Cookie")) {
				int cookieValueStartIndex = httpRequestContent.indexOf(":") + 1;

				return httpRequestContent.substring(cookieValueStartIndex).trim();
			}
		}

		return "";
	}

	private String getHttpRequestPath(String httpRequestStartLine) {
		String httpRequestUrl = getHttpRequestUrl(httpRequestStartLine);

		int queryStringStartIndex = httpRequestUrl.indexOf("?");

		if (queryStringStartIndex == -1) {
			return httpRequestUrl;
		}

		return httpRequestUrl.substring(0, queryStringStartIndex);
	}

	private String getHttpQueryString(String httpRequestStartLine) {
		String httpRequestUrl = getHttpRequestUrl(httpRequestStartLine);

		int queryStringStartIndex = httpRequestUrl.indexOf("?");

		if (queryStringStartIndex == -1) {
			return "";
		}

		return httpRequestUrl.substring(queryStringStartIndex + 1);
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
