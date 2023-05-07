package next.controller.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import next.dao.UserDao;
import next.model.User;
import next.view.ModelAndView;

public class ProfileController implements Controller {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		String userId = req.getParameter("userId");
		UserDao userDao = new UserDao();
		User user = userDao.findByUserId(userId);
		if (user == null) {
			throw new NullPointerException("사용자를 찾을 수 없습니다.");
		}
		req.setAttribute("user", user);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setModelAttribute("user", user);
		modelAndView.setViewName("/user/profile.jsp");

		return modelAndView;
	}
}
