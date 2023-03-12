package webserver;

public interface Controller {
	void service(RequestEntity requestEntity, ResponseEntity responseEntity);
}
