package webserver;

public class AbstractController implements Controller {
	@Override
	public void service(RequestEntity requestEntity, ResponseEntity responseEntity) {
		HttpMethod method = requestEntity.getMethod();

		if (method == HttpMethod.GET) {
			doGet(requestEntity, responseEntity);
			return;
		}

		if (method == HttpMethod.POST) {
			doPost(requestEntity, responseEntity);
			return;
		}

		throw new UnsupportedOperationException("Request method isn't allowed. method:" + method);
	}

	public void doGet(RequestEntity requestEntity, ResponseEntity responseEntity) {
	}

	public  void doPost(RequestEntity requestEntity, ResponseEntity responseEntity) {
	}
}
