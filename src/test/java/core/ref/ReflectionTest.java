package core.ref;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import next.model.Question;
import next.model.User;

public class ReflectionTest {
	private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

	@Test
	public void showClass() {
		Class<Question> clazz = Question.class;
		logger.debug(clazz.getName());
	}

	@Test
	public void newInstanceWithConstructorArgs() throws InvocationTargetException, InstantiationException, IllegalAccessException {
		Class<User> clazz = User.class;

		User user = (User)clazz.getDeclaredConstructors()[0].newInstance("userId", "password", "name", "email");
		logger.debug("user : {}", user);

		logger.debug(clazz.getName());
	}

	@Test
	public void privateFieldAccess() throws IllegalAccessException {
		Class<Student> clazz = Student.class;

		Student student = new Student();

		for (Field declaredField : clazz.getDeclaredFields()) {
			declaredField.setAccessible(true);

			if (declaredField.getName().equals("name")) {
				declaredField.set(student, "김신혁");
			}

			if (declaredField.getName().equals("age")) {
				declaredField.set(student, 27);
			}
		}

		logger.debug("student : {}", student);
		logger.debug(clazz.getName());
	}
}
