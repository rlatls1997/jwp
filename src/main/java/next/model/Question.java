package next.model;

import java.util.Date;

public class Question {

	private final long questionId;
	private final String writer;
	private final String title;
	private final String contents;
	private final Date createdDate;
	private final long countOfAnswer;

	public Question(long questionId, String writer, String title, String contents, Date createdDate, long countOfAnswer) {
		this.questionId = questionId;
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.createdDate = createdDate;
		this.countOfAnswer = countOfAnswer;
	}

	public long getQuestionId() {
		return questionId;
	}

	public String getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public String getContents() {
		return contents;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public long getCountOfAnswer() {
		return countOfAnswer;
	}
}
