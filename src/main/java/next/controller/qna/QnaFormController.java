package next.controller.qna;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.model.User;

public class QnaFormController extends AbstractController {

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		User user = UserSessionUtils.getUserFromSession(session);

		if (Objects.isNull(user)) {
			return jspView("redirect:/users/loginForm");
		}

		ModelAndView modelAndView = jspView("/qna/form.jsp");
		modelAndView.addObject("writer", user.getName());

		return modelAndView;
	}
}
