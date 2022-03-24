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
			assertEquals(1.5, Double.parseDouble(Interpreter.operate("(averagenum 1 1 1 3)").toString()));
			
			
			Interpreter.operate("(defun hola() \"Hola\")");
			assertEquals("\"Hola\"", Interpreter.operate("(hola )").toString());
			
			Interpreter.operate("(defun op(var1 var2 var3) (setq res1 (+ var1 var2)) (* res1 var3) )");
			assertEquals(90, Integer.parseInt(Interpreter.operate("(op 1 2 (op 1 4 6))").toString()));
		} catch (Exception e) {
			fail("Function error: " + e);
		}
		
	}

}
