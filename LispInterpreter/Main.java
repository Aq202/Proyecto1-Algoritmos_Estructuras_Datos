package LispInterpreter;

import java.util.Scanner;

public class Main{
	
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		String expression = "(write (+ 5 * 4 (+ 1 2 (/ 25 5) (+ 3 4)) ))";
		String [] lines = new String[1];
		try {
			if(expression.contains("\n"))
				lines = expression.split("\n");
			else lines[0] = expression;
			for(String l : lines) {
				Data data = Interpreter.operate(l);
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