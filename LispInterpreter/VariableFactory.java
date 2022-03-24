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

			data = new Data(Data.castValue(String.valueOf(value)));
			

		} else
			data = new Data(value);

		variables.put(name.trim(), data);
		return data;
	}

	public static Data getVariable(String name) throws ReferenceException, IllegalArgumentException {
		
		if(name == null) throw new IllegalArgumentException();

		if (variables.containsKey(name))
			return variables.get(name);
		else
			throw new ReferenceException("Variable " + name + " no tiene valor.");
	}

	public static boolean contains(String name) {
		return variables.containsKey(name);
	}
}
