package org.hamstudy.chap02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class StringCalculatorTest {

	private StringCalculator stringCalculator;

	@BeforeEach
	void setup() {
		stringCalculator = new StringCalculator();
	}

	@ParameterizedTest
	@ValueSource(strings = {"6", "5,1", "4:2", "1,2,3", "1:2:3", "1,2:3", "1:2,3", "//;\n1;2;3", "//&\n1&2&3", "//#\n1#2#3"})
	void add_When_NormalCase_Expect_Sum(String text) {
		int sum = stringCalculator.add(text);

		assertEquals(6, sum);
	}

	@ParameterizedTest
	@NullAndEmptySource
	void add_When_Null_And_Blank_Expect_Zero(String text) {
		int sum = stringCalculator.add(text);

		assertEquals(0, sum);
	}

	@ParameterizedTest
	@ValueSource(strings = {"-6", "5,-1", "-4:2", "1,-2,3", "-1:2:-3", "-1,-2:-3", "1:-2,-3", "//;\n-1;2;3", "//&\n1&-2&3", "//#\n1#2#-3"})
	void add_When_Negative_Expect_Throw(String text) {
		assertThrows(IllegalArgumentException.class, () -> stringCalculator.add(text));
	}
}