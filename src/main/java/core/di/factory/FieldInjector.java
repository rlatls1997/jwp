package core.di.factory;

import java.lang.reflect.Field;
import java.util.Set;

public class FieldInjector extends AbstractInjector {

	public FieldInjector(BeanFactory beanFactory) {
		super(beanFactory);
	}

	@Override
	void inject(Object injectedBean, Object object, BeanFactory beanFactory) {
		Field field = (Field)injectedBean;

		try {
			field.setAccessible(true);
			field.set(beanFactory.getBean(field.getDeclaringClass()), object);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	Class<?> getBeanClass(Object injectedBean) {
		Field field = (Field)injectedBean;

		return findConcreteClass(field.getType());
	}

	@Override
	Set<?> getInjectedBeans(Class<?> clazz) {
		return BeanFactoryUtils.getInjectedFields(clazz);
	}
}
