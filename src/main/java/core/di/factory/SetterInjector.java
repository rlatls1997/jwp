package core.di.factory;

import java.lang.reflect.Method;
import java.util.Set;

public class SetterInjector extends AbstractInjector {

	public SetterInjector(BeanFactory beanFactory) {
		super(beanFactory);
	}

	@Override
	void inject(Object injectedBean, Object object, BeanFactory beanFactory) {
		Method method = (Method)injectedBean;

		Class<?>[] paramTypes = method.getParameterTypes();

		if (paramTypes.length != 1) {
			throw new IllegalStateException("DI할 메소드 인자는 하나여야 합니다.");
		}

		try {
			method.invoke(beanFactory.getBean(method.getDeclaringClass()), object);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	Class<?> getBeanClass(Object injectedBean) {
		Method method = (Method)injectedBean;

		Class<?>[] paramTypes = method.getParameterTypes();

		if (paramTypes.length != 1) {
			throw new IllegalStateException("DI할 메소드 인자는 하나여야 합니다.");
		}

		return findConcreteClass(paramTypes[0]);
	}

	@Override
	Set<?> getInjectedBeans(Class<?> clazz) {
		return BeanFactoryUtils.getInjectedMethods(clazz);
	}
}
