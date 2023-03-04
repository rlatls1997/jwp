package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class ResponseEntity {
	private final Map<String, String> headers = new HashMap<>();
	private final DataOutputStream dataOutputStream;

	public ResponseEntity(DataOutputStream dataOutputStream) {
		this.dataOutputStream = dataOutputStream;
	}

	public void addHeader(String key, String value) {
		this.headers.put(key, value);
	}

	public void forward(String filePath) {
		try {
			dataOutputStream.writeBytes("HTTP/1.1 200 OK \r\n");

			byte[] body = Files.readAllBytes(new File("./webapp" + filePath).toPath());
			int contentLength = body.length;

			dataOutputStream.writeBytes("Content-Type: " + getContentType(filePath) + ";charset=utf-8\r\n");
			dataOutputStream.writeBytes("Content-Length: " + contentLength + "\r\n");

			writeHeaders();

			dataOutputStream.writeBytes("\r\n");
			dataOutputStream.write(body, 0, body.length);
			dataOutputStream.flush();
		} catch (IOException exception) {
			throw new IllegalStateException("Failed to forward.");
		}
	}

	public void forward(byte[] body) {
		try {
			dataOutputStream.writeBytes("HTTP/1.1 200 OK \r\n");

			int contentLength = body.length;

			dataOutputStream.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dataOutputStream.writeBytes("Content-Length: " + contentLength + "\r\n");

			writeHeaders();

			dataOutputStream.writeBytes("\r\n");
			dataOutputStream.write(body, 0, body.length);
			dataOutputStream.flush();
		} catch (IOException exception) {
			throw new IllegalStateException("Failed to forward.");
		}
	}

	public void sendRedirect(String filePath) {
		try {
			dataOutputStream.writeBytes("HTTP/1.1 302 FOUND \r\n");

			byte[] body = Files.readAllBytes(new File("./webapp" + filePath).toPath());
			int contentLength = body.length;

			dataOutputStream.writeBytes("Content-Type: " + getContentType(filePath) + ";charset=utf-8\r\n");
			dataOutputStream.writeBytes("Content-Length: " + contentLength + "\r\n");
			dataOutputStream.writeBytes("Location: " + filePath + "\r\n");

			writeHeaders();

			dataOutputStream.writeBytes("\r\n");
			dataOutputStream.write(body, 0, body.length);
			dataOutputStream.flush();
		} catch (IOException exception) {
			throw new IllegalStateException("Failed to send redirect.");
		}
	}

	private void writeHeaders() throws IOException {
		for (Map.Entry<String, String> header : headers.entrySet()) {
			String key = header.getKey();
			String value = header.getValue();

			dataOutputStream.writeBytes(key + ": " + value + "\r\n");
		}
	}

	private String getContentType(String filePath) {
		int extensionDelimiterIndex = filePath.lastIndexOf(".");

		if (extensionDelimiterIndex == -1) {
			return "text/plain";
		}

		return "text/" + filePath.substring(extensionDelimiterIndex + 1);
	}
}
