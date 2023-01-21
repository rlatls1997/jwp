package org.hamstudy.chap02;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CalculatorTest {

	@ParameterizedTest
	@CsvSource({
		"1, 2",
		"99, -4",
		"672, 99"
	})
	void testAdd(int first, int second) {
	    // given

	    // when
		int result = Calculator.add(first, second);

		// then
		Assertions.assertEquals(result, first + second);
	}
}