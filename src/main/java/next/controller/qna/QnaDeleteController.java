package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.model.User;
import next.service.qna.QnaService;

public class QnaDeleteController extends AbstractController {

	private final QnaService qnaService = new QnaService();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		if (!UserSessionUtils.isLogined(session)) {
			return jspView("redirect:/users/loginForm");
		}

		User user = UserSessionUtils.getUserFromSession(session);
		String userName = user.getName();

		long questionId = Long.parseLong(request.getParameter("questionId"));

		qnaService.deleteQuestion(questionId, userName);
		return jspView("redirect:/");
	}
}
