package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.view.ModelAndView;

public class ShowController implements Controller {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		long questionId = Long.parseLong(req.getParameter("questionId"));
		QuestionDao questionDao = new QuestionDao();
		AnswerDao answerDao = new AnswerDao();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("/qna/show.jsp");
		modelAndView.setModelAttribute("question", questionDao.findById(questionId));
		modelAndView.setModelAttribute("answers", answerDao.findAllByQuestionId(questionId));

		return modelAndView;
	}
}
