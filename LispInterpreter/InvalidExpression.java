package LispInterpreter;

public class InvalidExpression extends Exception{

	public InvalidExpression(String message) {
		super(message);
	}
	
	public InvalidExpression() {
		super("Expresion invalida.");
	}
}
