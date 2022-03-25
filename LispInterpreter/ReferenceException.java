package LispInterpreter;

/**
 * Excepcion que indica la ejecucion de una variable indefinida.
 * 
 * @author Diego Morales, Erick Guerra, Pablo Zamora
 * @version 25/03/2022
 *
 */
public class ReferenceException extends Exception {

	/**
	 * Metodo constructor de la clase.
	 * 
	 * @param message String.
	 */
	public ReferenceException(String message) {
		super(message);
	}

	/**
	 * Metodo constructor de la clase.
	 */
	public ReferenceException() {
		super("Referencia a variable inexistente.");
	}
}
