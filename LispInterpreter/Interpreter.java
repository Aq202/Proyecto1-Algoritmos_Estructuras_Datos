package LispInterpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

	public static final String[] RESERVER_WORDS = { "setq" };

	public static Data operate(String expression) throws InvalidExpression, ReferenceException {

		int state = SintaxScanner.getState(expression.trim());
		String mainExpression;

		switch (state) {
		case 1: { // operacion aritmetica
			mainExpression = operateSubexpressions(expression, 1);
			return new Data(Operations.arithmeticOperation(mainExpression));
		}
		case 2: { // nueva variable
			mainExpression = operateSubexpressions(Operations.getListContent(expression), 2);
			return Operations.assignVariable(mainExpression);
		}

		case 6: {// evaluar variable
			return VariableFactory.getVariable(expression);
		}
		
		case 7: { //dato primitivo
			return new Data(Data.castValue(expression));
		}

		}

		throw new InvalidExpression();

	}

	/**
	 * Se encarga de ejecutar las operaciones hijas de una expresion.
	 * 
	 * @param expression
	 * @return Retorna la expresion con los valores correspondientes sustituidos.
	 * @throws ReferenceException
	 */
	private static String operateSubexpressions(String expression, int argumentsNumber)
			throws InvalidExpression, ReferenceException {

		String arguments = "", operatedExpression;

		try {
			for (String argument : Operations.getListParameters(expression, argumentsNumber)) {
				arguments += " " + argument;
			}
			operatedExpression = Operations.getListBody(expression, argumentsNumber);
		} catch (NullPointerException ex) {
			throw new InvalidExpression();
		}

		// selecciona las (operaciones), "strings", 'strings' o variables.
		String[] regexMatches = getChildExpressions(operatedExpression);

		int matchIndex = 0;
		while (regexMatches.length > matchIndex && !regexMatches[matchIndex].equals(expression.trim())) {

			String valueToOverwrite = regexMatches[matchIndex];
			String newValue = operate(regexMatches[matchIndex]).toString();
			operatedExpression = operatedExpression.replaceFirst(Pattern.quote(valueToOverwrite),
					Matcher.quoteReplacement(newValue));

			matchIndex++;

		}

		// obtener "Strings", 'strings' y variables
		final String childElements_regex = "(\"[^\"]*\")|('[^']*')|((?<!\")[a-z][^\"'() ]*(?!\"))";
		String[] childElements = SintaxScanner.evaluateRegex(childElements_regex, operatedExpression);

		for (String element : childElements) {

			if (!Data.isString(element)) {
				// evaluar variable
				String variableValue = operate(element).toString();
				operatedExpression = operatedExpression.replaceFirst(Pattern.quote(element),
						Matcher.quoteReplacement(variableValue));
			}
		}

		// return full expression
		return (arguments + " " + Operations.getListContent(operatedExpression.trim()).trim());

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
							childExpression = "";
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

		}
		return expressions.toArray(new String[expressions.size()]);
	}

}
