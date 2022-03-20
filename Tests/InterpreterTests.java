package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import LispInterpreter.Interpreter;
import LispInterpreter.InvalidExpression;
import LispInterpreter.ReferenceException;
import LispInterpreter.VariableFactory;

class InterpreterTests {

	private Interpreter interpreter;

	public InterpreterTests() {
		interpreter = new Interpreter();
	}

	@Test
	void arithmeticTest() {

		try {
			assertEquals(24, Integer.parseInt(interpreter.operate("(+ 5 4 (+ 1 2 (/ 25 5) (+ 3 4)) )").toString()));
			assertEquals(72.0, Double.parseDouble(interpreter.operate("(* (- 9.5 0.5) 8)").toString()));
		} catch (InvalidExpression e1) {
			fail();
		}

		try {
			assertEquals("24", interpreter.operate("(+ 5 * 4 (+ 1 2 (/ 25 5) (+ 3 4)) )").toString());
			fail();
		} catch (InvalidExpression e) {
			System.out.println("Resultado esperado test: " + e.toString());
		}

	}

	@Test
	void assignVariable() {
		try {
			interpreter.operate("(setq var1 (+ 5 4 (+ 1 2 (/ 25 5) (+ 3 4)) ))");
			interpreter.operate("(setq var2 2.14)");
			interpreter.operate("(setq var3 \"hola mundo!\")");

			assertEquals(24, Integer.parseInt(VariableFactory.getVariable("var1").toString()));
			assertEquals(2.14, Double.parseDouble(VariableFactory.getVariable("var2").toString()));
			assertEquals("hola mundo!", VariableFactory.getVariable("var3").toString());

		} catch (InvalidExpression e) {
			fail("Invalid expression." + e);
		} catch (ReferenceException e) {
			fail(e);
		}
	}

}
