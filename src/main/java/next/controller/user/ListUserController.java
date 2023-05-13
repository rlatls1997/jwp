package next.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import next.controller.UserSessionUtils;
import next.dao.UserDao;
import next.view.ModelAndView;

public class ListUserController extends AbstractController {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		if (!UserSessionUtils.isLogined(req.getSession())) {
			return jspView("redirect:/users/loginForm");
		}

		UserDao userDao = new UserDao();

		return jspView("/user/list.jsp")
			.setModelAttribute("users", userDao.findAll());
	}
}
