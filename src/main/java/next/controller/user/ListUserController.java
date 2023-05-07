package next.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import next.controller.UserSessionUtils;
import next.dao.UserDao;
import next.view.ModelAndView;

public class ListUserController implements Controller {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		ModelAndView modelAndView = new ModelAndView();

		if (!UserSessionUtils.isLogined(req.getSession())) {
			modelAndView.setViewName("redirect:/users/loginForm");
			return modelAndView;
		}

		UserDao userDao = new UserDao();

		modelAndView.setModelAttribute("users", userDao.findAll());
		modelAndView.setViewName("/user/list.jsp");

		return modelAndView;
	}
}
