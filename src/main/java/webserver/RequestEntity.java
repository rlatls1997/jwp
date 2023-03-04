package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import util.HttpRequestUtils;

public class RequestEntity {
	private final String method;
	private final String path;
	private final String protocol;
	private final Map<String, String> headers;
	private final Map<String, String> parameters;
	private final Map<String, String> cookies;

	public RequestEntity(String method, String path, String protocol, Map<String, String> headers, Map<String, String> parameters, Map<String, String> cookies) {
		this.method = method;
		this.path = path;
		this.protocol = protocol;
		this.headers = headers;
		this.parameters = parameters;
		this.cookies = cookies;
	}

	public static RequestEntity from(InputStreamReader inputStreamReader) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		List<String> httpRequestContents = HttpRequestUtils.getHttpRequestContents(bufferedReader);

		String[] startLineElements = httpRequestContents.get(0).split(" ");

		String method = HttpRequestUtils.getRequestMethod(startLineElements);
		String path = HttpRequestUtils.getRequestPath(startLineElements);
		String protocol = HttpRequestUtils.getRequestProtocol(startLineElements);

		Map<String, String> headers = HttpRequestUtils.getHeaders(httpRequestContents);
		Map<String, String> parameters = HttpRequestUtils.getParameters(bufferedReader, startLineElements, headers);
		Map<String, String> cookies = HttpRequestUtils.parseCookies(headers.get("Cookie"));

		return new RequestEntity(method, path, protocol, headers, parameters, cookies);
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getHeader(String header) {
		return headers.get(header);
	}

	public String getParameter(String parameter) {
		return parameters.get(parameter);
	}

	public String getCookie(String cookie) {
		return cookies.get(cookie);
	}
}
