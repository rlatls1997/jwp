package next.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.mvc.Controller;
import next.controller.UserSessionUtils;
import next.view.ModelAndView;

public class LogoutController implements Controller {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		HttpSession session = req.getSession();
		session.removeAttribute(UserSessionUtils.USER_SESSION_KEY);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/");

		return modelAndView;
	}
}
