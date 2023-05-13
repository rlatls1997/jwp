package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import next.dao.AnswerDao;
import next.model.Result;
import next.view.ModelAndView;

public class DeleteAnswerController extends AbstractController {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Long answerId = Long.parseLong(req.getParameter("answerId"));
		AnswerDao answerDao = new AnswerDao();

		answerDao.delete(answerId);

		return jsonView()
			.setModelAttribute("result", Result.ok());
	}
}
