package next.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.mvc.Controller;
import next.controller.UserSessionUtils;
import next.dao.UserDao;
import next.model.User;
import next.view.ModelAndView;

public class LoginController implements Controller {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");
		UserDao userDao = new UserDao();
		User user = userDao.findByUserId(userId);

		ModelAndView modelAndView = new ModelAndView();

		if (user == null) {
			modelAndView.setModelAttribute("loginFailed", true);
			modelAndView.setViewName("/user/login.jsp");

			return modelAndView;
		}
		if (user.matchPassword(password)) {
			HttpSession session = req.getSession();
			session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);

			modelAndView.setViewName("redirect:/");

			return modelAndView;
		} else {
			modelAndView.setModelAttribute("loginFailed", true);
			modelAndView.setViewName("/user/login.jsp");

			return modelAndView;
		}
	}
}
