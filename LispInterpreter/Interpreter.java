package LispInterpreter;

public class Interpreter {
	
	public Data operate(String expression) throws InvalidExpression{
		
		int state = SintaxScanner.getState(expression);
		String mainExpression = operateSubexpressions(Operations.getListContent(expression));
		
		switch(state) {
		case 1:{
			
			return new Data(Operations.arithmeticOperation(mainExpression));
		}
		}
		
		throw new InvalidExpression();
		
		
	}
	
	/**
	 * Se encarga de ejecutar las operaciones hijas de una expresion.
	 * @param expression 
	 * @return Retorna la expresion con los valores correspondientes sustituidos.
	 */
	private String operateSubexpressions(String expression) throws InvalidExpression{
		
		String operatedExpression = expression;
		String subexpressions_regexp = "(\\([^()]*\\))|(\\b[a-z]\\w*)"; //selecciona las operaciones y variables
		String[] regexMatches = SintaxScanner.evaluateRegex(subexpressions_regexp, operatedExpression);
		
		while(regexMatches.length > 0 && regexMatches[0] != expression.trim()) {
			
			operatedExpression = operatedExpression.replaceFirst(subexpressions_regexp, operate(regexMatches[0]).toString());
			regexMatches = SintaxScanner.evaluateRegex(subexpressions_regexp, operatedExpression);
			
		}
		
		return operatedExpression;	
		
		
	}

}
