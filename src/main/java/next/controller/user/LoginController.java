package next.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.mvc.AbstractController;
import next.controller.UserSessionUtils;
import next.dao.UserDao;
import next.model.User;
import next.view.ModelAndView;

public class LoginController extends AbstractController {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String userId = req.getParameter("userId");
		String password = req.getParameter("password");
		UserDao userDao = new UserDao();
		User user = userDao.findByUserId(userId);

		if (user == null) {
			return jspView("/user/login.jsp")
				.setModelAttribute("loginFailed", true);
		}
		if (user.matchPassword(password)) {
			HttpSession session = req.getSession();
			session.setAttribute(UserSessionUtils.USER_SESSION_KEY, user);

			return jspView("redirect:/");
		} else {

			return jspView("/user/login.jsp")
				.setModelAttribute("loginFailed", true);
		}
	}
}
