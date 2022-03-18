package LispInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SintaxScanner {

	/**
	 * Se encarga de evaluar una expresion y determinar el tipo de operacion a realizar.
	 * @param expression. String
	 * @return int. 0: operacion aritmetica.
	 */
	public static int getState(String expression) {

		System.out.println(expression);
		if (match("^\\(\\s*[\\+\\-\\/\\*](\\s*\\w*[.]*(\\(.*\\))*)*\\)", expression))
			return 1;
		return 0;
	}

	/**
	 * Se encarga de verificar si una expresion cumple con un formato determinado.
	 * @param regex. String Expresión regular.
	 * @param expression
	 * @return boolean. 
	 */
	public static boolean match(String regex, String expression) {
		return evaluateRegex(regex, expression).length > 0;
	}
	
	/**
	 * Se encarga de encontrar las coincidencias con una expresion regular.
	 * @param regex
	 * @param expression
	 * @return String[]. Arreglo con todas las coincidencias encontradas.
	 */
	public static String[] evaluateRegex(String regex, String expression) {

		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(expression);
		ArrayList<String> results = new ArrayList<>();
		
			while(matcher.find()) {
				results.add(matcher.group());
			}
			
			return results.toArray(new String[results.size()]);
	}

}
