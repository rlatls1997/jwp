package webserver;

public class DefaultController extends AbstractController {

	@Override
	public void doGet(RequestEntity requestEntity, ResponseEntity responseEntity) {
		String path = requestEntity.getPath();

		if (path.equals("/") || path.isBlank()) {
			responseEntity.forward("/index.html");
			return;
		}

		responseEntity.forward(path);
	}
}
