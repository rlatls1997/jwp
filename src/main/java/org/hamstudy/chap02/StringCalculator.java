package org.hamstudy.chap02;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

	private static final Pattern DELIMITER = Pattern.compile("[,:]");

	private static final Pattern CUSTOM_DELIMITER_REGEX = Pattern.compile("^\\/\\/(?<delimiter>.)\\n(?<expression>.*)");

	int add(String text) {
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
		Pattern customDelimiter = getCustomDelimiter(matcher);
		String expression = matcher.group("expression");

		return addSplitNumbers(expression, customDelimiter);
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

	private Pattern getCustomDelimiter(Matcher matcher) {
		String delimiter = matcher.group("delimiter");

		return Pattern.compile(delimiter);
	}
}
