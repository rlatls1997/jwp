package next.model;

import java.util.Date;

public class Answer {

	private long answerId;
	private final String writer;
	private final String contents;
	private Date createdDate;
	private final long questionId;

	public Answer(String writer, String contents, long questionId) {
		this.writer = writer;
		this.contents = contents;
		this.questionId = questionId;
	}

	public Answer(long answerId, String writer, String contents, Date createdDate, long questionId) {
		this.answerId = answerId;
		this.writer = writer;
		this.contents = contents;
		this.createdDate = createdDate;
		this.questionId = questionId;
	}

	public long getAnswerId() {
		return answerId;
	}

	public String getWriter() {
		return writer;
	}

	public String getContents() {
		return contents;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public long getQuestionId() {
		return questionId;
	}
}
