package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import LispInterpreter.Interpreter;
import LispInterpreter.InvalidExpression;
import LispInterpreter.Operations;
import LispInterpreter.ReferenceException;
import LispInterpreter.VariableFactory;

class InterpreterTests {


	public InterpreterTests() {
	}

	@Test
	void arithmeticTest() {

		try {
			assertEquals(24, Integer.parseInt(Interpreter.operate("(+ 5 4 (+ 1 2 (/ 25 5) (+ 3 4)) )").toString()));
			assertEquals(72.0, Double.parseDouble(Interpreter.operate("(* (- 9.5 0.5) 8)").toString()));
		} catch (InvalidExpression e1) {
			System.out.println("Operacion invalida");
			fail(e1);
		} catch (ReferenceException e) {
			fail(e);
		}

		try {
			assertEquals("24", Interpreter.operate("(+ 5 * 4 (+ 1 2 (/ 25 5) (+ 3 4)) )").toString());
			fail();
		} catch (InvalidExpression e) {
			System.out.println("Resultado esperado test: " + e.toString());
		} catch (ReferenceException e) {
			fail(e);
		}

	}

	@Test
	void assignVariable() {
		try {
			Interpreter.operate("(setq var1 (+ 5 4 (+ 1 2 (/ 25 5) (+ 3 4)) ))");
			Interpreter.operate("(setq var2 2.14)");
			Interpreter.operate("(setq var3 \"hola mundo!\")");

			assertEquals(24, Integer.parseInt(VariableFactory.getVariable("var1").toString()));
			assertEquals(2.14, Double.parseDouble(VariableFactory.getVariable("var2").toString()));
			assertEquals("hola mundo!", VariableFactory.getVariable("var3").toString());

		} catch (InvalidExpression e) {
			fail("Invalid expression." + e);
		} catch (ReferenceException e) {
			fail(e);
		}
	}

	@Test
	void operationWithVariable() {
		try {
			Interpreter.operate("(setq numero 10)");
			assertEquals(15, Integer.parseInt(Interpreter.operate("(+ numero 5 )").toString()));
			assertEquals(25.5, Double.parseDouble(Interpreter.operate("(+ 0.5 (setq name 25) )").toString()));
			assertEquals(22, Integer.parseInt(String.valueOf(Interpreter.operate("(+ 3 (setq nombre (+ 1 5)) (+ 1 5 (+ 1 nombre)))"))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("OperationWithVariableError "+e);
		}
	}
	
	@Test
	void quoteTest() {
		try {
			assertEquals("num", Interpreter.operate("(quote num)").toString());
			assertEquals("num", Interpreter.operate("'num").toString());
			assertEquals("(+ 1 8 5 3 6)", Interpreter.operate("(quote (+ 1 8 5 3 6))").toString());
			assertEquals("(+ 1 8 5 3 6)", Interpreter.operate("'(+ 1 8 5 3 6)").toString());
			assertEquals("(+ (* 9 8 6) (- 9 6))", Interpreter.operate("(quote (+ (* 9 8 6) (- 9 6)))").toString());
			assertEquals("(+ (* 9 8 6) (- 9 6))", Interpreter.operate("'(+ (* 9 8 6) (- 9 6))").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("OperationWithVariableError "+e);
		}
	}
	
	@Test
	void writeTest() {
		try {
			assertEquals("\"num\"", Interpreter.operate("(write \"num\")").toString());
			assertEquals("num", Interpreter.operate("(write 'num)").toString());
			assertEquals("19", Interpreter.operate("(write (+ 8 (- (* 5 3) 4)))").toString());
			assertEquals("\"19\"", Interpreter.operate("(write \"19\")").toString());
			assertEquals("\"t\"", Interpreter.operate("(write \"t\")").toString());
			assertEquals("NIL", Interpreter.operate("(write nil)").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("OperationWithVariableError "+e);
		}
	}
	
	@Test
	void atomTest() {
		try {
			assertEquals("NIL", Interpreter.operate("(atom '(+ 1 2))").toString());
			assertEquals("T", Interpreter.operate("(atom 'num)").toString());
			assertEquals("T", Interpreter.operate("(atom (+ 9 (* 18 (+ 6 8))))").toString());
			assertEquals("NIL", Interpreter.operate("(atom (list 10))").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("OperationWithVariableError "+e);
		}
	}
	
	@Test
	void listpTest() {
		try {
			assertEquals("T", Interpreter.operate("(listp '(+ 1 2))").toString());
			assertEquals("NIL", Interpreter.operate("(listp 'num)").toString());
			assertEquals("NIL", Interpreter.operate("(listp (+ 9 (* 18 (+ 6 8))))").toString());
			assertEquals("T", Interpreter.operate("(listp (list 10))").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("OperationWithVariableError "+e);
		}
	}
	
	@Test
	void listTest() {
		try {
			assertEquals("(15)", Interpreter.operate("(list 15)").toString());
			assertEquals("(num)", Interpreter.operate("(list 'num)").toString());
			assertEquals("((+ 1 (* 9 (- 8 4))))", Interpreter.operate("(list '(+ 1 (* 9 (- 8 4))))").toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			fail("OperationWithVariableError "+e);
		}
	}

}
