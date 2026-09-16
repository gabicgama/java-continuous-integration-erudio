package br.com.erudio.math;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.exceptions.UnsupportedMathOperationException;

@RestController
public class MathController {

	@GetMapping(value = "/sum/{numberOne}/{numberTwo}")
	public Double sum(@PathVariable String numberOne, @PathVariable String numberTwo)
			throws Exception {
		if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		Double result = covertToDouble(numberOne) + covertToDouble(numberTwo);
		return result;
	}

	@GetMapping(value = "/subtraction/{numberOne}/{numberTwo}")
	public Double subtraction(@PathVariable String numberOne, @PathVariable String numberTwo)
			throws Exception {
		if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		Double result = covertToDouble(numberOne) - covertToDouble(numberTwo);
		return result;
	}

	@GetMapping(value = "/multiplication/{numberOne}/{numberTwo}")
	public Double multiplication(@PathVariable String numberOne,
			@PathVariable String numberTwo) throws Exception {
		if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		Double result = covertToDouble(numberOne) * covertToDouble(numberTwo);
		return result;
	}

	@GetMapping(value = "/division/{numberOne}/{numberTwo}")
	public Double division(@PathVariable String numberOne, @PathVariable String numberTwo)
			throws Exception {
		if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		Double result = covertToDouble(numberOne) / covertToDouble(numberTwo);
		return result;
	}

	@GetMapping(value = "/mean/{numberOne}/{numberTwo}")
	public Double mean(@PathVariable String numberOne, @PathVariable String numberTwo)
			throws Exception {
		if (!isNumeric(numberOne) || !isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		Double result = (covertToDouble(numberOne) + covertToDouble(numberTwo)) / 2;
		return result;
	}

	@GetMapping(value = "/squareRoot/{number}")
	public Double squareRoot(@PathVariable String number) throws Exception {
		if (!isNumeric(number)) {
			throw new UnsupportedMathOperationException("Please set a numeric value!");
		}
		Double result = Math.sqrt(covertToDouble(number));
		return result;
	}

	public static Double covertToDouble(String strNumber) {
		if (strNumber == null)
			return 0d;
		String number = strNumber.replaceAll(",", ".");
		if (isNumeric(number))
			return Double.parseDouble(number);
		return 0d;
	}

	public static boolean isNumeric(String strNumber) {
		if (strNumber == null)
			return false;
		String number = strNumber.replaceAll(",", ".");
		return number.matches("[-+]?[0-9]*\\.?[0-9]+");

	}

}
