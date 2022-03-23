package LispInterpreter;

import java.util.ArrayList;

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
			return integerOperation(operator, operationBody);

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
			//selecciona la ultima palabra/numero o "String"
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
							total /= value;
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

	private static int integerOperation(String operator, String expressionBody) throws InvalidExpression {
		return (int) doubleOperation(operator, expressionBody);
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
			if (elem.trim() != "") numbers.add(elem.trim());
		}
		boolean current_result = true;
		for (int counter = 0; counter < numbers.size()-1; counter++) {
			if (!current_result) break;
			if (SintaxScanner.hasMatches("(?<!\\S)([-]{0,1}([\\d^.]+)|((\\d+\\.\\d+)))(?!\\S)", numbers.get(counter))) {
				
				if (SintaxScanner.hasMatches("(?<!\\S)([-]{0,1}([\\d^.]+)|((\\d+\\.\\d+)))(?!\\S)", numbers.get(counter+1))) {
					double current_num = Double.parseDouble(numbers.get(counter));
					double next_num = Double.parseDouble(numbers.get((counter+1)));
					switch (operator.trim()) {
					
					case "<":{
						if (current_num < next_num) {
							current_result = true;
						}
						else {
							current_result = false;
						}
						break;
					} 
					
					case ">":{
						if (current_num > next_num) {
							current_result = true;
						}
						else {
							current_result = false;
						}
						break;
					}
					
					case "=": {
						if (current_num == next_num) {
							current_result = true;
						}
						else {
							current_result = false;
						}
						break;
					}
					
					default:
						throw new InvalidExpression("Operador invalido.");
					} 
				}
				else {
					throw new InvalidExpression(numbers.get(counter+1) + " no es un numero."); // no es un numero
				}
				
				
			}
			else {
				throw new InvalidExpression(numbers.get(counter) + " no es un numero."); // no es un numero
			}
		}
		return current_result;
		
	}
	
	public static String getListContent(String expression) throws IndexOutOfBoundsException {

		return SintaxScanner.evaluateRegex("(?<=^\\().+(?=\\)$)", expression)[0];
	}

}
