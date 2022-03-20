package LispInterpreter;

public class ReferenceException extends Exception{
	public ReferenceException(String message) {
		super(message);
	}
	
	public ReferenceException() {
		super("Referencia a variable inexistente.");
	}
}
