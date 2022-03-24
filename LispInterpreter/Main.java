package LispInterpreter;

import java.util.Scanner;

public class Main{
	
	public static void main(String[] args) {
		
		Interpreter interpreter = new Interpreter();
		
		Scanner sc = new Scanner(System.in);
		
		String expression = "(setq s 10)\n(write (quote nil))";
		String [] lines = null;
		try {
			if(expression.contains("\n"))
				lines = expression.split("\n");
			else lines[0] = expression;
			for(String l : lines) {
				Data data = interpreter.operate(l);
				if(data.getDescription() != null && data.getDescription().contains("print"))
					System.out.println(data.getValue());
			}
		} catch (InvalidExpression | ReferenceException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		//System.out.println(result);
		
		sc.close();
	}
	
}