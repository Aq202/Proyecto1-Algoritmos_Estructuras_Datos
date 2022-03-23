package LispInterpreter;

import java.util.Arrays;

public class Interpreter {
	
	public static final String[] RESERVER_WORDS = {"setq"};
	
	public Data operate(String expression) throws InvalidExpression, ReferenceException{
		
		int state = SintaxScanner.getState(expression);
		String mainExpression;
		
		switch(state) {
		case 1:{ //operacion aritmetica
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 1);
			return new Data(Operations.arithmeticOperation(mainExpression));
		}
		case 2:{ //nueva variable
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 2);
			return Operations.assignVariable(mainExpression);
		}
		
		case 3:{
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 1);
			return new Data (Operations.booleanOperation(mainExpression));
		}
		
		case 6:{//evaluar variable
			return VariableFactory.getVariable(expression);
		}	
		
		
		}
		
		throw new InvalidExpression();
		
		
	}
	
	/**
	 * Se encarga de ejecutar las operaciones hijas de una expresion.
	 * @param expression 
	 * @return Retorna la expresion con los valores correspondientes sustituidos.
	 * @throws ReferenceException 
	 */
	private String operateSubexpressions(String expression, int argumentsNumber) throws InvalidExpression, ReferenceException{
		
		String operatedExpression = expression, 
				arguments = "";
		
		final String firstWord_regex = "^\\s*[^\\s]+";
		
		for(int i = 0; i < argumentsNumber; i++) {
			
			//get and save arguments(first word)
			String[] argumentMatches = SintaxScanner.evaluateRegex(firstWord_regex, operatedExpression);
			if(argumentMatches.length > 0) arguments += " " + argumentMatches[0];
			//delete from expression to operate
			operatedExpression = operatedExpression.replaceFirst(firstWord_regex, "");
		}
		
		//selecciona las (operaciones), "strings", 'strings' o variables.
		String subexpressions_regexp = "(\\([^()]*\\))|(\"[^\"]*\")|('[^']*')|(\\b(?<!\")[a-z]\\w*(?!\")\\b)"; 
		String[] regexMatches = SintaxScanner.evaluateRegex(subexpressions_regexp, operatedExpression);
		
		int matchIndex = 0;
		while(regexMatches.length > matchIndex && regexMatches[matchIndex] != expression.trim()) {
			
			if(Data.isString(regexMatches[matchIndex])) {
				matchIndex++;
				continue;
			}
			
			operatedExpression = operatedExpression.replaceFirst(subexpressions_regexp, operate(regexMatches[matchIndex]).toString());
			regexMatches = SintaxScanner.evaluateRegex(subexpressions_regexp, operatedExpression);
			matchIndex = 0;
			
		}
		//return full expression
		return (arguments + " " + operatedExpression).trim();	
		
		
	}

}
