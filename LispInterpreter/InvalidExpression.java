package LispInterpreter;

/**
 * Excepcion para indicar una expresion invalida.
 * 
 * @author Diego Morales, Erick Guerra, Pablo Zamora
 * @version 25/03/2022
 *
 */
public class InvalidExpression extends Exception {

	/**
	 * Metodo constructor.
	 * 
	 * @param message
	 */
	public InvalidExpression(String message) {
		super(message);
	}

	/**
	 * Metodo constructor Excepcion para indicar una expresion invalida.
	 */
	public InvalidExpression() {
		super("Expresion invalida.");
	}
}
