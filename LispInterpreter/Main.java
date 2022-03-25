package LispInterpreter;

import java.io.IOException;
import java.util.Scanner;

public class Main{
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String menu;
		
		System.out.println("Bienvenido al interprete de LISP!");
		boolean end = false;
		while(!end) {
			menu = """
					\n1. Indicar el nombre del archivo a ejecutar.
					2. Salir""";
			int option = pregunta(menu, 2, sc);
			
			switch(option) { 
			case 1: //Encontrar el archivo "program.txt"
				String[] fileContent = null;
				boolean repeat = true;
				while(repeat) { //Bucle para encontrar el archivo
					try { //Se encuentra el archivo
						System.out.println("Ingrese el nombre del archivo donde se encuentra su programa (sin extensión)");
						String filename = sc.nextLine();
						fileContent = FileController.readFile(filename);
						repeat = false;
					} catch (IOException e) { //Si no se encuentra el archivo
						System.out.println("\nArchivo no encontrado.\nPor favor, asegurese de que el archivo tenga el nombre correcto y se encuentre en la carpeta que contiene al programa.");
						System.out.println("Presione enter para volver a buscar el archivo.");
						sc.nextLine();
					}
				}
				System.out.println("\nArchivo encontrado");
				try {
					String [] program = Interpreter.validFormat(fileContent);
					for(String row : program) {
						Data data = Interpreter.operate(row);
					}
				}catch(Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
				break;
			case 2: //Finaliza el programa
				System.out.println("Gracias por utilizar el programa!"); 
				end = true;
				break;
			default: //Opcion no valida
				System.out.println("Opcion no valida");
				break;
			}
		}
		/*String expression = "(write (+ 5 * 4 (+ 1 2 (/ 25 5) (+ 3 4)) ))";
		String [] lines = new String[1];
		try {
			if(expression.contains("\n"))
				lines = expression.split("\n");
			else lines[0] = expression;
			for(String l : lines) {
				Data data = Interpreter.operate(l);
				if(data.getDescription() != null && data.getDescription().contains("print"))
					System.out.println(data.getValue());
			}
		} catch (InvalidExpression | ReferenceException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		//System.out.println(result);*/
		
		sc.close();
	}
	
	public static int pregunta(String pregunta, int opciones, Scanner scan)
	  {
	      boolean bucle = true;
	      int respuesta = 0;
	      try 
	      {
	          while(bucle)
	          {
	              System.out.println(pregunta);
	              respuesta = scan.nextInt();
	              scan.nextLine();
	              if(respuesta > 0 && respuesta <= opciones) bucle = false;
	              else System.out.println("\nRepuesta no valida.\n");
	          }    
	      } catch (Exception e) {
	          System.out.println("\nRepuesta no valida. Ingrese solamente numeros.\n");
	          respuesta = pregunta(pregunta, opciones, scan);
	      }
	      return respuesta;
	  }
	
}