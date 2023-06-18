package core.nmvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import core.annotation.Controller;

public class ControllerScanner {

	private final Map<Class<?>, Object> controllers = new HashMap<>();

	public Set<Class<?>> getControllers() {
		return controllers.keySet();
	}

	public Object getControllerInstance(Class<?> controller) {
		return controllers.get(controller);
	}

	public void instantiateControllers(Object[] basePackage) {
		Reflections reflections = new Reflections(basePackage);
		Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);

		for (Class<?> controller : controllers) {
			try {
				this.controllers.put(controller, controller.getConstructor().newInstance());
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}
}
