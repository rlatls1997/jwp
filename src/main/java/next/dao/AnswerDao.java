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

	public Answer findOne(long answerId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "SELECT answerId, writer, contents, createdDate, questionId FROM ANSWERS WHERE answerId=?";

		RowMapper<Answer> rm = rs -> new Answer(rs.getLong("answerId"),
			rs.getString("writer"),
			rs.getString("contents"),
			rs.getDate("createdDate"),
			rs.getLong("questionId"));

		return jdbcTemplate.queryForObject(sql, rm, answerId);
	}

	public boolean delete(long answerId){
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "DELETE FROM ANSWERS WHERE answerId=?";

		return jdbcTemplate.execute(sql, answerId);
	}

	public Answer insertAndSelectSavedAnswer(Answer answer) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate();
		String sql = "INSERT INTO ANSWERS (writer, contents, createdDate, questionId) VALUES (?, ?, CURRENT_TIMESTAMP(), ?)";

		long generatedKey = jdbcTemplate.insert(sql, answer.getWriter(), answer.getContents(), answer.getQuestionId());

		RowMapper<Answer> rm = rs -> new Answer(rs.getLong("answerId"),
			rs.getString("writer"),
			rs.getString("contents"),
			rs.getDate("createdDate"),
			rs.getLong("questionId"));

		return findOne(generatedKey);
	}
}
