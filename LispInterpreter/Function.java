package LispInterpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Function {

	private String expression, name, functionBodyExpression;
	private String[] functionParameters;

	public Function(String expression) throws InvalidExpression {

		this.expression = Operations.getListContent(expression);
		this.name = getName();
		this.destructureExpression();
	}

	public String getName() throws InvalidExpression {

		if (name != null)
			return name;

		try {
			String[] params = Operations.getListParameters(expression, 3);
			return params[1].trim();
		} catch (NullPointerException ex) {
			throw new InvalidExpression("Falta de parametros para definicion de funciones.");
		}

	}

	private void destructureExpression() {

		// get function params
		String[] expressionParams = Operations.getListParameters(expression, 3); // defun, name, (params)
		functionParameters = SintaxScanner.evaluateRegex("[^()\"' ]+", Operations.getListContent(expressionParams[2]));

		// get function body expressions
		functionBodyExpression = Operations.getListBody(expression, 3);

	}

	/**
	 * Se encarga de determinar el nombre de variables temporales disponibles.
	 * 
	 * @param variableName
	 * @return String
	 * @throws InvalidExpression
	 */
	private String getVariableTemporaryName(String variableName) throws InvalidExpression {

		String name = variableName + "@" + this.getName();
		double cont = 1;
		while (VariableFactory.contains(name + (this.hashCode() * cont))) {
			cont += 0.001;
		}
		return name + (this.hashCode() * cont);
	}

	public Data execute(String... params) throws InvalidExpression, ReferenceException {

		// Muy pocos argumentos proporcionados
		if (functionParameters.length > 0 && (params == null || params.length < functionParameters.length))
			throw new InvalidExpression(
					String.format("Argumentos insuficientes para ejecutar funcion %s (%s en lugar de $s).", getName(),
							params != null ? params.length : 0, functionParameters.length));

		// Muchos argumentos proporcionados
		if (params != null && params.length > functionParameters.length)
			throw new InvalidExpression(String.format("Muchos argumentos para ejecutar funcion %s.", getName()));

		for (int index = 0; index < functionParameters.length; index++) {

			String temporaryVariable = getVariableTemporaryName(functionParameters[index]);

			// almacenar variable temporal
			VariableFactory.newVariable(temporaryVariable, params[index]);

			// reemplazar nombre por variable temporal
			functionBodyExpression =  functionBodyExpression.replaceFirst(Pattern.quote(functionParameters[index]),
					Matcher.quoteReplacement(temporaryVariable));

		}
		
		//ejecutar funcion
		return Interpreter.operate(functionBodyExpression);

	}

}
