package next.web;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/user/logout")
public class LogoutServlet extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		Object sessionUser = session.getAttribute("user");

		if (Objects.isNull(sessionUser)) {
			throw new IllegalStateException("logout user not logined.");
		}

		session.invalidate();
		response.sendRedirect("/user/list");
	}
}
