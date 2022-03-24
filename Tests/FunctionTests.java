package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import LispInterpreter.Function;
import LispInterpreter.InvalidExpression;

class FunctionTests {

	@Test
	void functionsDefinition() {
		
		try {
			Function function = new Function("defun averagenum (n1 n2 n3 n4)  (/ ( + n1 n2 n3 n4) 4)");
			System.out.println("RESULTADO "+function.execute("1","2", "3", "4"));
		} catch (Exception e) {
			fail("Function error: " + e);
		}
		
	}

}
