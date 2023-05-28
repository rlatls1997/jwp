package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.dao.QuestionDao;
import next.model.Question;
import next.model.User;

public class QnaUpdateFormController extends AbstractController {

	private final QuestionDao questionDao = new QuestionDao();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		if (!UserSessionUtils.isLogined(session)) {
			return jspView("redirect:/users/loginForm");
		}

		User user = UserSessionUtils.getUserFromSession(session);
		String userName = user.getName();

		long questionId = Long.parseLong(request.getParameter("questionId"));
		Question question = questionDao.findById(questionId);

		if (userName.equals(question.getWriter())) {
			return jspView("/qna/updateForm.jsp")
				.addObject("question", question);
		}

		return jspView("redirect:/");
	}
}
