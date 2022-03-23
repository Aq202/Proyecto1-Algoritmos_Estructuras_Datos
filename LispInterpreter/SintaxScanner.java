package LispInterpreter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SintaxScanner {

	/**
	 * Se encarga de evaluar una expresion y determinar el tipo de operacion a
	 * realizar.
	 * 
	 * @param expression. String
	 * @return int. 0: operacion aritmetica. 1: nueva variable. 6:evaluate variable
	 */
	public static int getState(String expression) {

		System.out.println(expression);
		if (match("^\\(\\s*[\\+\\-\\/\\*](\\s+\\w*[.]*(\\(.*\\))*)*\\)", expression))
			return 1;
		// verifica si es la instruccion (setq name value), donde value puede ser un
		// valor o una expresion a evaluar
		// (setq name (operators)|"string"|'String'|variable)
		if (match("^\\(\\s*setq\\s+(\\d*[a-z]\\w*)\\s*((\\(.+\\))|('[^']*')|(\"[^\"]*\")|[^\\s^\\(^\\)]+)\\s*\\)$",
				expression))
			return 2;
		if (match("^\\(\\s*[\\<\\>\\=]\\+(\\s*\\w*[.]*(\\(.*\\))*)*\\)", expression))
			return 3;
		if(match("(\\b(?<!\")[a-z]\\w*(?!\")\\b)", expression))
			return 6;

		return 0;
	}

	/**
	 * Se encarga de verificar si una expresion posee coincidencias
	 * 
	 * @param regex.     String Expresión regular.
	 * @param expression
	 * @return boolean.
	 */
	public static boolean hasMatches(String regex, String expression) {
		return evaluateRegex(regex, expression).length > 0;
	}

	/**
	 * Se encarga de verificar si una expresion coincide en su totalidad con la
	 * regex.
	 * 
	 * @param regex
	 * @param expression
	 * @return boolean.
	 */
	public static boolean match(String regex, String expression) {
		String[] matches = SintaxScanner.evaluateRegex(regex, expression);

		// verificar si hay Strings en la expresion
		if (matches.length > 0) {

			// el string corresponde a toda la cadena
			if (expression.trim().equals(matches[0].trim()))
				return true;
		}
		return false;
	}

	/**
	 * Se encarga de encontrar las coincidencias con una expresion regular.
	 * 
	 * @param regex
	 * @param expression
	 * @return String[]. Arreglo con todas las coincidencias encontradas.
	 */
	public static String[] evaluateRegex(String regex, String expression) {

		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(expression);
		ArrayList<String> results = new ArrayList<>();

		while (matcher.find()) {
			results.add(matcher.group());
		}

		return results.toArray(new String[results.size()]);
	}

}
