package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import next.dao.AnswerDao;
import next.dao.QuestionDao;

public class QnaShowController implements Controller {
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		long questionId = Long.parseLong(req.getParameter("questionId"));

		QuestionDao questionDao = new QuestionDao();
		AnswerDao answerDao = new AnswerDao();

		req.setAttribute("question", questionDao.findOne(questionId));
		req.setAttribute("answers", answerDao.findByQuestionId(questionId));
		return "/qna/show.jsp";
	}
}
