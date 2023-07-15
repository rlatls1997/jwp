package core.di.factory;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;

public abstract class AbstractInjector implements Injector {

	private final BeanFactory beanFactory;

	public AbstractInjector(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	@Override
	public void inject(Class<?> clazz) {
		instantiateClass(clazz);
		Set<?> injectedBeans = getInjectedBeans(clazz);

		for (Object injectedBean : injectedBeans) {
			Class<?> beanClass = getBeanClass(injectedBean);
			inject(injectedBean, instantiateClass(beanClass), beanFactory);
		}
	}

	abstract void inject(Object injectedBean, Object object, BeanFactory beanFactory);

	abstract Class<?> getBeanClass(Object injectedBean);

	abstract Set<?> getInjectedBeans(Class<?> clazz);

	private Object instantiateClass(Class<?> clazz) {
		Object bean = beanFactory.getBean(clazz);
		if (bean != null) {
			return bean;
		}
		System.out.println("clazz	:"+clazz);
		Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz);
		if (injectedConstructor == null) {
			bean = BeanUtils.instantiate(clazz);
			beanFactory.putBean(clazz, bean);
			return bean;
		}

		bean = instantiateConstructor(injectedConstructor);
		beanFactory.putBean(clazz, bean);
		return bean;
	}

	private Object instantiateConstructor(Constructor<?> constructor) {

		Class<?>[] pTypes = constructor.getParameterTypes();
		List<Object> args = Lists.newArrayList();
		for (Class<?> clazz : pTypes) {
			Class<?> concreteClazz = findConcreteClass(clazz);

			if (isNotBeanClass(concreteClazz)) {
				throw new IllegalStateException(clazz + "는 Bean이 아니다.");
			}

			Object bean = beanFactory.getBean(concreteClazz);
			if (bean == null) {
				bean = instantiateClass(concreteClazz);
			}
			args.add(bean);
		}
		return BeanUtils.instantiateClass(constructor, args.toArray());
	}

	public Class<?> findConcreteClass(Class<?> clazz) {
		return beanFactory.findConcreteClass(clazz);
	}

	public boolean isBeanClass(Class<?> clazz) {
		return beanFactory.isBeanClass(clazz);
	}

	public boolean isNotBeanClass(Class<?> clazz) {
		return !isBeanClass(clazz);
	}
}
