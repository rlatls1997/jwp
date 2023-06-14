package core.ref;

import java.util.Arrays;

import org.junit.Test;

public class Junit4TestRunner {
	@Test
	public void run() throws Exception {
		Class<Junit4Test> clazz = Junit4Test.class;

		Arrays.stream(clazz.getMethods()).forEach(method -> {
			if (method.isAnnotationPresent(MyTest.class)) {
				try {
					method.invoke(Junit4Test.class.newInstance());
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
	}
}
