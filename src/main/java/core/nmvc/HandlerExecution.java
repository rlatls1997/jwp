package core.nmvc;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import core.mvc.ModelAndView;

public class HandlerExecution {

	private final Object controller;
	private final Method method;

	public HandlerExecution(Object controller, Method method) {
		this.controller = controller;
		this.method = method;
	}

	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return (ModelAndView)method.invoke(controller, request, response);
	}

	public static HandlerExecution from(Object controller, Method method) {
		return new HandlerExecution(controller, method);
	}
}
