package LispInterpreter;

import java.util.HashMap;

public class VariableFactory {

	private static HashMap<String, Data> variables = new HashMap<>();

	public static Data newVariable(String name, Object value) throws IllegalArgumentException {

		if (name == null || value == null)
			throw new IllegalArgumentException();

		Data data;

		// objeto string
		if (value instanceof String) {

			String stringValue = String.valueOf(value).trim();

			// valida si es un numero
			if (Data.isNumber(stringValue)) {

				// numero decimal
				if (SintaxScanner.hasMatches("\\d+\\.\\d+", stringValue))
					data = new Data(Double.parseDouble(stringValue));
				else
					data = new Data(Integer.parseInt(stringValue));
			} else {
				// si tiene formato "String" o 'String"
				if (Data.isString(stringValue))
					stringValue = stringValue.substring(1, stringValue.length() - 1); //elimina las comillas
				
				data = new Data(stringValue);
			}

		} else
			data = new Data(value);

		variables.put(name.trim(), data);
		return data;
	}

	public static Data getVariable(String name) throws ReferenceException {

		if (variables.containsKey(name))
			return variables.get(name);
		else
			throw new ReferenceException("Variable " + name + " no tiene valor.");
	}

}
