package br.com.erudio.math;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Teste da classe SimpleMath")
class SimpleMathTest {

	SimpleMath math;

	@BeforeAll
	static void fistMethodToRun() {
		System.out.println(">>Rodando com annotation @BeforeALL");
	}

	@AfterAll
	static void lastMethodToRun() {
		System.out.println(">>Rodando com annotation @AfterALL");
	}

	@BeforeEach
	void runBeforeEachMethod() {
		System.out.println("Rodando com annotation @BeforeEach");
		math = new SimpleMath();
	}

	@AfterEach
	void afterEachMethod() {
		System.out.println("Rodando com annotation @AfterEach");
	}

	// como nomear uma classe teste:
	// test[System under test]_[Condition or state change]_[Expected result]
	@Test
	@DisplayName("Teste da soma")
	void testSum_When_SixDotTwoIsAddedByTwo_ShouldReturnEightDotTwo() {
		System.out.println("Teste 1");

		// Behavior-Driven Development(BDD)
		// Given (Dado que) When (Quando) Then(Então)
		// Desenvolve o teste em torno do contexto de um comportamento experado.

		// AAA Arrange, Act, Assert

		// Given - Arrange
//		SimpleMath math = new SimpleMath();
		double firstNumber = 6.2D;
		double secondNumber = 2D;
		Double expected = 8.2D;

		// When - Act
		Double actual = math.sum(firstNumber, secondNumber);

		// Then - Assert
		// lazy assert messages: usa lambda function para executar a mensagem somente se
		// o teste falhar
		assertEquals(expected, actual,
				() -> "sum " + firstNumber + " + " + secondNumber + " didn't result " + expected);
		assertNotEquals(0, actual);
		assertNotNull(actual);
	}

	@Test
	void testSubtraction() {
		System.out.println("Teste 2");
//		SimpleMath math = new SimpleMath();
		double firstNumber = 6.2D;
		double secondNumber = 2D;
		Double actual = math.subtraction(firstNumber, secondNumber);
		Double expected = 4.2D;
		assertEquals(expected, actual,
				() -> "subtraction" + firstNumber + " - " + secondNumber + " didn't result " + expected);
		assertNotEquals(0, actual);
		assertNotNull(actual);
	}

	@Test
	void testMultiplication() {
		System.out.println("Teste 3");
//		SimpleMath math = new SimpleMath();
		double firstNumber = 3D;
		double secondNumber = 5D;
		Double actual = math.multiplication(firstNumber, secondNumber);
		Double expected = 15D;
		assertEquals(expected, actual,
				() -> "multiplication" + firstNumber + " * " + secondNumber + " didn't result " + expected);
		assertNotEquals(0, actual);
		assertNotNull(actual);
	}

	@Test
	void testDivision() {
		System.out.println("Teste 4");
//		SimpleMath math = new SimpleMath();
		double firstNumber = 12D;
		double secondNumber = 3D;
		Double actual = math.division(firstNumber, secondNumber);
		Double expected = 4D;
		assertEquals(expected, actual,
				() -> "division" + firstNumber + " / " + secondNumber + " didn't result " + expected);
		assertNotEquals(0, actual);
		assertNotNull(actual);
	}

	@Test
	void testMean() {
		System.out.println("Teste 5");
//		SimpleMath math = new SimpleMath();
		double firstNumber = 12D;
		double secondNumber = 3D;
		Double actual = math.mean(firstNumber, secondNumber);
		Double expected = 7.5D;
		assertEquals(expected, actual,
				() -> "mean (" + firstNumber + " + " + secondNumber + ")/ 2 " + "didn't result " + expected);
		assertNotEquals(0, actual);
		assertNotNull(actual);
	}

	@Test
	void testSquareRoot() {
		System.out.println("Teste 6");
//		SimpleMath math = new SimpleMath();
		double number = 9D;
		Double actual = math.squareRoot(number);
		Double expected = 3D;
		assertEquals(expected, actual, () -> "squareRoot of " + number + "didn't result " + expected);
		assertNotEquals(0, actual);
		assertNotNull(actual);
	}

	@Test
	@DisplayName("Test Division by Zero")
	void testDivision_When_FirstNumberIsDividedByZero_ShouldThrowArithmeticException() {

		// given
		double firstNumber = 6.2D;
		double secondNumber = 0D;

		var expectedMessage = "Impossible to divide by zero!";

		// when & then
		ArithmeticException actual = assertThrows(ArithmeticException.class, () -> {
			// when & then
			math.division(firstNumber, secondNumber);
		}, () -> "Division by zero should throw an ArithmeticException");

		assertEquals(expectedMessage, actual.getMessage(), () -> "Unexpected exception message!");
	}

}
