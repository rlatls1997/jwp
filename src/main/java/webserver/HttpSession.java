package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpSession {

	private final String id;
	private final Map<String, Object> sessionData = new HashMap<>();

	public HttpSession() {
		this.id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setAttribute(String name, Object value) {
		sessionData.put(name, value);
	}

	public Object getAttribute(String name) {
		return sessionData.get(name);
	}

	public void removeAttribute(String name) {
		sessionData.remove(name);
	}

	public void invalidate() {
		sessionData.clear();
	}
}
