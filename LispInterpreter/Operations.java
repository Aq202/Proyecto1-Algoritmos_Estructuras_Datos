package LispInterpreter;

public class Operations {

	public static Object arithmeticOperation(String expressionContent) throws InvalidExpression {

		
			//obtiene primer signo
			final String operator = SintaxScanner.evaluateRegex("^\\s*[\\+\\-*\\/]", expressionContent)[0].trim();
			//obtiene todo menos primer signo
			final String operationBody = SintaxScanner.evaluateRegex("(?<=[\\+\\-*\\/]).+", expressionContent)[0]; 

			if (SintaxScanner.match("\\d+\\.\\d+", operationBody)) { // verifica si hay decimales

				// operacion con decimales
				return doubleOperation(operator, operationBody);

			} else {

				// operacion con enteros
				return integerOperation(operator, operationBody);

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

				//valida si es un numero
				if (SintaxScanner.match("(?<!\\S)([-]{0,1}([\\d^.]+)|((\\d+\\.\\d+)))(?!\\S)", elem)) {
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
	
	private static int integerOperation(String operator, String expressionBody) throws InvalidExpression{
		return (int) doubleOperation(operator, expressionBody);
	}

	public static String getListContent(String expression) throws IndexOutOfBoundsException {

		return SintaxScanner.evaluateRegex("(?<=^\\().+(?=\\)$)", expression)[0];
	}

}
