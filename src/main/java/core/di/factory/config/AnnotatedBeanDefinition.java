package core.di.factory.config;

import java.lang.reflect.Method;

public class AnnotatedBeanDefinition extends DefaultBeanDefinition {
	private final Method method;

	public AnnotatedBeanDefinition(Class<?> clazz, Method method) {
		super(clazz);
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}
}
