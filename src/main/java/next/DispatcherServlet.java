package next;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.controller.Controller;
import next.controller.CreateUserController;
import next.controller.ForwardController;
import next.controller.HomeController;
import next.controller.ListUserController;
import next.controller.LoginController;
import next.controller.LogoutController;
import next.controller.ProfileController;
import next.controller.UpdateUserController;
import next.controller.UpdateUserFormController;

// 서블릿은 최초 요청이 들어올 때 초기화된다. 따라서 맨 처음 호출하게 되는 유처는 응답까지 오랜 시간이 걸린다.
// loadOnStartup은 0보다 큰 값으로 설정할 때 서버가 시작될 때 서블릿을 초기화한다. 값은 우선순위를 의미한다(가장 낮은 수가 가장 먼저 초기화됨)
@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestUri = request.getRequestURI();

		Controller controller = RequestMapping.mapController(requestUri);
		String command = controller.execute(request, response);

		if (isRedirect(command)) {
			String location = getRedirectLocation(command);
			response.sendRedirect(location);
			return;
		}

		forward(command, request, response);
	}

	private boolean isRedirect(String command) {
		return command.startsWith("redirect:");
	}

	private String getRedirectLocation(String command) {
		return command.substring(command.indexOf(":") + 1);
	}

	private void forward(String forwardUrl, HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		RequestDispatcher rd = req.getRequestDispatcher(forwardUrl);
		rd.forward(req, resp);
	}
}
