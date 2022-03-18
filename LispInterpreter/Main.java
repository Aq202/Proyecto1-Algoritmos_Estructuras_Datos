package LispInterpreter;

import java.util.Scanner;

public class Main{
	
	public static void main(String[] args) {
		
		Interpreter interpreter = new Interpreter();
		
		Scanner sc = new Scanner(System.in);
		
		String expression = "(+ 56 54 (- 1 2 ( + 3 2)))";
		//interpreter.operate(expression);
		
		//System.out.println(result);
		
		double h = 0.1;
		System.out.println((int) h);
		
		sc.close();
	}
	
}