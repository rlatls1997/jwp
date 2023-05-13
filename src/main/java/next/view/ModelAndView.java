package next.view;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ModelAndView {
	private final View view;
	private final Map<String, Object> model = new HashMap<>();

	public ModelAndView(View view) {
		this.view = view;
	}

	public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.view.render(model, request, response);
	}

	public ModelAndView setModelAttribute(String name, Object object) {
		this.model.put(name, object);

		return this;
	}
}
