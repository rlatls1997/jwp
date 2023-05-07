package next.controller.qna;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import next.dao.AnswerDao;
import next.model.Result;
import next.view.ModelAndView;

public class DeleteAnswerController implements Controller {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		Long answerId = Long.parseLong(req.getParameter("answerId"));
		AnswerDao answerDao = new AnswerDao();

		answerDao.delete(answerId);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setModelAttribute("result", Result.ok());

		return modelAndView;
	}
}
