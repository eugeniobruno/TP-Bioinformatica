package ar.edu.utn.frba.bioinformatica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class Exercise3 {

	public static void main(String[] args) {
		System.out.println("----- Ejercicio 3 -----");
		System.out.println("Este script trabaja con un resultado de un alineamiento"
			+ " en formato Blast, ubicado en el archivo \"Ex2_output.txt\".");

		// El archivo input de este script es el output del Ex2
		String inputFilesDirectoryPath = "data/output/";
		File inputBlast = new File(inputFilesDirectoryPath + "Ex2_output.txt");
		
		if (!inputBlast.exists()) {
			System.out.println("No se encuentra el archivo Ex2_output.txt");
			System.out.println("Ejecucion abortada");
			System.exit(0);
		}

		try {
			// Leemos el archivo Blast a un String en memoria
			System.out.print("Leyendo los resultados de Blast input...");

			String blastOutput;
			StringBuilder fileContents = new StringBuilder(
					(int) inputBlast.length());
			Scanner scanner = new Scanner((Readable) new BufferedReader(
					new FileReader(inputBlast)));
			String lineSeparator = System.getProperty("line.separator");

			try {
				while (scanner.hasNextLine()) {
					fileContents.append(scanner.nextLine() + lineSeparator);
				}
				blastOutput = fileContents.toString();
			} finally {
				scanner.close();
			}

			System.out.println(" listo!");

			// Separamos cada hit en un String diferente
			String[] hits = blastOutput.split(">");

			String inputSequenceSeparator = StringUtils
					.repeat(lineSeparator, 4);

			// Eliminamos lineas en blanco redundantes
			for (int i = 0; i < hits.length; i++) {
				if (hits[i].indexOf(inputSequenceSeparator) >= 0) {
					hits[i] = hits[i].substring(0,
							hits[i].indexOf(inputSequenceSeparator));
				}
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String pattern = "";
			boolean huboResultados;

			while (true) {
				System.out.print("Ingrese un pattern para buscar: ");

				huboResultados = false;
				try {
					pattern = br.readLine();
					for (int i = 1; i < hits.length; i++) {
						if (StringUtils.containsIgnoreCase(hits[i], pattern)) {
							System.out.println(hits[i]);
							huboResultados = true;
						}
					}
					if (!huboResultados) {
						System.out.println("No hay resultados.");
					}
				} catch (IOException e) {
					System.out
							.println("Se produjo un error al leer del teclado. Ejecucion abortada.");
					e.printStackTrace();
					System.exit(0);
				}
			}
		} catch (IOException e) {
			System.out
					.println("Se produjo un error al leer el input. Ejecucion abortada.");
			e.printStackTrace();
			System.exit(0);
		}
	}
}