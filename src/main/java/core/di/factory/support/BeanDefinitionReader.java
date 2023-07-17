package core.di.factory.support;

public interface BeanDefinitionReader {
	void loadBeanDefinitions(Class<?>... annotatedClasses);
}
