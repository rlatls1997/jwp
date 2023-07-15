package core.di.factory;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import core.annotation.Controller;

public class BeanFactory {
	private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

	private final Set<Class<?>> preInstanticateBeans;

	private final Map<Class<?>, Object> beans = Maps.newHashMap();

	private final List<Injector> injectors;

	public BeanFactory(Set<Class<?>> preInstanticateBeans) {
		this.preInstanticateBeans = preInstanticateBeans;
		this.injectors = List.of(new FieldInjector(this), new SetterInjector(this), new ConstructorInjector(this));
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> requiredType) {
		return (T)beans.get(requiredType);
	}

	public void initialize() {
		for (Class<?> clazz : preInstanticateBeans) {
			if (beans.get(clazz) == null) {
				logger.debug("instantiated Class : {}", clazz);
				injectors.forEach(injector -> injector.inject(clazz));
			}
		}
	}

	public Class<?> findConcreteClass(Class<?> clazz) {
		return BeanFactoryUtils.findConcreteClass(clazz, preInstanticateBeans);
	}

	public boolean isBeanClass(Class<?> clazz) {
		return preInstanticateBeans.contains(clazz);
	}

	public void putBean(Class<?> clazz, Object bean) {
		beans.put(clazz, bean);
	}

	public Map<Class<?>, Object> getControllers() {
		Map<Class<?>, Object> controllers = Maps.newHashMap();
		for (Class<?> clazz : preInstanticateBeans) {
			if (clazz.isAnnotationPresent(Controller.class)) {
				controllers.put(clazz, beans.get(clazz));
			}
		}
		return controllers;
	}
}
