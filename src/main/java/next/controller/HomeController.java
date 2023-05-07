package next.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.Controller;
import next.dao.QuestionDao;
import next.view.JspView;
import next.view.ModelAndView;
import next.view.View;

public class HomeController implements Controller {
	@Override
	public ModelAndView execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		QuestionDao questionDao = new QuestionDao();

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setModelAttribute("questions", questionDao.findAll());
		modelAndView.setViewName("home.jsp");

		return modelAndView;
	}
}
