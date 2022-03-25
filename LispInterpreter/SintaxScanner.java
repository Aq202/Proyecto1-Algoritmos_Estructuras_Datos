package LispInterpreter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que permite manipular las expresiones regulares, relacionadas a la
 * sintaxis lisp.
 * 
 * @author Diego Morales, Erick Guerra, Pablo Zamora
 * @version 25/03/2022
 *
 */
public class SintaxScanner {

	/**
	 * Se encarga de evaluar una expresion y determinar el tipo de operacion a
	 * realizar.
	 * 
	 * @param expression. String
	 * @return int. 1: operacion aritmetica. 2: nueva variable. 3:Expresion atom. 4:
	 *         Definicion de funciones. 5:Expresion Listp. 6: Expresion List.
	 *         7:Expresion write 8: Expresion quote. 9:Expresion logica.
	 *         10:Expresion cond. 11:Expresion equal. 12:Evaluar una funcion.
	 *         13:Dato primitivo. 14:Evaluar variable. 0: Expresion invalida
	 */
	public static int getState(String expression) {

		expression = expression != null ? expression.trim() : null;

		// Operacion aritmetica
		if (match("^\\(\\s*[\\+\\-\\/\\*]\\s+([^()\"']+|(\\(.*\\))+)+\\)", expression))
			return 1;

		// (setq name (operators)|"string"|'String'|variable)
		if (match("^\\(\\s*setq\\s+(\\d*[a-z]\\w*)\\s*((\\(.+\\))|('[^']*')|(\"[^\"]*\")|[^\\s^\\(^\\)]+)\\s*\\)$",
				expression))
			return 2;

		// Atom
		if (match("^\\s*\\(atom\\s+'*((\\(.*\\))|(\\\"[^\\\"]*\\\")|('[^\\']*')|(\\w+))\\)$", expression))
			return 3;

		// Definicion de funciones
		if (match("^\\(\\s*defun\\s+[^()\\\"']+\\s*\\([^()\\\"']*\\).*\\)$", expression))
			return 4;

		// Listp
		if (match("^\\s*\\(listp\\s+'*((\\(.*\\))|(\\\"[^\\\"]*\\\")|('[^\\']*')|(\\w+))\\)$", expression))
			return 5;

		// List
		if (match("^\\(\\s*list\\s+'*((\\w+)|(\\(.*\\))|(\\\"[^\\\"]*\\\")|(\\'[^\\']*\\')+)\\)$", expression))
			return 6;

		// Write
		if (match("^\\(\\s*write\\s+'*((\\w+)|(\\(.*\\))|(\\\"[^\\\"]*\\\")|(\\'[^\\']*\\')+)\\)$", expression))
			return 7;

		// Quote
		if (match("^\\s*\\(quote\\s+'*((\\(.*\\))|(\\\"[^\\\"]*\\\")|('[^\\']*')|(\\w+))\\)$", expression))
			return 8;

		// Single quote
		if (match("^\\s*'+((\\(.*\\))|(\\\"[^\\\"]*\\\")|('[^\\']*')|(\\w+))$", expression))
			return 8;

		// expresion logica
		if (match("^\\(\\s*[\\<\\>\\=](\\s+[^()\"' ]+\\s*){2,}\\)$", expression))
			return 9;

		// cond
		if (match("^\\(\\s*cond\\s+(\\(\\s*((\\(.*\\))|T|(NIL))\\s*\\S+.*)+\\)$", expression))
			return 10;

		// equal
		if (match("^\\(\\s*equal\\s+.+\\s+.+\\)", expression))
			return 11;

		// evaluar funcion
		if (match(
				"^\\(\\s*((?!write)|(?!quote)|(?!setq)|(?!list)|(?!listp)|(?!atom)|(?!cond)|(?!defun)|(?!setq)).[^()\\\"']+\\s+.*\\)$",
				expression))
			if (!Arrays.asList(Interpreter.RESERVER_WORDS).contains(evaluateRegex("(?:\\w+)", expression)[0]))
				return 12;

		// Valores primitivos numeros, strings, booleans (T, NIL)
		if (match(
				"^(([-+]{0,1}([\\d^.]+)|((\\d+\\.\\d+)))|(\\\"\\\"[^\\\"]*\\\"\\\")|(\\\"[^\\\"]*\\\")|('[^']*')|T|(NIL))+$",
				expression))
			return 13;

		// evaluar variable
		if (match("^[^()\"' ]+$", expression))
			return 14;

		return 0;
	}

	/**
	 * Se encarga de verificar si una expresion posee coincidencias
	 * 
	 * @param regex.     String. Expresion regular.
	 * @param expression String.
	 * @return boolean.
	 */
	public static boolean hasMatches(String regex, String expression) {
		return evaluateRegex(regex, expression).length > 0;
	}

	/**
	 * Se encarga de verificar si una expresion coincide en su totalidad con la
	 * regex.
	 * 
	 * @param regex      String. Expresion regular.
	 * @param expression String.
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
	 * @param regex      String. Expresion regular.
	 * @param expression String.
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
