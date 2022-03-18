package Tests;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;

import LispInterpreter.Operations;

class OperationsTest {

	@Test
	void test() {
		
		assertEquals(30, Operations.arithmeticOperation("(+ 4 5 6 8 7)"));
		assertEquals(29.5, Operations.arithmeticOperation("(+ 7 6.5 16)   "));
		assertEquals(18.06, Operations.arithmeticOperation("(* 2 4.3 2.1)"));
		assertEquals(5, Operations.arithmeticOperation("(/ 25 5)"));
		assertEquals(-1, Operations.arithmeticOperation("(- 1 -1 3)"));
		
		try {
			Operations.arithmeticOperation("(- 1 '(- 4 5) 2 )");
			fail("Excepcion esperada");
		}catch(Exception ex) {
			
		}
		
		
	}

}