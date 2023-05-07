package next.dao;

import java.util.List;

import core.jdbc.JdbcTemplate;
import core.jdbc.RowMapper;
import next.model.Answer;

public class AnswerDao {
	public List<Answer> findByQuestionId(long questionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE questionId=?";

		RowMapper<Answer> rm = rs -> new Answer(rs.getLong("answerId"),
			rs.getString("writer"),
			rs.getString("contents"),
			rs.getDate("createdDate"),
			rs.getLong("questionId"));

		return jdbcTemplate.query(sql, rm, questionId);
	}
}
