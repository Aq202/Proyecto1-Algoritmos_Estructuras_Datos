package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import LispInterpreter.Function;
import LispInterpreter.Interpreter;
import LispInterpreter.InvalidExpression;

class FunctionTests {

	@Test
	void functionsDefinition() {
		
		try {
			Interpreter.operate("(defun averagenum (n1 n2 n3 n4) (/ ( + n1 n2 n3 n4) 4))");
			assertEquals(6, Integer.parseInt(Interpreter.operate("(averagenum 2 8 10 4)").toString()));
			//assertEquals(1.5, Double.parseDouble(Interpreter.operate("(averagenum 1 1 1 3)").toString()));
		} catch (Exception e) {
			fail("Function error: " + e);
		}
		
	}

}
