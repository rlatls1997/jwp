package webserver;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum HttpMethod {
	GET,
	POST;

	private static final Map<String, HttpMethod> httpMethodMap;

	static {
		httpMethodMap = Arrays.stream(HttpMethod.values())
			.collect(Collectors.toMap(Enum::name, httpMethod -> httpMethod));
	}

	public static HttpMethod from(String httpMethod) {
		return httpMethodMap.get(httpMethod);
	}
}
