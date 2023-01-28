package org.hamstudy.chap02;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringCalculatorTest {

	private StringCalculator stringCalculator;

	@BeforeEach
	void setup() {
		stringCalculator = new StringCalculator();
	}

	@Test
	void add_When_NormalCase_Expect_Sum() {
		String text = "1,2:3";

		int sum = stringCalculator.add(text);

		assertEquals(6, sum);
	}

	@Test
	void add_When_SingleNumber_Expect_Sum() {
		String text = "1";

		int sum = stringCalculator.add(text);

		assertEquals(1, sum);
	}

	@Test
	void add_When_WithCustomDelimiter_Expect_Sum() {
		String text = "//;\n1;2;3";

		int sum = stringCalculator.add(text);

		assertEquals(6, sum);
	}

	@Test
	void add_When_Null_Expect_Zero() {
		int sum = stringCalculator.add(null);

		assertEquals(0, sum);
	}

	@Test
	void add_When_Blank_Expect_Zero() {
		String text = "";

		int sum = stringCalculator.add(text);

		assertEquals(0, sum);
	}

	@Test
	void add_When_Negative_Expect_Throw() {
		assertThrows(IllegalArgumentException.class, () -> {
			String text = "1,-2:3";
			stringCalculator.add(text);
		});
	}
}