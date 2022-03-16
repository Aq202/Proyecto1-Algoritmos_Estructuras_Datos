package LispInterpreter;

public class Interpreter {
	
	public String operate(String expression) {
		
		int state = SintaxScanner.getState(expression);
		String mainExpression = operateSubexpressions(expression);
		
		switch(state) {
		case 1:{
			
			return "OPERACION";
		}
		}
		
		return "resultado";
		
		
	}
	
	private String operateSubexpressions(String expression) {
		
		String operatedExpression = expression;
		String subexpressions_regexp = "\\([^()]*\\)";
		String[] regexMatches = SintaxScanner.evaluateRegex(subexpressions_regexp, operatedExpression);
		
		while(regexMatches.length > 0 && regexMatches[0] != expression.trim()) {
			
			System.out.println("Hey "+operatedExpression);
			operatedExpression = operatedExpression.replaceFirst(subexpressions_regexp, operate(regexMatches[0]));
			regexMatches = SintaxScanner.evaluateRegex(subexpressions_regexp, operatedExpression);
			
		}
		
		return operatedExpression;	
		
		
	}

}
