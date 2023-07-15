package core.di.factory;

import java.lang.reflect.Method;
import java.util.Set;

public class AnnotatedBeanDefinitionReader {

	private final BeanDefinitionRegistry beanDefinitionRegistry;

	public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
		this.beanDefinitionRegistry = beanDefinitionRegistry;
	}

	public void register(Class<?>... classes) {
		for (Class<?> clazz : classes) {
			registerBean(clazz);
		}
	}

	public void registerBean(Class<?> clazz) {
		beanDefinitionRegistry.registerBeanDefinition(clazz, new BeanDefinition(clazz));
		Set<Method> methodSet = BeanFactoryUtils.getBeanMethods(clazz);

		for (Method method : methodSet) {
			Class<?> methodType = method.getReturnType();
			BeanDefinition annotatedBeanDefinition = new AnnotatedBeanDefinition(methodType, method);
			beanDefinitionRegistry.registerBeanDefinition(methodType, annotatedBeanDefinition);
		}
	}
}
