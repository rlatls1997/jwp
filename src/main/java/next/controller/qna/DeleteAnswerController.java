package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.AbstractController;
import core.mvc.ModelAndView;
import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Question;

public class DeleteAnswerController extends AbstractController {
	private QuestionDao questionDao = new QuestionDao();
	private AnswerDao answerDao = new AnswerDao();

	@Override
	public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long answerId = Long.parseLong(request.getParameter("answerId"));
		long questionId = Long.parseLong(request.getParameter("questionId"));

		answerDao.delete(answerId);

		Question question = questionDao.findById(questionId);
		long countOfComment = question.getCountOfComment() - 1;

		questionDao.updateCountOfComment(countOfComment, questionId);

		return jsonView()
			.addObject("countOfComment", countOfComment);
	}
}
