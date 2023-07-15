package core.di.factory;

import java.util.Arrays;
import java.util.Set;

import com.google.common.collect.Sets;

import core.annotation.ComponentScan;

public class ApplicationConfigApplicationContext implements ApplicationContext {

	private final BeanFactory beanFactory;

	public ApplicationConfigApplicationContext(Class<?>... configClass) {
		Object[] basePackages = findBasePackages(configClass);
		beanFactory = new BeanFactory();

		AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);
		annotatedBeanDefinitionReader.register(configClass);

		ClasspathBeanDefinitionScanner classpathBeanDefinitionScanner = new ClasspathBeanDefinitionScanner(beanFactory);
		classpathBeanDefinitionScanner.doScan(basePackages);

		beanFactory.initialize();
	}

	public Object[] findBasePackages(Class<?>[] classes) {
		Set<String> basePackages = Sets.newHashSet();

		for (Class<?> clazz : classes) {
			ComponentScan componentScan = clazz.getAnnotation(ComponentScan.class);
			basePackages.addAll(Arrays.asList(componentScan.basePackages()));
		}

		return basePackages.toArray(new String[0]);
	}

	public <T> T getBean(Class<T> clazz) {
		return beanFactory.getBean(clazz);
	}

	public Set<Class<?>> getBeanClasses() {
		return beanFactory.getBeanClasses();
	}
}
