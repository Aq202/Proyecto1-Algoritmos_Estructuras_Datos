package LispInterpreter;

/**
 * Permite la transmision y manipulacion de objetos de distintos tipos.
 * 
 * @author Diego Morales, Erick Guerra, Pablo Zamora
 * @version 25/03/2022
 *
 */
public class Data {

	private Object value;
	private String description;
	private boolean blockPrint = false;

	/**
	 * Metodo constructor de la clase
	 * 
	 * @param value       Object.
	 * @param description String
	 */
	public Data(Object value, String description) {
		this.value = value;
		this.description = description;
	}

	/**
	 * Metodo constructor de la clase
	 * 
	 * @param value Object.
	 */
	public Data(Object value) {
		this.value = value;
		this.description = null;
	}

	/**
	 * Metodo getter de la propiedad value.
	 * 
	 * @return Object.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Metodo getter de la propiedad Description.
	 * 
	 * @return String.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Metodo que permite obtener el valor en formato String.
	 * 
	 * @return String.
	 */
	@Override
	public String toString() {
		if (value instanceof Boolean)
			return (Boolean) value == true ? "T" : "NIL";
		return value != null ? value + "" : null;
	}

	/**
	 * Metodo que permite validar si una cadena corresponde al formato de un numero.
	 * 
	 * @param expression
	 * @return boolean.
	 */
	public static boolean isNumber(String expression) {

		String[] matches = SintaxScanner.evaluateRegex("(?<!\\S)([-+]{0,1}([\\d^.]+)|((\\d+\\.\\d+)))(?!\\S)",
				expression);

		// verificar si hay numeros en la expresion
		if (matches.length > 0) {

			// el numero corresponde a toda la cadena
			if (expression.trim().equals(matches[0].trim()))
				return true;

		}

		return false;
	}

	/**
	 * Se encarga de verificar si un numero posee decimales diferentes a cero.
	 * 
	 * @param expression
	 * @return boolean.
	 */
	public static boolean isDouble(String expression) {
		return SintaxScanner.hasMatches("^([-+]*\\d+\\.(\\d*[1-9]\\d*)+)$",
				expression != null ? expression.trim() : "");
	}

	/**
	 * Se encarga de validar si una expresion corresponde al formato 'string' o
	 * "String".
	 * 
	 * @param expression
	 * @return boolean.
	 */
	public static boolean isString(String expression) {

		String[] matches = SintaxScanner.evaluateRegex("(\\\"\\\"[^\\\"]*\\\"\\\")|(\\\"[^\\\"]*\\\")|('[^']*')",
				expression);

		// verificar si hay Strings en la expresion
		if (matches.length > 0) {

			// el string corresponde a toda la cadena
			if (expression.trim().equals(matches[0].trim()))
				return true;
		}

		return false;
	}

	/**
	 * Valida si una expresion corresponde al dato primivo booleano T o NIL.
	 * 
	 * @param expression
	 * @return
	 */
	public static boolean isBoolean(String expression) {

		if (expression == null)
			return false;
		expression = expression.trim();

		return (expression.equalsIgnoreCase("T") || expression.equalsIgnoreCase("NIL"));

	}

	/**
	 * Se encarga de transformar una expresion a una insancia de un objeto, segun el
	 * formato de la misma.
	 * 
	 * @param value
	 * @return Object.
	 */
	public static Object castValue(String value) {

		value = value.trim();

		// valida si es un numero
		if (Data.isNumber(value)) {

			// numero decimal
			if (SintaxScanner.hasMatches("\\d+\\.\\d+", value))
				return Double.parseDouble(value);
			else
				return Integer.parseInt(value);
		} else {

			// valor bool
			if (isBoolean(value)) {
				if (value.equalsIgnoreCase("T"))
					return true;
				else
					return false;
			}

			// si tiene formato "String" o 'String"
			if (Data.isString(value))
				return value;

			return new IndeterminateObject(value);
		}

	}

	/**
	 * Indica que el contenido NO debe de imprimirse en el Main
	 */
	public void blockPrint() {
		this.blockPrint = true;
	}

	/**
	 * Indica si el contenido debe de imprimirse en el Main
	 * 
	 * @return boolean.
	 */
	public boolean getBlockPrint() {
		return this.blockPrint;
	}
}
