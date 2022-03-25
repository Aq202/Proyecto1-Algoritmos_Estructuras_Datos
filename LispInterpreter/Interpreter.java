package LispInterpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

	public static final String[] RESERVER_WORDS = { "setq", "list", "listp", "t", "nil", "atom", "write", "cond",
			"defun", "quote" };

	public static Data operate(String expression) throws InvalidExpression, ReferenceException {

		int state = SintaxScanner.getState(expression);
		String mainExpression;

		switch (state) {
		case 1: { // operacion aritmetica
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 1, true);
			return new Data(Operations.arithmeticOperation(mainExpression));
		}
		case 2: { // nueva variable
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 2, true);
			return Operations.assignVariable(mainExpression);
		}

		case 3: {// predicado Atom
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 1, true);
			return Operations.checkAtom(mainExpression, "atom");
		}

		case 4: { // Nueva funcion
			Function func = new Function(expression);
			return VariableFactory.newVariable(func.getName(), func);
		}

		case 5: { // Predicado lisp
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 1, true);
			return Operations.checkAtom(mainExpression, "lisp");
		}

		case 6: {// Predicado list
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 1, true);
			return Operations.toList(mainExpression);
		}

		case 7: { // Instruccion write
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 1, true);
			return Operations.print(mainExpression);
		}

		case 8: { // Instruccion QUOTE
			if (expression.charAt(0) == '(')
				mainExpression = operateSubexpressions(Operations.getListContent(expression), 1, false);
			else
				mainExpression = operateSubexpressions(expression, 1, false);
			return Operations.quote(mainExpression);
		}

		case 9: { // operacion logica
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 1, true);
			return new Data(Operations.booleanOperation(mainExpression));
		}

		case 10: { // COND
			return Operations.condOperation(Operations.getListContent(expression));
		}
		case 11: { // EQUAL
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 1, true);
			String equalExpression = mainExpression.replace("equal", "=");
			return new Data(Operations.booleanOperation(equalExpression));
		}

		case 12: { // evaluar funcion
			return Operations.evaluateFunction(expression);
		}

		case 13: { // dato primitivo
			return new Data(Data.castValue(expression));
		}

		case 14: {// evaluar variable
			return VariableFactory.getVariable(expression);
		}

		default: { //buscar opciones
			String content = Operations.getListContent(expression);
			
			if(!expression.equals(content)) {
				
				//verificar si es un dato primitivo
				if(SintaxScanner.getState(content) == 13)
					return new Data(content);
				
				//evaluar contenido de la lista
				return operate(content);
			}
			
		}

		}

		throw new InvalidExpression();

	}

	/**
	 * Se encarga de ejecutar las operaciones hijas de una expresion.
	 * 
	 * @param expressionContent
	 * @return Retorna la expresion con los valores correspondientes sustituidos.
	 * @throws ReferenceException
	 */

	public static String operateSubexpressions(String expressionContent, int argumentsNumber, boolean nested)
			throws InvalidExpression, ReferenceException {

		String arguments = "", operatedExpression;
		Data data;

		try {
			for (String argument : Operations.getListParameters(expressionContent, argumentsNumber)) {
				arguments += " " + argument;
			}
			operatedExpression = Operations.getListBody(expressionContent, argumentsNumber);
		} catch (NullPointerException ex) {
			throw new InvalidExpression();
		}
		if (arguments.trim().length() > 1 && arguments.trim().charAt(0) == '\'') {
			operatedExpression = arguments.trim().substring(1, arguments.trim().length());
			arguments = "'";
		}

		// selecciona las (operaciones), "strings", 'strings' o variables.
		String[] regexMatches = getChildExpressions(operatedExpression);

		int matchIndex = 0;
		while (regexMatches.length > matchIndex) {

			String valueToOverwrite = regexMatches[matchIndex];
			String newValue;
			if (!isNested(arguments, valueToOverwrite))
				return (arguments + " " + operatedExpression);
			
			data = operate(regexMatches[matchIndex]);
			
			nested = data.getDescription() != null && data.getDescription().contains("notNested") ? false : true;
			newValue = data.toString();
			newValue = newValue.equals("t") || newValue.equals("nil") ? "\"" + newValue + "\"" : newValue;
			operatedExpression = operatedExpression.replaceFirst(Pattern.quote(valueToOverwrite),
					Matcher.quoteReplacement(newValue));

			matchIndex++;

		}

		if (nested) {
			// obtener "Strings", 'strings' y variables
			final String childElements_regex = "(\"[^\"]*\")|('[^']*')|((?<!\")[a-z][^\"'() ]*(?!\"))";
			String[] childElements = SintaxScanner.evaluateRegex(childElements_regex, operatedExpression);

			for (String element : childElements) {

				if (!Data.isString(element)) {
					// evaluar variable
					String variableValue = element;
					if (!Arrays.asList(RESERVER_WORDS).contains(element.toLowerCase()))
						variableValue = operate(element).toString();
					// variableValue = variableValue instanceof String ? "\"" + variableValue + "\""
					// : variableValue;
					operatedExpression = operatedExpression.replaceFirst(Pattern.quote(element),
							Matcher.quoteReplacement(variableValue));
				}
			}
		}

		// return full expression
		return (arguments + " " + operatedExpression.trim());

	}

	/**
	 * Metodo que permite obtener las (expresiones) hermanas.
	 * 
	 * @param expressionContent
	 * @return String[].
	 */
	public static String[] getChildExpressions(String expressionContent) {

		ArrayList<String> expressions = new ArrayList<>();

		if (expressionContent != null) {

			String childExpression = "";
			int parenthesesCount = 0;

			// conjunto de "strings"
			ArrayList<String> strings = new ArrayList<>(
					Arrays.asList(SintaxScanner.evaluateRegex("(\"[^\"]*\")|('[^']*')", expressionContent)));

			for (int i = 0; i < expressionContent.length(); i++) {

				String currentChar = String.valueOf(expressionContent.charAt(i));

				int firstStringExpressionIndex = !strings.isEmpty() ? expressionContent.indexOf(strings.get(0)) : -1;
				// eliminar strings ya evaluados
				if (!strings.isEmpty() && i > (firstStringExpressionIndex + strings.get(0).length() - 1)) {
					strings.remove(0);
					firstStringExpressionIndex = !strings.isEmpty() ? expressionContent.indexOf(strings.get(0)) : -1;
				}

				// si esta recorriendo un strings, ignorar parentesis y supuestos hijos
				if (strings.isEmpty() || !(firstStringExpressionIndex <= i
						&& i <= (firstStringExpressionIndex + strings.get(0).length() - 1))) {

					// manejar inicio-final expresiones
					if (currentChar.equals("(")) {

						// si no hay una expresion padre, anadir contenido
						if (parenthesesCount == 0) {
							// expressions.add(childExpression.trim());
							childExpression = quoteFormat(childExpression) ? childExpression : "";
						}

						parenthesesCount++;
					} else if (currentChar.equals(")")) {
						parenthesesCount--;
						// si ya no hay parentesis por cerrar, agregar expresion
						if (parenthesesCount == 0) {

							childExpression += ")"; // anadir caracter final
							if (childExpression.trim().length() > 0)
								expressions.add(childExpression.trim()); // agregar expresion
							childExpression = "";
							continue; // evita duplicar el )
						}
					}
				}

				// anadir caracter
				childExpression += currentChar;

			}
			if (childExpression.length() > 0 && childExpression.charAt(0) == '\'')
				expressions.add(childExpression.trim());

		}
		return expressions.toArray(new String[expressions.size()]);
	}

	/**
	 * Verifica si la operacion a realizar es anidada o no
	 * 
	 * @param argument, subExpression
	 * @return boolean.
	 */
	private static boolean isNested(String argument, String subExpression) {
		String[] notNested = { "quote", "'" };
		if (Arrays.asList(notNested).contains(argument.trim())
				&& !(subExpression.contains("quote") || subExpression.contains("'")))
			return false;
		return true;
	}

	/**
	 * Verifica si la expresion contiene uno o varios single quote.
	 * 
	 * @param childExpression
	 * @return boolean.
	 */
	private static boolean quoteFormat(String childExpression) {
		if (SintaxScanner.match("'+", childExpression))
			return true;
		return false;
	}
	
	public static String[] validFormat(String[] fileContent) {
		ArrayList<String> program = new ArrayList<String>();
		String line = "";
		for(String row : fileContent) {
			if(!row.equals(null))
				line += row.trim();
		}
		if(line.charAt(0) == '\'') {
			program.add(line);
			return program.toArray(new String[program.size()]);
		}
		if(!line.equals("")) {
			String expression = "";
			int parenthesis = 0;
			for(int i = 0; i<line.length(); i++) {
				if(line.charAt(i) == '(')
					parenthesis++;
				if(parenthesis>0)
					expression += line.charAt(i);
				if(line.charAt(i) == ')') {
					parenthesis--;
					if(parenthesis == 0) {
						program.add(expression);
						expression = "";
					}
				}
			}
		}
		return program.toArray(new String[program.size()]);
		
	}

}
