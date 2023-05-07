package next.controller;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import core.mvc.Controller;
import next.dao.AnswerDao;
import next.model.Result;

public class DeleteAnswerController implements Controller {

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		AnswerDao answerDao = new AnswerDao();

		long answerId = Long.parseLong(req.getParameter("answerId"));
		boolean isDeleted = answerDao.delete(answerId);

		Result result = isDeleted? Result.ok():Result.fail("fail to delete answer.");

		ObjectMapper mapper = new ObjectMapper();
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();
		out.print(mapper.writeValueAsString(result));
		return null;
	}
}
