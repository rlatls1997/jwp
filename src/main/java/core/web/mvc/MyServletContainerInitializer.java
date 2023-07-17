package core.web.mvc;

import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.HandlesTypes;

import core.comfig.Config;
import core.di.context.ApplicationConfigApplicationContext;
import core.di.context.ApplicationContext;

@HandlesTypes(WebApplicationInitializer.class)
public class MyServletContainerInitializer implements ServletContainerInitializer {

	@Override
	public void onStartup(Set<Class<?>> set, ServletContext servletContext) throws ServletException {
		ApplicationContext applicationContext = new ApplicationConfigApplicationContext(Config.class);
		AnnotationHandlerMapping ahm = new AnnotationHandlerMapping(applicationContext);
		ahm.initialize();
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(ahm));
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");
	}
}
