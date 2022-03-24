package LispInterpreter;

import java.util.Scanner;

public class Main{
	
	public static void main(String[] args) {
		
		Interpreter interpreter = new Interpreter();
		
		Scanner sc = new Scanner(System.in);
		
		String expression = "(write (quote \"nil\"))";
		try {
			Data data = interpreter.operate(expression);
			if(data.getDescription() != null && data.getDescription().equals("print"))
				System.out.println(data.getValue());
		} catch (InvalidExpression | ReferenceException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		//System.out.println(result);
		
		sc.close();
	}
	
}