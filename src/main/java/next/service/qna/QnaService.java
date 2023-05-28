package next.service.qna;

import next.dao.AnswerDao;
import next.dao.QuestionDao;
import next.model.Question;

public class QnaService {

	private final QuestionDao questionDao = new QuestionDao();

	private final AnswerDao answerDao = new AnswerDao();

	public boolean deleteQuestion(long questionId, String userName) {
		Question question = questionDao.findById(questionId);

		if (!question.getWriter().equals(userName)) {
			return false;
		}

		if (question.getCountOfComment() == 0) {
			questionDao.delete(questionId);
			return true;
		}

		long countOfAnswer = answerDao.countOfAnswersByQuestionIdAndWriter(questionId, userName);

		if (countOfAnswer == 0) {
			questionDao.delete(questionId);
			return true;
		}

		return false;
	}
}
