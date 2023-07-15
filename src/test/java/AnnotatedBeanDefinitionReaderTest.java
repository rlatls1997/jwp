import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Test;

import core.di.factory.AnnotatedBeanDefinitionReader;
import core.di.factory.BeanFactory;

public class AnnotatedBeanDefinitionReaderTest {
	@Test
	public void register_simple() {
		BeanFactory beanFactory = new BeanFactory();
		AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(beanFactory);
		annotatedBeanDefinitionReader.register(ExampleConfig.class);
		beanFactory.initialize();

		assertNotNull(beanFactory.getBean(DataSource.class));
	}
}
