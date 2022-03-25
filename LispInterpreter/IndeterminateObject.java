package LispInterpreter;

/**
 * Clase IndeterminateObject. Objeto que almacena valores no determinados.
 * @author Diego Morales, Erick Guerra, Pablo Zamora
 * @version 25/03/2022
 *
 */
public class IndeterminateObject {
	
	private Object value;

	public IndeterminateObject(Object value){
		this.value = value;
	}

	/**
	 * Metodo getter de la propiedad value
	 * @return Object.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Metodo setter de la propiedad value.
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	
}
