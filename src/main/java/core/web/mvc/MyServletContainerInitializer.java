package core.web.mvc;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.HandlesTypes;

@HandlesTypes(WebApplicationInitializer.class)
public class MyServletContainerInitializer implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
		AnnotationHandlerMapping ahm = new AnnotationHandlerMapping("next", "core");
		ahm.initialize();
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(ahm));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}
}
