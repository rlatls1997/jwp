package core.nmvc;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.reflections.ReflectionUtils;

import com.google.common.collect.Maps;

import core.annotation.RequestMapping;
import core.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {
	private Object[] basePackage;

	private Map<HandlerKey, HandlerExecution> handlerExecutions = Maps.newHashMap();

	private ControllerScanner controllerScanner;

	public AnnotationHandlerMapping(Object... basePackage) {
		this.basePackage = basePackage;
		this.controllerScanner = new ControllerScanner(basePackage);
	}

	@Override
	public void initialize() {
		Map<Class<?>, Object> controllers = controllerScanner.getControllers();

		for (Map.Entry<Class<?>, Object> controllerEntry : controllers.entrySet()) {
			initHandlerExecutions(controllerEntry);
		}
	}

	private HandlerKey createHandlerKey(RequestMapping requestMapping) {
		return new HandlerKey(requestMapping.value(), requestMapping.method());
	}

	private void initHandlerExecutions(Map.Entry<Class<?>, Object> controllerEntry) {
		Set<Method> methods = ReflectionUtils.getAllMethods(controllerEntry.getKey(), ReflectionUtils.withAnnotation(RequestMapping.class));

		for (Method method : methods) {
			RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

			try {
				handlerExecutions.put(createHandlerKey(requestMapping), HandlerExecution.from(controllerEntry.getValue(), method));
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	public HandlerExecution getHandler(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		RequestMethod rm = RequestMethod.valueOf(request.getMethod().toUpperCase());
		return handlerExecutions.get(new HandlerKey(requestUri, rm));
	}
}
