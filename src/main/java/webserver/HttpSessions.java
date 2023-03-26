package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpSessions {
	private static final Map<String, HttpSession> sessions = new HashMap<>();

	public static HttpSession getSession(RequestEntity requestEntity) {
		String sessionId = requestEntity.getCookie("JSESSION");
		return sessions.get(sessionId);
	}

	public static void addSession(HttpSession session) {
		String sessionId = session.getId();
		sessions.put(sessionId, session);
	}
}
