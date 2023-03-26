package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import user.UserController;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private static final String DEFAULT_CONTROLLER_KEY = "/";
	private static final Map<String, Controller> CONTROLLER_MAP = Map.of("/user", new UserController(), DEFAULT_CONTROLLER_KEY, new DefaultController());

	private static final String ROOT_PATH_GROUP_NAME = "rootPath";
	private static final Pattern ROOT_PATH_REGEX = Pattern.compile("(?<" + ROOT_PATH_GROUP_NAME + ">/[^/]*)");

	private static final String JSESSION = "JSESSION";

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

			Controller controller = getMappingController(requestPath);

			ResponseEntity responseEntity = new ResponseEntity(dataOutputStream);

			if (isJSessionIdNotExist(requestEntity)) {
				HttpSession httpSession = new HttpSession();
				responseEntity.addCookie(JSESSION, httpSession.getId());
				HttpSessions.addSession(httpSession);
			}

			controller.service(requestEntity, responseEntity);
		} catch (IOException ioException) {
			log.error(ioException.getMessage());
		}
	}

	private boolean isJSessionIdNotExist(RequestEntity requestEntity) {
		return Objects.isNull(requestEntity.getCookie(JSESSION));
	}

	private Controller getMappingController(String requestPath) {
		Controller defaultController = CONTROLLER_MAP.get(DEFAULT_CONTROLLER_KEY);
		Matcher matcher = ROOT_PATH_REGEX.matcher(requestPath);

		if (matcher.find()) {
			String rootPath = matcher.group(ROOT_PATH_GROUP_NAME);
			return CONTROLLER_MAP.getOrDefault(rootPath, defaultController);
		}

		return defaultController;
	}
}

