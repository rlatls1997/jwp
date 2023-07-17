import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Test;

import core.di.factory.AnnotatedBeanDefinitionReader;
import core.di.factory.DefaultBeanFactory;

public class AnnotatedDefaultBeanDefinitionReaderTest {
	@Test
	public void register_simple() {
		DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
		AnnotatedBeanDefinitionReader annotatedBeanDefinitionReader = new AnnotatedBeanDefinitionReader(defaultBeanFactory);
		annotatedBeanDefinitionReader.loadBeanDefinitions(ExampleConfig.class);
		defaultBeanFactory.initialize();

		assertNotNull(defaultBeanFactory.getBean(DataSource.class));
	}
}
