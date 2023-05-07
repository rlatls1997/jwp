package core.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import next.view.JspView;
import next.view.ModelAndView;

public class ForwardController implements Controller {
	private String forwardUrl;

	public ForwardController(String forwardUrl) {
		this.forwardUrl = forwardUrl;
		if (forwardUrl == null) {
			throw new NullPointerException("forwardUrl is null. 이동할 URL을 입력하세요.");
		}
	}

	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(forwardUrl);

		return modelAndView;
	}
}
