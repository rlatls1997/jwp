package next.web;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import core.db.DataBase;
import next.model.User;

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/user/login.jsp");
		requestDispatcher.forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userId");
		String password = request.getParameter("password");

		User user = DataBase.findUserById(userId);

		if (Objects.isNull(user)) {
			throw new NoSuchElementException("update user not exist. userId:" + userId);
		}

		if (password.equals(user.getPassword())) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			response.sendRedirect("/user/list");
			return;
		}

		throw new IllegalArgumentException("wrong login password. userId:" + userId + ",password:" + password);
	}
}
