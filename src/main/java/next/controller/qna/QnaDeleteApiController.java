package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.controller.UserSessionUtils;
import next.model.Result;
import next.model.User;
import next.service.qna.QnaService;

public class QnaDeleteApiController extends AbstractController {

	private final QnaService qnaService = new QnaService();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();

		if (!UserSessionUtils.isLogined(session)) {
			return jsonView().addObject("result", Result.fail("not logined"));
		}

		User user = UserSessionUtils.getUserFromSession(session);
		String userName = user.getName();

		long questionId = Long.parseLong(request.getParameter("questionId"));

		boolean isDeleted = qnaService.deleteQuestion(questionId, userName);

		if (isDeleted) {
			return jsonView().addObject("result", Result.ok());
		}

		return jsonView().addObject("result", Result.fail("delete question fail."));
	}
}
