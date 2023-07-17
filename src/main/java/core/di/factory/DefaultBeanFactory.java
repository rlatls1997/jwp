package core.di.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import core.di.factory.config.AnnotatedBeanDefinition;
import core.di.factory.config.BeanDefinition;
import core.di.factory.support.BeanDefinitionRegistry;
import core.di.factory.support.InjectType;

public class DefaultBeanFactory implements BeanDefinitionRegistry, BeanFactory {
	private static final Logger log = LoggerFactory.getLogger(DefaultBeanFactory.class);

	private final Map<Class<?>, Object> beans = Maps.newHashMap();

	private final Map<Class<?>, BeanDefinition> beanDefinitions = Maps.newHashMap();

	public void initialize() {
		for (Class<?> clazz : getBeanClasses()) {
			getBean(clazz);
		}
	}

	public Set<Class<?>> getBeanClasses() {
		return beanDefinitions.keySet();
	}

	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> clazz) {
		Object bean = beans.get(clazz);
		if (bean != null) {
			return (T)bean;
		}

		BeanDefinition BeanDefinition = beanDefinitions.get(clazz);

		if (BeanDefinition instanceof AnnotatedBeanDefinition) {
			bean = createAnnotatedBean(BeanDefinition);
			beans.put(clazz, bean);
			return (T)bean;
		}

		Class<?> concreteClass = findConcreteClass(clazz);
		BeanDefinition concreteBeanDefinition = beanDefinitions.get(concreteClass);

		bean = inject(concreteBeanDefinition);
		beans.put(concreteClass, bean);
		initialize(bean, concreteClass);
		return (T)bean;
	}

	private void initialize(Object bean, Class<?> beanClass) {
		Set<Method> initializeMethods = BeanFactoryUtils.getPostConstructorMethods(beanClass);

		if (initializeMethods.isEmpty()) {
			return;
		}

		for (Method initializeMethod : initializeMethods) {
			try {
				initializeMethod.invoke(bean);
			} catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		}
	}

	private Object createAnnotatedBean(BeanDefinition beanDefinition) {
		AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition)beanDefinition;
		Method method = annotatedBeanDefinition.getMethod();

		try {
			return method.invoke(getBean(method.getDeclaringClass()));
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	private Class<?> findConcreteClass(Class<?> clazz) {
		Set<Class<?>> beanClasses = getBeanClasses();
		Class<?> concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, beanClasses);
		if (!beanClasses.contains(concreteClazz)) {
			throw new IllegalStateException(clazz + "는 Bean이 아니다.");
		}
		return concreteClazz;
	}

	private Object inject(BeanDefinition beanDefinition) {
		if (beanDefinition.getResolvedInjectMode() == InjectType.INJECT_NO) {
			return BeanUtils.instantiate(beanDefinition.getBeanClass());
		} else if (beanDefinition.getResolvedInjectMode() == InjectType.INJECT_FIELD) {
			return injectFields(beanDefinition);
		} else {
			return injectConstructor(beanDefinition);
		}
	}

	private Object injectConstructor(BeanDefinition beanDefinition) {
		Constructor<?> constructor = beanDefinition.getInjectConstructor();
		List<Object> args = Lists.newArrayList();
		for (Class<?> clazz : constructor.getParameterTypes()) {
			args.add(getBean(clazz));
		}
		return BeanUtils.instantiateClass(constructor, args.toArray());
	}

	private Object injectFields(BeanDefinition beanDefinition) {
		Object bean = BeanUtils.instantiate(beanDefinition.getBeanClass());
		Set<Field> injectFields = beanDefinition.getInjectFields();
		for (Field field : injectFields) {
			injectField(bean, field);
		}
		return bean;
	}

	private void injectField(Object bean, Field field) {
		log.debug("Inject Bean : {}, Field : {}", bean, field);
		try {
			field.setAccessible(true);
			field.set(bean, getBean(field.getType()));
		} catch (IllegalAccessException | IllegalArgumentException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void clear() {
		beanDefinitions.clear();
		beans.clear();
	}

	@Override
	public void registerBeanDefinition(Class<?> clazz, BeanDefinition beanDefinition) {
		log.debug("register bean : {}", clazz);
		beanDefinitions.put(clazz, beanDefinition);
	}
}
