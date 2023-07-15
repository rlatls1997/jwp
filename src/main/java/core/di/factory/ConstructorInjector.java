package core.di.factory;

import java.util.Set;

import com.google.common.collect.Sets;

public class ConstructorInjector extends AbstractInjector {

	public ConstructorInjector(BeanFactory beanFactory) {
		super(beanFactory);
	}

	@Override
	void inject(Object injectedBean, Object object, BeanFactory beanFactory) {
	}

	@Override
	Class<?> getBeanClass(Object injectedBean) {
		return null;
	}

	@Override
	Set<?> getInjectedBeans(Class<?> clazz) {
		return Sets.newHashSet();
	}
}
