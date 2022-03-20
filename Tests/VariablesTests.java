package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import LispInterpreter.ReferenceException;
import LispInterpreter.VariableFactory;

class VariablesTests {

	@Test
	void createVariableTest() {
		
		VariableFactory.newVariable("var1", 25.12);
		VariableFactory.newVariable("var2", "Hola mundo!");
		VariableFactory.newVariable("var3", 10);
		try {
			assertEquals(25.12, Double.parseDouble(VariableFactory.getVariable("var1").toString()));
			assertEquals("Hola mundo!", VariableFactory.getVariable("var2").toString());
			assertEquals(10,Integer.parseInt(VariableFactory.getVariable("var3").toString()));
		} catch (ReferenceException e) {
			fail(e);
		}
	}

}
