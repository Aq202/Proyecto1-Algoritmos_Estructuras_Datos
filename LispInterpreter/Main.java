package LispInterpreter;

import java.io.IOException;
import java.util.Scanner;

/**
 * Clase Main.
 * 
 * @author Diego Morales, Erick Guerra, Pablo Zamora
 * @version 25/03/2022
 *
 */
public class Main {

	/**
	 * Se encarga de solicitar al usuario que ingrese un dato.
	 * 
	 * @param sc      Scanner
	 * @param message
	 * @return String.
	 */
	private static String getInstruction(Scanner sc) {
		System.out.print("\n>> ");
		return sc.nextLine();
	}

	/**
	 * Metodo principal del programa.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("LISP INTERPRETER V1.0.0");
		System.out.println("Ingresa --help para ver las instrucciones.");

		String currentInstruccion = "";

		while (true) {
			currentInstruccion = getInstruction(sc).trim();

			if (currentInstruccion.length() == 0)
				continue;
			if (currentInstruccion.equalsIgnoreCase("exit"))
				break;

			try {

				// ejecutar archivo
				if (SintaxScanner.match("lisp\\s+[^~“#%&*:<>?\\/\\\\{|} ]+", currentInstruccion)) {

					String fileName = SintaxScanner.evaluateRegex("[^~“#%&*:<>?\\/\\\\{|} ]+$", currentInstruccion)[0];
					String[] fileContent;
					try {
						fileContent = FileController.readFile(fileName);
					} catch (IOException e) {
						throw new Exception(
								String.format("El archivo %s.lisp no existe en %s\\", fileName, FileController.PATH));
					}

					String[] program = Interpreter.validFormat(fileContent);
					for (String row : program) {
						Interpreter.operate(row);
					}

				} else if (currentInstruccion.equalsIgnoreCase("--help")) {
					// help
					System.out.println(
							"""

									Instrucciones del interprete:

										lisp FILE_NAME     -Ejecuta las expresiones contenidas en el archivo currentPath/fileName.lisp
										exit               -Finaliza la ejecucion del interprete
									""");
				} else {
					// interpretar expresion
					Data result = Interpreter.operate(currentInstruccion);
					if (!result.getBlockPrint())
						System.out.println(result.toString());
				}

			} catch (Exception e) {

				if (e instanceof InvalidExpression)
					System.out.println("\nSYNTAX ERROR: " + e.getMessage());
				else
					System.out.println("\nERROR: " + e.getMessage());

			}

		}
		System.out.println("\nINTERPRETE DE LISP FINALIZADO");
		sc.close();
	}

}