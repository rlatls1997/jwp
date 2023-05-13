package core.mvc;

import next.view.JsonView;
import next.view.JspView;
import next.view.ModelAndView;

public abstract class AbstractController implements Controller {

	protected ModelAndView jspView(String forwardUrl) {
		return new ModelAndView(new JspView(forwardUrl));
	}

	protected ModelAndView jsonView() {
		return new ModelAndView(new JsonView());
	}
}
