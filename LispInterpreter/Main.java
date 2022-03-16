package LispInterpreter;

import java.util.Scanner;

public class Main{
	
	public static void main(String[] args) {
		
		Interpreter interpreter = new Interpreter();
		
		Scanner sc = new Scanner(System.in);
		
		String expression = "(+ 56 54 (- 1 2 ( + 3 2)))";
		String result = interpreter.operate(expression);
		
		System.out.println(result);
		
		sc.close();
	}
	
}