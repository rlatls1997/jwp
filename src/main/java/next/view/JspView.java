package next.view;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JspView implements View {

	private static final String DEFAULT_REDIRECT_PREFIX = "redirect:";

	private final String viewName;

	public JspView(String viewName) {
		this.viewName = viewName;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		setModel(model, request);
		move(viewName, request, response);
	}

	private void setModel(Map<String, ?> model, HttpServletRequest request) {
		for (String key : model.keySet()) {
			request.setAttribute(key, model.get(key));
		}
	}

	private void move(String viewName, HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		if (viewName.startsWith(DEFAULT_REDIRECT_PREFIX)) {
			resp.sendRedirect(viewName.substring(DEFAULT_REDIRECT_PREFIX.length()));
			return;
		}

		RequestDispatcher rd = req.getRequestDispatcher(viewName);
		rd.forward(req, resp);
	}
}
