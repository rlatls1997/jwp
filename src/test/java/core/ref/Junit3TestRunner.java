package core.ref;

import java.util.Arrays;

import org.junit.Test;

public class Junit3TestRunner {
	@Test
	public void run() throws Exception {
		Class<Junit3Test> clazz = Junit3Test.class;

		Arrays.stream(clazz.getMethods()).forEach(method -> {
			if (method.getName().startsWith("test")) {
				try {
					method.invoke(Junit3Test.class.newInstance());
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
	}
}
