package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardController implements Controller {
	private final String resource;

	public ForwardController(String resource) {
		this.resource = resource;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return resource;
	}
}
