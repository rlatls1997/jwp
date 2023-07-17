package core.di.factory;

import java.lang.reflect.Method;
import java.util.Set;

import core.di.factory.config.AnnotatedBeanDefinition;
import core.di.factory.config.BeanDefinition;
import core.di.factory.config.DefaultBeanDefinition;
import core.di.factory.support.BeanDefinitionReader;
import core.di.factory.support.BeanDefinitionRegistry;

public class AnnotatedBeanDefinitionReader implements BeanDefinitionReader {

	private final BeanDefinitionRegistry beanDefinitionRegistry;

	public AnnotatedBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
		this.beanDefinitionRegistry = beanDefinitionRegistry;
	}

	@Override
	public void loadBeanDefinitions(Class<?>... annotatedClasses) {
		for (Class<?> clazz : annotatedClasses) {
			loadBeanDefinition(clazz);
		}
	}


	public void loadBeanDefinition(Class<?> clazz) {
		beanDefinitionRegistry.registerBeanDefinition(clazz, new DefaultBeanDefinition(clazz));
		Set<Method> methodSet = BeanFactoryUtils.getBeanMethods(clazz);

		for (Method method : methodSet) {
			Class<?> methodType = method.getReturnType();
			BeanDefinition annotatedBeanDefinition = new AnnotatedBeanDefinition(methodType, method);
			beanDefinitionRegistry.registerBeanDefinition(methodType, annotatedBeanDefinition);
		}
	}
}
