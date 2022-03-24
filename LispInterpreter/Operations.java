package LispInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operations {

	/**
	 * 
	 * @param expressionContent. Contenido sin () de la expresion aritmetica
	 *                           principal
	 * @return
	 * @throws InvalidExpression
	 */
	public static Object arithmeticOperation(String expressionContent) throws InvalidExpression {

		// obtiene primer signo
		final String operator = SintaxScanner.evaluateRegex("^\\s*[\\+\\-*\\/]", expressionContent)[0].trim();
		// obtiene todo menos primer signo
		final String operationBody = SintaxScanner.evaluateRegex("(?<=[\\+\\-*\\/]).+", expressionContent)[0];

		if (SintaxScanner.hasMatches("\\d+\\.\\d+", operationBody)) { // verifica si hay decimales

			// operacion con decimales
			return doubleOperation(operator, operationBody);

		} else {

			// operacion con enteros
			double result = doubleOperation(operator, operationBody);

			if (Data.isDouble(String.valueOf(result))) {
				return result;
			} else {
				return (int) result;
			}
		}

	}

	/**
	 * 
	 * @param expressionContent. El contenido sin () de la expresion setq
	 * @return
	 * @throws InvalidExpression
	 */
	public static Data assignVariable(String expressionContent) throws InvalidExpression {

		String variableName, value;

		try {
			variableName = SintaxScanner.evaluateRegex("(?<=setq)\\s+(\\d*[a-z]\\w*)+", expressionContent)[0];
			// selecciona la ultima palabra/numero o "String"
			value = SintaxScanner.evaluateRegex("((\"[^\"]*\")|('[^']*')|[^\\s]+)\\s*$", expressionContent.trim())[0];

		} catch (IndexOutOfBoundsException ex) {
			throw new InvalidExpression();
		} catch (NullPointerException ex) {
			throw new InvalidExpression();
		}

		try {
			return VariableFactory.newVariable(variableName, value);
		} catch (Exception ex) {
			throw new InvalidExpression();
		}

	}

	private static double doubleOperation(String operator, String expressionBody) throws InvalidExpression {

		if (operator == null)
			throw new InvalidExpression("Operador invalido.");
		if (expressionBody == null)
			throw new InvalidExpression();

		String[] elements = expressionBody.split(" ");
		Double total = null;

		for (String elem : elements) {

			if (elem.trim() != "") {

				// valida si es un numero
				if (SintaxScanner.hasMatches("(?<!\\S)([-]{0,1}([\\d^.]+)|((\\d+\\.\\d+)))(?!\\S)", elem)) {
					Double value = Double.parseDouble(elem);

					if (total == null)
						total = value;
					else {

						switch (operator.trim()) {

						case "+": {
							total += value;
							break;
						}
						case "-": {
							total -= value;
							break;
						}
						case "*": {
							total *= value;
							break;
						}
						case "/": {
							if (value != 0)
								total /= value;
							else
								throw new InvalidExpression("Division por cero invalida.");
							break;
						}
						default:
							throw new InvalidExpression("Operador invalido.");
						}

					}
				} else
					throw new InvalidExpression(elem + " no es un numero."); // no es un numero

			}

		}
		return total;
	}

	/**
	 * Retorna el contenido de una expresion sin los () de hasta afuera. Si no los
	 * tiene retorna la expresion original.
	 * 
	 * @param expression
	 * @return
	 * @throws IndexOutOfBoundsException
	 */
	public static String getListContent(String expression) throws IndexOutOfBoundsException {

		String[] match = SintaxScanner.evaluateRegex("(?<=^\\().+(?=\\)$)", expression);
		return match.length > 0 ? match[0] : expression;
	}

	/**
	 * Retorna los argumentos n primeras palabras o (param) sin hijos.
	 * 
	 * @param expression
	 * @param argumentsNumber
	 * @return String[]
	 */
	public static String[] getListParameters(String expression, int argumentsNumber) throws NullPointerException {

		String operatedExpression = Operations.getListContent(expression);
		ArrayList<String> arguments = new ArrayList<String>();

		final String firstWord_regex = "^\\s*([^\\s()]+|(\\([^()\"']*\\)))";

		for (int i = 0; i < argumentsNumber; i++) {

			// get and save arguments(first word)
			String[] argumentMatches = SintaxScanner.evaluateRegex(firstWord_regex, operatedExpression);

			if (argumentMatches != null && argumentMatches.length > 0) {
				arguments.add(argumentMatches[0].trim());
				operatedExpression = operatedExpression.replaceFirst(Pattern.quote(argumentMatches[0]),
						Matcher.quoteReplacement(""));
			} else
				break;
		}

		return arguments.toArray(new String[arguments.size()]);
	}

	public static String getListBody(String expression, int argumentsNumber) throws NullPointerException {

		expression = Operations.getListContent(expression);

		String[] parameters = getListParameters(expression, argumentsNumber);
		for (String param : parameters) {
			expression = expression.replaceFirst(Pattern.quote(param), Matcher.quoteReplacement(""));
		}
		return expression.trim();
	}

}
