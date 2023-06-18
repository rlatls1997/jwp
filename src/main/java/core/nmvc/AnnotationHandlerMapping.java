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

	private ControllerScanner controllerScanner = new ControllerScanner();

	public AnnotationHandlerMapping(Object... basePackage) {
		this.basePackage = basePackage;
	}

	@Override
	public void initialize() {
		controllerScanner.instantiateControllers(basePackage);
		Set<Class<?>> controllers = controllerScanner.getControllers();

		for (Class<?> controller : controllers) {
			initHandlerExecutions(controller);
		}
	}

	private HandlerKey createHandlerKey(RequestMapping requestMapping) {
		return new HandlerKey(requestMapping.value(), requestMapping.method());
	}

	private void initHandlerExecutions(Class<?> controller) {
		Set<Method> methods = ReflectionUtils.getAllMethods(controller, ReflectionUtils.withAnnotation(RequestMapping.class));

		for (Method method : methods) {
			RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

			try {
				Object controllerInstance = controllerScanner.getControllerInstance(controller);
				handlerExecutions.put(createHandlerKey(requestMapping), HandlerExecution.from(controllerInstance, method));
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
