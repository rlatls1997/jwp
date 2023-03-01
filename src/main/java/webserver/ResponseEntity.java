package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ResponseEntity {
	private final String protocol;
	private final int statusCode;
	private final String reasonPhrase;
	private final String contentType;
	private final int contentLength;
	private final String location;
	private final Map<String, String> cookies;
	private final byte[] body;

	private ResponseEntity(String protocol, int statusCode, String reasonPhrase, String contentType, int contentLength, String location, Map<String, String> cookies, byte[] body) {
		this.protocol = protocol;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
		this.contentType = contentType;
		this.contentLength = contentLength;
		this.location = location;
		this.cookies = cookies;
		this.body = body;
	}

	public static ContentType response200() {
		return builder()
			.protocol("HTTP/1.1")
			.statusCode(200)
			.reasonPhrase("OK");
	}

	public static HttpResponseHeader response200Html() {
		return builder()
			.protocol("HTTP/1.1")
			.statusCode(200)
			.reasonPhrase("OK")
			.contentType("text/html;charset=utf-8");
	}

	public static HttpResponseHeader response302Html() {
		return builder()
			.protocol("HTTP/1.1")
			.statusCode(302)
			.reasonPhrase("FOUND")
			.contentType("text/html;charset=utf-8");
	}

	public void response(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeBytes(protocol + " " + statusCode + " " + reasonPhrase + " \r\n");
		dataOutputStream.writeBytes("Content-Type: " + contentType + "\r\n");
		dataOutputStream.writeBytes("Content-Length: " + contentLength + "\r\n");

		if (Objects.nonNull(location) && !location.isBlank()) {
			dataOutputStream.writeBytes("Location: " + location + "\r\n");
		}

		setCookiesResponseHeader(dataOutputStream);
		dataOutputStream.writeBytes("\r\n");

		dataOutputStream.write(body, 0, body.length);
		dataOutputStream.flush();
	}

	private void setCookiesResponseHeader(DataOutputStream dataOutputStream) throws IOException {
		Set<String> cookieKeys = cookies.keySet();

		if (!cookieKeys.isEmpty()) {
			dataOutputStream.writeBytes("Set-Cookie: ");

			for (String key : cookieKeys) {
				dataOutputStream.writeBytes(key + "=" + cookies.get(key) + "; ");
			}

			dataOutputStream.writeBytes("path=/;");
			dataOutputStream.writeBytes("\r\n");
		}
	}

	public static Protocol builder() {
		return new Builder();
	}

	public interface Protocol {
		StatusCode protocol(String protocol);
	}

	public interface StatusCode {
		ReasonPhrase statusCode(int statusCode);
	}

	public interface ReasonPhrase {
		ContentType reasonPhrase(String reasonPhrase);
	}

	public interface ContentType {
		HttpResponseHeader contentType(String contentType);
	}

	public interface HttpResponseHeader {

		HttpResponseHeader location(String location);

		HttpResponseHeader addCookie(String key, String value);

		HttpResponseHeader body(byte[] body);

		ResponseEntity build();
	}

	public static class Builder implements Protocol, StatusCode, ReasonPhrase, ContentType, HttpResponseHeader {
		private String protocol;
		private int statusCode;
		private String reasonPhrase;
		private String contentType;
		private String location;
		private final Map<String, String> cookies = new HashMap<>();
		private byte[] body = new byte[0];

		@Override
		public StatusCode protocol(String protocol) {
			this.protocol = protocol;
			return this;
		}

		@Override
		public ReasonPhrase statusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		@Override
		public ContentType reasonPhrase(String reasonPhrase) {
			this.reasonPhrase = reasonPhrase;
			return this;
		}

		@Override
		public HttpResponseHeader contentType(String contentType) {
			this.contentType = contentType;
			return this;
		}

		@Override
		public HttpResponseHeader location(String location) {
			this.location = location;
			return this;
		}

		@Override
		public HttpResponseHeader addCookie(String key, String value) {
			this.cookies.put(key, value);
			return this;
		}

		@Override
		public HttpResponseHeader body(byte[] body) {
			this.body = body;
			return this;
		}

		@Override
		public ResponseEntity build() {
			return from(this);
		}
	}

	private static ResponseEntity from(Builder builder) {
		String protocol = builder.protocol;
		int statusCode = builder.statusCode;
		String reasonPhrase = builder.reasonPhrase;
		String contentType = builder.contentType;
		int contentLength = builder.body.length;
		String location = builder.location;
		Map<String, String> cookies = builder.cookies;
		byte[] body = builder.body;

		return new ResponseEntity(protocol, statusCode, reasonPhrase, contentType, contentLength, location, cookies, body);
	}
}
