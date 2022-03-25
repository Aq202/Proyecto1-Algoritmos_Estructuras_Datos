package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import LispInterpreter.Interpreter;
import LispInterpreter.InvalidExpression;
import LispInterpreter.ReferenceException;

class LispFullProgramsTests {

	@Test
	void factorialProgram() {
		
		try {
			Interpreter.operate("(defun factorial(number)(if (< number 0) \"MATH ERROR\" (if (= number 0) 1 (* number (factorial (- number 1))))))");
			assertEquals(720, Integer.parseInt(Interpreter.operate("(factorial 6)").toString()));
		
		} catch (InvalidExpression e) {
			fail(e.getMessage());
		} catch (ReferenceException e) {
			fail(e.getMessage());
		}
		
	}

}
