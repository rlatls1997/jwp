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
import core.nmvc.ControllerHandlerAdapter;
import core.nmvc.HandlerAdapter;
import core.nmvc.HandlerExecutionHandlerAdapter;
import core.nmvc.HandlerMapping;

@WebServlet(name = "dispatcher", urlPatterns = "/", loadOnStartup = 1)
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

	private final List<HandlerMapping> handlerMappings = new ArrayList<>();

	private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

	@Override
	public void init() {
		handlerMappings.add(new LegacyHandlerMapping());
		handlerMappings.add(new AnnotationHandlerMapping("next.controller"));

		handlerMappings.forEach(HandlerMapping::initialize);

		handlerAdapters.add(new ControllerHandlerAdapter());
		handlerAdapters.add(new HandlerExecutionHandlerAdapter());
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		String requestUri = request.getRequestURI();
		logger.debug("Method : {}, Request URI : {}", request.getMethod(), requestUri);

		Object handler = getHandler(request);
		ModelAndView mav;

		try {
			mav = execute(handler, request, resp);
			mav.getView().render(mav.getModel(), request, resp);
		} catch (Throwable exception) {
			logger.error("Exception : ", exception);
			throw new ServletException(exception.getMessage());
		}
	}

	private ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
		for (HandlerAdapter handlerAdapter : handlerAdapters) {
			if (handlerAdapter.supports(handler)) {
				return handlerAdapter.handle(request, response, handler);
			}
		}

		throw new IllegalStateException("DispatcherServlet execute fail. request:" + request);
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
