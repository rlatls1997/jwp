package next;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import next.controller.Controller;
import next.controller.CreateUserController;
import next.controller.ForwardController;
import next.controller.HomeController;
import next.controller.ListUserController;
import next.controller.LoginController;
import next.controller.LogoutController;
import next.controller.ProfileController;
import next.controller.UpdateUserController;
import next.controller.UpdateUserFormController;

public class RequestMapping {
	private static final Map<String, Controller> CONTROLLER_MAP;

	static {
		Map<String, Controller> controllers = new HashMap<>();

		HomeController homeController = new HomeController();
		controllers.put("/", homeController);

		controllers.put("/users", new ListUserController());
		controllers.put("/users/create", new CreateUserController());
		controllers.put("/users/update", new UpdateUserController());
		controllers.put("/users/updateForm", new UpdateUserFormController());
		controllers.put("/users/login", new LoginController());
		controllers.put("/users/logout", new LogoutController());
		controllers.put("/users/profile", new ProfileController());

		controllers.put("/users/form", new ForwardController("/user/form.jsp"));
		controllers.put("/users/loginForm", new ForwardController("/user/login.jsp"));

		CONTROLLER_MAP = Collections.unmodifiableMap(controllers);
	}

	public static Controller mapController(String requestPath){
		return CONTROLLER_MAP.get(requestPath);
	}
}
