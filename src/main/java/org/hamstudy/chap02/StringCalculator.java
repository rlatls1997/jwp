package org.hamstudy.chap02;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

	private static final Pattern DELIMITER = Pattern.compile("[,:]");

	private static final String DELIMITER_GROUP_NAME = "delimiter";

	private static final String EXPRESSION_GROUP_NAME = "expression";

	private static final Pattern CUSTOM_DELIMITER_REGEX = Pattern.compile("^\\/\\/(?<" + DELIMITER_GROUP_NAME + ">.)\\n(?<" + EXPRESSION_GROUP_NAME + ">.*)");

	public int add(String text) {
		if (Objects.isNull(text) || text.isBlank()) {
			return 0;
		}

		Matcher customDelimiterMatcher = CUSTOM_DELIMITER_REGEX.matcher(text);

		if (customDelimiterMatcher.find()) {
			return addNumbersSplitByCustomDelimiter(customDelimiterMatcher);
		}

		return addSplitNumbers(text, DELIMITER);
	}

	private int addNumbersSplitByCustomDelimiter(Matcher matcher) {
		String delimiter = matcher.group(DELIMITER_GROUP_NAME);
		String expression = matcher.group(EXPRESSION_GROUP_NAME);

		Pattern delimiterPattern = Pattern.compile(delimiter);

		return addSplitNumbers(expression, delimiterPattern);
	}

	private int addSplitNumbers(String text, Pattern delimiter) {
		return delimiter.splitAsStream(text)
			.map(this::convertPositiveNumber)
			.reduce(0, Integer::sum);
	}

	private int convertPositiveNumber(String text) {
		int number = Integer.parseInt(text);

		if (number < 0) {
			throw new IllegalArgumentException("target text isn't positive number. text:" + text);
		}

		return number;
	}
}
