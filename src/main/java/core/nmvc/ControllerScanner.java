package core.nmvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import core.annotation.Controller;

public class ControllerScanner {

	private final Reflections reflections;

	public ControllerScanner(Object... basePackage) {
		this.reflections = new Reflections(basePackage);
	}

	public Map<Class<?>, Object> getControllers() {
		Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
		return instantiateControllers(controllerClasses);
	}

	public Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
		Map<Class<?>, Object> controllers = new HashMap<>();

		for (Class<?> controller : controllerClasses) {
			try {
				controllers.put(controller, controller.getConstructor().newInstance());
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		return controllers;
	}
}
