package next.dao;

import java.util.List;

import next.jdbctemplate.JdbcTemplate;
import next.model.User;

public class UserDao {

	public void insert(User user) {
		new JdbcTemplate().update("INSERT INTO USERS VALUES (?, ?, ?, ?)",
			user.getUserId(),
			user.getPassword(),
			user.getName(),
			user.getEmail());
	}

	public void update(User user) {
		new JdbcTemplate().update("UPDATE USERS SET password = ?, name = ?, email = ? WHERE userId = ?",
			user.getPassword(),
			user.getName(),
			user.getEmail(),
			user.getUserId());
	}

	public List<User> findAll() {
		return new JdbcTemplate().query("SELECT userId, password, name, email FROM USERS",
			resultSet -> new User(resultSet.getString("userId"), resultSet.getString("password"), resultSet.getString("name"),
				resultSet.getString("email")));
	}

	public User findByUserId(String userId) {
		return new JdbcTemplate().queryForObject("SELECT userId, password, name, email FROM USERS WHERE userid=?",
			resultSet -> new User(resultSet.getString("userId"), resultSet.getString("password"), resultSet.getString("name"),
				resultSet.getString("email")),
			userId);
	}

}
