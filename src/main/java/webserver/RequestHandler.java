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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import user.UserProcessor;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private static final int START_LINE = 0;

	private Socket connection;

	private UserProcessor userProcessor;

	public RequestHandler(Socket connectionSocket, UserProcessor userProcessor) {
		this.connection = connectionSocket;
		this.userProcessor = userProcessor;
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
			String httpRequestUrl = getHttpRequestUrl(httpRequestStartLine);
			int contentLength = getContentLength(httpRequestContents);
			String httpRequestBodyString = IOUtils.readData(br, contentLength);

			DataOutputStream dos = new DataOutputStream(out);

			// 회원가입
			if(httpRequestUrl.startsWith("/user/create")){
				Map<String, String> httpRequestBodyMap = HttpRequestUtils.parseRequestBody(httpRequestBodyString);
				userProcessor.createUser(httpRequestBodyMap);
			}else{
				byte[] body = Files.readAllBytes(new File("./webapp" + httpRequestUrl).toPath());
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

	private String getHttpQueryString(String httpQueryString){
		int queryStringStartIndex = httpQueryString.indexOf("?") + 1;
		return httpQueryString.substring(queryStringStartIndex);
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
