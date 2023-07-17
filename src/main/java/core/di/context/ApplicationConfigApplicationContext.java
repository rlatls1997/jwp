package core.di.context;

import java.util.Arrays;
import java.util.Set;

import com.google.common.collect.Sets;

import core.annotation.ComponentScan;
import core.di.factory.AnnotatedBeanDefinitionReader;
import core.di.factory.DefaultBeanFactory;
import core.di.factory.ClasspathBeanDefinitionScanner;

public class ApplicationConfigApplicationContext implements ApplicationContext {

	private final DefaultBeanFactory defaultBeanFactory;

	public ApplicationConfigApplicationContext(Class<?>... configClass) {
		Object[] basePackages = findBasePackages(configClass);
		defaultBeanFactory = new DefaultBeanFactory();

		AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(defaultBeanFactory);
		annotatedBeanDefinitionReader.loadBeanDefinitions(configClass);

		ClasspathBeanDefinitionScanner classpathBeanDefinitionScanner = new ClasspathBeanDefinitionScanner(defaultBeanFactory);
		classpathBeanDefinitionScanner.doScan(basePackages);

		defaultBeanFactory.initialize();
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
		return defaultBeanFactory.getBean(clazz);
	}

	public Set<Class<?>> getBeanClasses() {
		return defaultBeanFactory.getBeanClasses();
	}
}
