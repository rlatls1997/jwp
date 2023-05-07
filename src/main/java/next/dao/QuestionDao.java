package next.dao;

import java.util.List;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.Question;

public class QuestionDao {
	public List<Question> findAll() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS";

		RowMapper<Question> rm = rs -> new Question(rs.getLong("questionId"),
			rs.getString("writer"),
			rs.getString("title"),
			rs.getString("contents"),
			rs.getDate("createdDate"),
			rs.getLong("countOfAnswer"));

		return jdbcTemplate.query(sql, rm);
	}

	public Question findOne(long questionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT questionId, writer, title, contents, createdDate, countOfAnswer FROM QUESTIONS WHERE questionId=?";

		RowMapper<Question> rm = rs -> new Question(rs.getLong("questionId"),
			rs.getString("writer"),
			rs.getString("title"),
			rs.getString("contents"),
			rs.getDate("createdDate"),
			rs.getLong("countOfAnswer"));

		return jdbcTemplate.queryForObject(sql, rm, questionId);
	}
}
