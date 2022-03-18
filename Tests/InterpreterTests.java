package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import LispInterpreter.Interpreter;
import LispInterpreter.InvalidExpression;

class InterpreterTests {
	
	private Interpreter interpreter;
	
	public InterpreterTests() {
		interpreter = new Interpreter();
	}

	@Test
	void arithmeticTest() {
		
		try {
			assertEquals(24,Integer.parseInt(interpreter.operate("(+ 5 4 (+ 1 2 (/ 25 5) (+ 3 4)) )").toString()));
			assertEquals(72.0,Double.parseDouble(interpreter.operate("(* (- 9.5 0.5) 8)").toString()));
		} catch (InvalidExpression e1) {
			fail();
		}
		
		
		try {
			assertEquals("24", interpreter.operate("(+ 5 * 4 (+ 1 2 (/ 25 5) (+ 3 4)) )").toString());
			fail();
		} catch (InvalidExpression e) {
			System.out.println("Resultado esperado test: "+e.toString());
		}
	}

}
