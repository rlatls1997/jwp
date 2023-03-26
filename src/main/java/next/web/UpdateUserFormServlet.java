package next.web;

import java.io.IOException;
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

@WebServlet("/user/update")
public class UpdateUserFormServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("userId");
		request.setAttribute("user", DataBase.findUserById(userId));
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/user/update.jsp");
		requestDispatcher.forward(request, response);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Object sessionUser = session.getAttribute("user");

		if (Objects.isNull(sessionUser)) {
			response.sendRedirect("/user/login");
			return;
		}

		String targetUserId = request.getParameter("userId");

		User castedSessionUser = (User)sessionUser;
		String sessionUserId = castedSessionUser.getUserId();

		if (targetUserId.equals(sessionUserId)) {
			User user = new User(targetUserId, request.getParameter("password"), request.getParameter("name"), request.getParameter("email"));
			DataBase.updateUser(user);
			response.sendRedirect("/user/list");
			return;
		}

		throw new IllegalStateException("session user isn't equals with user to update. sessionUserId:" + sessionUserId + ",castedUserId:" + castedSessionUser);
	}
}
