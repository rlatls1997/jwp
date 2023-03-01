package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import util.HttpRequestUtils;
import util.IOUtils;

public class RequestEntity {
	private final String method;
	private final String path;
	private final Map<String, String> body;
	private final Map<String, String> queryStrings;
	private final Map<String, String> cookies;

	public RequestEntity(String method, String path, Map<String, String> body, Map<String, String> queryStrings, Map<String, String> cookies) {
		this.method = method;
		this.path = path;
		this.body = body;
		this.queryStrings = queryStrings;
		this.cookies = cookies;
	}

	public static RequestEntity from(BufferedReader bufferedReader) throws IOException {
		List<String> httpRequestContents = HttpRequestUtils.getHttpRequestContents(bufferedReader);

		String[] startLines = httpRequestContents.get(0)
			.split(" ");

		String method = startLines[0];
		String requestUrl = startLines[1];

		int queryStringStartIndex = requestUrl.indexOf("?");

		String path;
		Map<String, String> queryStrings;

		if (queryStringStartIndex == -1) {
			path = requestUrl;
			queryStrings = Collections.emptyMap();
		} else {
			path = requestUrl.substring(0, queryStringStartIndex);
			String wholeQueryString = requestUrl.substring(queryStringStartIndex);
			queryStrings = HttpRequestUtils.parseQueryString(wholeQueryString);
		}

		Map<String, String> body = Collections.emptyMap();
		Map<String, String> cookies = Collections.emptyMap();

		for (String httpRequestContent : httpRequestContents) {
			HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(httpRequestContent);

			if(Objects.isNull(pair)){
				continue;
			}

			String headerName = pair.getKey();
			String headerValue = pair.getValue();

			if (headerName.equals("Content-Length")) {
				String bodyString = IOUtils.readData(bufferedReader, Integer.parseInt(headerValue));
				body = HttpRequestUtils.parseRequestBody(bodyString);
			}

			if (headerName.equals("Cookie")) {
				cookies = HttpRequestUtils.parseCookies(headerValue);
			}
		}

		return new RequestEntity(method, path, body, queryStrings, cookies);
	}

	public String getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public Map<String, String> getBody() {
		return body;
	}

	public Map<String, String> getQueryStrings() {
		return queryStrings;
	}

	public Map<String, String> getCookies() {
		return cookies;
	}
}
