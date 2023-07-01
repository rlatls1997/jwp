package core.di.factory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import core.annotation.Controller;

public class BeanFactory {
	private static final Logger logger = LoggerFactory.getLogger(BeanFactory.class);

	private Set<Class<?>> preInstanticateBeans;

	private Map<Class<?>, Object> beans = Maps.newHashMap();

	public BeanFactory(Set<Class<?>> preInstanticateBeans) {
		this.preInstanticateBeans = preInstanticateBeans;
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> requiredType) {
		return (T)beans.get(requiredType);
	}

	public void initialize() {
		for (Class<?> preInstanticateBean : preInstanticateBeans) {
			try {
				createBean(preInstanticateBean);
			} catch (Exception exception) {
				throw new IllegalStateException("Failed to create bean.", exception);
			}
		}
	}

	private void createBean(Class<?> beanClass) throws Exception {
		Class<?> concreteClass = BeanFactoryUtils.findConcreteClass(beanClass, preInstanticateBeans);

		// 이미 생성된 빈이면 스킵
		if (beans.containsKey(concreteClass)) {
			logger.info("Already created. concreteClass:{}", concreteClass);
			return;
		}

		Constructor<?> injectedConstructor = BeanFactoryUtils.getInjectedConstructor(concreteClass);

		//@Inject 애너테이션이 붙은 생성자가 없다면 기본생성자로 빈 등록
		if (Objects.isNull(injectedConstructor)) {
			beans.put(concreteClass, concreteClass.getDeclaredConstructor().newInstance());
			return;
		}

		Class<?>[] parameterTypes = injectedConstructor.getParameterTypes();
		int parameterCount = injectedConstructor.getParameterCount();
		Object[] parameters = new Object[parameterCount];

		for (int i = 0; i < parameterCount; i++) {
			Class<?> parameterType = parameterTypes[i];
			//생성자 파라미터 중 의존 컴포넌트가 빈에 등록이 안된 경우 빈 등록
			createBean(parameterType);

			Object bean = getBean(BeanFactoryUtils.findConcreteClass(parameterType, preInstanticateBeans));
			parameters[i] = bean;
		}

		//@Inject 애너테이션이 붙은 생성자가 있다면 의존 빈 가져와서 인스턴스 생성 후 빈 등록
		beans.put(concreteClass, injectedConstructor.newInstance(parameters));
	}

	public Map<Class<?>, Object> getControllers() {
		Map<Class<?>, Object> controllerMap = new HashMap<>();

		for (Class<?> beanClass : beans.keySet()) {
			if (Objects.nonNull(beanClass.getAnnotation(Controller.class))) {
				controllerMap.put(beanClass, beans.get(beanClass));
			}
		}

		return controllerMap;
	}
}
