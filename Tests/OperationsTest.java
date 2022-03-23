package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import LispInterpreter.InvalidExpression;
import LispInterpreter.Operations;

class OperationsTest {

	@Test
	void testArithmetics() {

		try {
			Operations.arithmeticOperation("+ 4 5 6 8 7");
			assertEquals(29.5, Operations.arithmeticOperation("+ 7 6.5 16   "));
			assertEquals(18.06, Operations.arithmeticOperation("* 2 4.3 2.1"));
			assertEquals(5, Operations.arithmeticOperation("/ 25 5"));
			assertEquals(-1, Operations.arithmeticOperation("- 1 -1 3"));
			
		} catch (InvalidExpression e) {
			fail(e);
		}

		try {
			Operations.arithmeticOperation("- 1 '(- 4 5) 2 ");
			fail("Excepcion esperada");
		} catch (Exception ex) {

		}

	}
	
	@Test
	void testBooleans() {
		try {
			assertEquals(true, Operations.booleanOperation("< 1.2 5 6.2"));
		} catch (InvalidExpression e) {
			fail (e);
		} 
	}

}
