package LispInterpreter;

public class Operations {

	public static Object addOperation(String expression) {

		try {

			String expressionContent = getListContent(expression);
			String validOperators = "+-*/";

		} catch (Exception ex) {
			System.out.println(ex);
			throw new IllegalArgumentException("La operacion ingresada no es valida.");

		}
		
		return null;

	}

	public static String getListContent(String expression) throws IndexOutOfBoundsException {

		return SintaxScanner.evaluateRegex("(?<=^\\().+(?=\\)$)", expression)[0];
	}
	

}
