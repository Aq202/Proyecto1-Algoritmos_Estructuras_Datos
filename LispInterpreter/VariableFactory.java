package LispInterpreter;

import java.util.HashMap;

/**
 * Clase VariableFactory. Permite manipular las variables del interprete.
 * 
 * @author Diego Morales, Erick Guerra, Pablo Zamora
 * @version 25/03/2022
 *
 */
public class VariableFactory {

	private static HashMap<String, Data> variables = new HashMap<>();

	/**
	 * Se encarga de crear/reemplazar y almacenar una variable.
	 * 
	 * @param name  String. Nombre de la variable.
	 * @param value Ojbect. Valor a almacenar.
	 * @return Data
	 * @throws IllegalArgumentException
	 */
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

	/**
	 * Se encarga de obtener el valor correspondiente a una variable.
	 * 
	 * @param name String. Nombre de la variable.
	 * @return Data
	 * @throws ReferenceException
	 * @throws IllegalArgumentException
	 */
	public static Data getVariable(String name) throws ReferenceException, IllegalArgumentException {

		if (name == null)
			throw new IllegalArgumentException();

		if (variables.containsKey(name))
			return variables.get(name);
		else
			throw new ReferenceException("Variable " + name + " no tiene valor.");
	}

	/**
	 * Se encarga de eliminar la referencia de una variable.
	 * 
	 * @param name String. Nombre de la variable.
	 * @return Data
	 * @throws ReferenceException
	 */
	public static Data deleteVariable(String name) throws ReferenceException {
		if (name == null)
			throw new IllegalArgumentException();

		if (variables.containsKey(name))
			return variables.remove(name);
		else
			throw new ReferenceException("Variable " + name + " no tiene valor.");
	}

	/**
	 * Permite determinar si existe una variable.
	 * 
	 * @param name String. Nombre de la variable.
	 * @return Data
	 */
	public static boolean contains(String name) {
		return variables.containsKey(name);
	}
}
