package auth;

import java.util.Map;
import java.util.Objects;

import db.DataBase;
import model.User;

public class AuthProcessor {

	public boolean login(Map<String, String> httpRequestBodyMap){
		String userId = httpRequestBodyMap.get("userId");
		String password = httpRequestBodyMap.get("password");

		User user = DataBase.findUserById(userId);

		if(Objects.isNull(user)){
			return false;
		}

		return password.equals(user.getPassword());
	}
}
