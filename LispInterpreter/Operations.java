package LispInterpreter;

import java.util.ArrayList;
import java.util.Arrays;
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
	 * @param expressionContent. El contenido sin () de la expresion quote
	 * @return Data
	 * @throws InvalidExpression
	 */
	public static Data quote(String expressionContent) throws InvalidExpression {
		String parameter;

		try {
			parameter = SintaxScanner.evaluateRegex("\\s*(?<=(quote\\s|'))((\\(.+\\))|(.+))", expressionContent)[0]
					.trim();
			if (Data.isNumber(parameter))
				return new Data(Integer.parseInt(parameter), "notNested");
			else if (Data.isString(parameter))
				return new Data(parameter.substring(1, parameter.length() - 1), "notNested");
			else if (Data.isBoolean(parameter)) {
				boolean value = parameter.equals("t") ? true : false;
				return new Data(value, "notNested");
			} else
				return new Data(parameter, "notNested");
		} catch (IndexOutOfBoundsException ex) {
			throw new InvalidExpression();
		} catch (NullPointerException ex) {
			throw new InvalidExpression();
		}
	}

	/**
	 * 
	 * @param expressionContent. El contenido sin () de la expresion list
	 * @return Data
	 * @throws InvalidExpression
	 */
	public static Data toList(String expressionContent) throws InvalidExpression {
		String list;
		try {
			list = SintaxScanner.evaluateRegex("(?<=list)\\s+((\\(.*\\))|([^ ]))+", expressionContent)[0].trim();
			if (Data.isBoolean(list))
				return new Data(list.equals("t") ? "(T)" : "(NIL)");
			return new Data("(" + list + ")");
		} catch (IndexOutOfBoundsException ex) {
			throw new InvalidExpression();
		} catch (NullPointerException ex) {
			throw new InvalidExpression();
		}
	}

	/**
	 * 
	 * @param expressionContent. El contenido sin () de la expresion atom y lisp
	 * @return Data
	 * @throws InvalidExpression
	 */
	public static Data checkAtom(String expressionContent, String predicate) throws InvalidExpression {
		String parameter;
		boolean result;
		try {
			if (SintaxScanner.match("(atom|listp)\\s+\\(.+\\)", expressionContent))
				result = false;
			else
				result = true;
			result = predicate.equals("atom") ? result : !result;
			return new Data(result);
		} catch (IndexOutOfBoundsException ex) {
			throw new InvalidExpression();
		} catch (NullPointerException ex) {
			throw new InvalidExpression();
		}
	}

	/**
	 * 
	 * @param expressionContent. El contenido sin () de la expresion write
	 * @return Data
	 * @throws InvalidExpression
	 */
	public static Data print(String expressionContent) throws InvalidExpression {
		String print;
		try {
			print = SintaxScanner.evaluateRegex("(?<=write)\\s+((\\(.*\\))|(\\\".*\\\")|([^ ]))+", expressionContent)[0]
					.trim();
			if (Data.isBoolean(print))
				return new Data(print.toLowerCase().equals("t") ? "T" : "NIL", "print");
			return new Data(print, "print");
		} catch (IndexOutOfBoundsException ex) {
			throw new InvalidExpression();
		} catch (NullPointerException ex) {
			throw new InvalidExpression();
		}
	}

	/**
	 * 
	 * @param expressionContent. El contenido sin () de la expresion setq
	 * @return
	 * @throws InvalidExpression
	 */
	public static Data assignVariable(String expressionContent) throws InvalidExpression, ReferenceException {

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
			if (!Data.isString(value) && !Data.isBoolean(value) && !Data.isNumber(value))
				value = (String) VariableFactory.getVariable(value).getValue();
			if (Data.isBoolean(value))
				value = value.toLowerCase().equals("t") ? "T" : "NIL";
			return VariableFactory.newVariable(variableName, value);
		} catch (ReferenceException ex) {
			throw ex;
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

	public static Data evaluateFunction(String expression) throws ReferenceException, InvalidExpression {
		expression = getListContent(expression);

		String[] expressionParams = getListParameters(expression, 1);

		String functionName = expressionParams[0];
		Data variableValue = VariableFactory.getVariable(functionName.trim());

		// validar que se trate de una funcion
		if (!(variableValue.getValue() instanceof Function))
			throw new InvalidExpression(functionName + "no es una funcion.");

		Function func = (Function) variableValue.getValue();

		String operatedFunctionParams = Interpreter.operateSubexpressions(expression, 1, true);
		final String primitiveValues_regex = "(([-+]{0,1}([\\d^.]+)|((\\d+\\.\\d+)))|(\"[^\\\"]*\")|('[^']*'))+";
		String[] functionParamValues = SintaxScanner.evaluateRegex(primitiveValues_regex, operatedFunctionParams);

		return func.execute(functionParamValues);
	}

	public static boolean booleanOperation(String expressionContent) throws InvalidExpression {
		// obtiene primer signo
		final String operator = SintaxScanner.evaluateRegex("^\\s*[\\<\\=*\\>]", expressionContent)[0].trim();
		// obtiene todo menos primer signo
		final String expressionBody = SintaxScanner.evaluateRegex("(?<=[\\<\\=*\\>]).+", expressionContent)[0];

		if (operator == null)
			throw new InvalidExpression("Operador invalido.");
		if (expressionBody == null)
			throw new InvalidExpression();

		String[] elements = expressionBody.split(" ");
		ArrayList<String> numbers = new ArrayList<String>();
		for (String elem : elements) {
			if (elem.trim() != "")
				numbers.add(elem.trim());
		}
		boolean current_result = true;
		for (int counter = 0; counter < numbers.size() - 1; counter++) {
			if (!current_result)
				break;
			if (SintaxScanner.hasMatches("(?<!\\S)([-]{0,1}([\\d^.]+)|((\\d+\\.\\d+)))(?!\\S)", numbers.get(counter))) {

				if (SintaxScanner.hasMatches("(?<!\\S)([-]{0,1}([\\d^.]+)|((\\d+\\.\\d+)))(?!\\S)",
						numbers.get(counter + 1))) {
					double current_num = Double.parseDouble(numbers.get(counter));
					double next_num = Double.parseDouble(numbers.get((counter + 1)));
					switch (operator.trim()) {

					case "<": {
						if (current_num < next_num) {
							current_result = true;
						} else {
							current_result = false;
						}
						break;
					}

					case ">": {
						if (current_num > next_num) {
							current_result = true;
						} else {
							current_result = false;
						}
						break;
					}

					case "=": {
						if (current_num == next_num) {
							current_result = true;
						} else {
							current_result = false;
						}
						break;
					}

					default:
						throw new InvalidExpression("Operador invalido.");
					}
				} else {
					throw new InvalidExpression(numbers.get(counter + 1) + " no es un numero."); // no es un numero
				}

			} else {
				throw new InvalidExpression(numbers.get(counter) + " no es un numero."); // no es un numero
			}
		}
		return current_result;

	}

	public static Data condOperation(String expressionContent) throws InvalidExpression, ReferenceException {

		try {
			String mainExpression = Operations.getListBody(expressionContent, 1);
			String clauses[] = Interpreter.getChildExpressions(mainExpression);
			for (String clause : clauses) {
				
				clause = getListContent(clause);

				String condition = Interpreter.operateSubexpressions(Operations.getListParameters(clause, 1)[0],0,true);
				String action = Interpreter.operateSubexpressions(Operations.getListBody(clause, 1),0,true);

				Data result = Interpreter.operate(condition);
				if (result.toString().equalsIgnoreCase("T")) {
					return new Data(Interpreter.operate(action));
				}
			}
			return new Data(false);

		} catch (IndexOutOfBoundsException ex) {
			System.out.println("cond" + ex);
			throw new InvalidExpression();
		}
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
		expression = expression.trim();
		String[] match = SintaxScanner.evaluateRegex("(?<=^\\().+(?=\\)$)", expression);
		return match.length > 0 ? match[0] : expression;
	}

	/**
	 * Retorna los argumentos n primeras palabras o (param) sin hijos.
	 * 
	 * @param expressionContent
	 * @param argumentsNumber
	 * @return String[]
	 */
	public static String[] getListParameters(String expressionContent, int argumentsNumber)
			throws NullPointerException {

		String operatedExpression = expressionContent;
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

	public static String getListBody(String expressionContent, int argumentsNumber) throws NullPointerException {
		;

		String[] parameters = getListParameters(expressionContent, argumentsNumber);
		for (String param : parameters) {
			expressionContent = expressionContent.replaceFirst(Pattern.quote(param), Matcher.quoteReplacement(""));
		}
		return expressionContent.trim();
	}

}
