package LispInterpreter;

public class Data {
	
	private Object value;
	private String description;
	
	public Data(Object value, String description) {
		this.value = value;
		this.description = description;
	}
	
	public Data(Object value) {
		this.value = value;
		this.description = null;
	}
	
	public Object getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public String toString() {
		if(value instanceof Boolean)
			return (Boolean)value == true ? "T" : "NIL";
		return value != null ? value+"" : null;
	}
	
	public static boolean isNumber(String expression) {

		String[] matches = SintaxScanner.evaluateRegex("(?<!\\S)([-+]{0,1}([\\d^.]+)|((\\d+\\.\\d+)))(?!\\S)", expression);

		// verificar si hay numeros en la expresion
		if (matches.length > 0) {

			// el numero corresponde a toda la cadena
			if (expression.trim().equals(matches[0].trim()))
				return true;

		}

		return false;

	}
	
	public static boolean isString(String expression) {
		
		String[] matches = SintaxScanner.evaluateRegex("(\"[^\"]*\")|('[^']*')", expression);

		// verificar si hay Strings en la expresion
		if (matches.length > 0) {

			// el string corresponde a toda la cadena
			if (expression.trim().equals(matches[0].trim()))
				return true;
		}

		return false;
	}
	
	public static boolean isBoolean(String expression) {
		String[] matches = SintaxScanner.evaluateRegex("(?<!\\\\S)(t)|(nil)(?!\\\\S)", expression);
		
		// verificar si hay booleanos en la expresion
		if(matches.length > 0) {
			
			//el boolean corresponde a toda la cadena
			if(expression.trim().equals(matches[0].trim()))
				return true;
		}
		
		return false;
	}
}
