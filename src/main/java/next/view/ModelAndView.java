package next.view;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModelAndView {
	private String viewName;
	private Map<String, Object> model = new HashMap<>();

	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		View view = viewName.isBlank() ? new JsonView() : new JspView(viewName);
		view.render(model, request, response);
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public void setModelAttribute(String name, Object object) {
		this.model.put(name, object);
	}
}
