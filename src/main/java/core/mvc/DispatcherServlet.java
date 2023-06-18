package core.mvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.nmvc.AnnotationHandlerMapping;
import core.nmvc.HandlerExecution;
import core.nmvc.HandlerMapping;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

	private final List<HandlerMapping> handlerMappings = new ArrayList<>();

	@Override
	public void init() throws ServletException {
		handlerMappings.add(new LegacyHandlerMapping());
		handlerMappings.add(new AnnotationHandlerMapping("next.controller"));

		handlerMappings.forEach(HandlerMapping::initialize);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = request.getRequestURI();
		logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

		Object handler = getHandler(request);
		ModelAndView mav;

		try {
			if (handler instanceof Controller) {
				mav = ((Controller)handler).execute(request, resp);
			} else if (handler instanceof HandlerExecution) {
				mav = ((HandlerExecution)handler).handle(request, resp);
			} else {
				throw new IllegalStateException("handler is not allowed type.");
			}
		} catch (Throwable exception) {
			logger.error("Exception : ", exception);
			throw new ServletException(exception.getMessage());
		}

		View view = mav.getView();

		try {
			view.render(mav.getModel(), request, resp);
		} catch (Throwable exception) {
			logger.error("Exception : ", exception);
			throw new ServletException(exception.getMessage());
		}
	}

	private Object getHandler(HttpServletRequest request) {
		for (HandlerMapping handlerMapping : handlerMappings) {
			Object handler = handlerMapping.getHandler(request);

			if (Objects.nonNull(handler)) {
				return handler;
			}
		}

		throw new IllegalStateException("DispatcherServlet getHandler fail. request:" + request);
	}
}
