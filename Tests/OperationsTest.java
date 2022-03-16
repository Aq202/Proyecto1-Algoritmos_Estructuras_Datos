package Tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import LispInterpreter.Interpreter;

class OperationsTest {

	@Test
	void test() {
Interpreter interpreter = new Interpreter();
		
		Scanner sc = new Scanner(System.in);
		String result = interpreter.operate("(+ 56 54 (- 1 2 ( + 3 2)))");
		System.out.println(result);
		assertEquals("", result);
	}

}
