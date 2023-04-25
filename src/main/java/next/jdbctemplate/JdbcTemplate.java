package next.jdbctemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import core.jdbc.ConnectionManager;

public class JdbcTemplate {

	private PreparedStatementSetter createPreparedStatementSetter(Object... objects) {
		return preparedStatement -> {
			for (int i = 0; i < objects.length; i++) {
				preparedStatement.setString(i + 1, (String)objects[i]);
			}
		};
	}

	public void update(String sql, Object... objects) {
		PreparedStatementSetter preparedStatementSetter = createPreparedStatementSetter(objects);
		update(sql, preparedStatementSetter);
	}

	private void update(String sql, PreparedStatementSetter preparedStatementSetter) {

		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sql)
		) {
			preparedStatementSetter.setValues(preparedStatement);

			preparedStatement.executeUpdate();
		} catch (SQLException sqlException) {
			throw new DataAccessException();
		}
	}

	public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... objects) {
		PreparedStatementSetter preparedStatementSetter = createPreparedStatementSetter(objects);

		try {
			return query(sql, preparedStatementSetter, rowMapper);
		} catch (SQLException sqlException) {
			throw new RuntimeException("failed to execute queryForObject. sql:" + sql + "message:" + sqlException.getMessage());
		}
	}

	private <T> List<T> query(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) throws SQLException {
		ResultSet resultSet = null;

		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sql);
		) {
			preparedStatementSetter.setValues(preparedStatement);
			resultSet = preparedStatement.executeQuery();
			List<T> objects = new ArrayList<>();

			while (resultSet.next()) {
				T object = rowMapper.mapRow(resultSet);

				objects.add(object);
			}

			return objects;
		} catch (SQLException sqlException) {
			throw new DataAccessException();
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}
	}

	public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... objects) {
		PreparedStatementSetter preparedStatementSetter = createPreparedStatementSetter(objects);

		try {
			return queryForObject(sql, preparedStatementSetter, rowMapper);
		} catch (SQLException sqlException) {
			throw new RuntimeException("failed to execute queryForObject. sql:" + sql + "message:" + sqlException.getMessage());
		}
	}

	private <T> T queryForObject(String sql, PreparedStatementSetter preparedStatementSetter, RowMapper<T> rowMapper) throws SQLException {
		ResultSet resultSet = null;

		try (Connection connection = ConnectionManager.getConnection();
			 PreparedStatement preparedStatement = connection.prepareStatement(sql);
		) {
			preparedStatementSetter.setValues(preparedStatement);

			resultSet = preparedStatement.executeQuery();

			T object = null;
			if (resultSet.next()) {
				object = rowMapper.mapRow(resultSet);
			}

			return object;
		} catch (SQLException sqlException) {
			throw new DataAccessException();
		} finally {
			if (resultSet != null) {
				resultSet.close();
			}
		}
	}
}
