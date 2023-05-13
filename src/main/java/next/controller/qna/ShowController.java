package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.view.ModelAndView;

public class ShowController extends AbstractController {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		long questionId = Long.parseLong(req.getParameter("questionId"));
		QuestionDao questionDao = new QuestionDao();
		AnswerDao answerDao = new AnswerDao();

		return jspView("/qna/show.jsp")
			.setModelAttribute("question", questionDao.findById(questionId))
			.setModelAttribute("answers", answerDao.findAllByQuestionId(questionId));
	}
}
