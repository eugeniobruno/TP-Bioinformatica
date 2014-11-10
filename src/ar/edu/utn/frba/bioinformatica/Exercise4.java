package ar.edu.utn.frba.bioinformatica;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Exercise4 {
	private static final String PATH_FOLDER_TEMP_OUTPUT_GETORF = "data/output/Ex4_temp_getorf/";
	private static final String PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS = "data/output/Ex4_temp_patmatmotifs/";
	private static final String PATH_OUTPUT_FILE = "data/output/Ex4_output.txt";

	public static void main(String[] args) {
		
		System.out.println("----- Ejercicio 4 -----");
		System.out.println("Este script trabaja con una secuencia de nucleotidos"
			+ " en formato FASTA, ubicada en el archivo \"Ex4_input.fasta\".");

		
		// Obtengo las secuencias de aminoácidos posibles a partir de la
		// secuencia de nucleótidos del ejercicio 1...
		createTempDirectory(PATH_FOLDER_TEMP_OUTPUT_GETORF);
		
		Path inputPath = Paths.get("data/input/Ex4_input.fasta");
		Path tempOutputGetorfPath = Paths.get(PATH_FOLDER_TEMP_OUTPUT_GETORF);
		
		System.out.print("Creando los archivos orf temporales...");
		String getorfCommand = "getorf -sequence "
				+ inputPath.toString()
				+ " -table 0 -minsize 30 -maxsize 1000000 -find 0 -methionine -nocircular -reverse -flanking 100 -ossingle2 -osdirectory2 "
				+ tempOutputGetorfPath.toString() + " -auto";

		executeCommand(getorfCommand);
		System.out.println(" listo!");

		System.out.print("Analizando dominios...");
		// Por cada secuencia de aminoácidos obtenida
		// hago un análisis de dominios...
		createTempDirectory(PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS);

		String patmatmotifsCommand = null;

		Path tempEachSequencePath;
		Path tempOutputMotifPath;
		
		for (String filename : getDirectoryFilenameList(PATH_FOLDER_TEMP_OUTPUT_GETORF)) {
			
			tempEachSequencePath = Paths.get(PATH_FOLDER_TEMP_OUTPUT_GETORF + filename);
			tempOutputMotifPath = Paths.get(PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS + filename);
			
			patmatmotifsCommand = "patmatmotifs -sequence "
					+ tempEachSequencePath.toString() + " -outfile "
					+ tempOutputMotifPath.toString()
					+ ".patmatmotifs -nofull -prune -rformat dbmotif -auto";
			executeCommand(patmatmotifsCommand);
		}
		System.out.println(" listo!");

		// Reúno los resultados en un único archivo...
		System.out.print("Juntando resultados en un archivo...");
		List<Path> inputs = new ArrayList<Path>();

		for (String filename : getDirectoryFilenameList(PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS))
			inputs.add(Paths.get(PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS + filename));

		Path output = Paths.get(PATH_OUTPUT_FILE);

		Charset charset = StandardCharsets.UTF_8;

		for (Path path : inputs) {
			List<String> lines;
			try {
				lines = Files.readAllLines(path, charset);
				Files.write(output, lines, charset, StandardOpenOption.CREATE,
						StandardOpenOption.APPEND);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(" listo!");
		
		System.out.print("Eliminando directorios temporales...");
		// Elimino los directorios temporales...
		try {
			FileUtils.deleteDirectory(new File(PATH_FOLDER_TEMP_OUTPUT_GETORF));
			FileUtils.deleteDirectory(new File(PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(" listo!");
		
		System.out.println("----- Fin del script -----");
	}

	private static void createTempDirectory(String path) {
		File file = null;

		try {
			file = new File(path);
			FileUtils.forceMkdir(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private static void executeCommand(String command) {
		
		StringBuffer output = new StringBuffer();

		Process p;

		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//System.out.println(output.toString());
	}

	private static List<String> getDirectoryFilenameList(String directoryPath) {
		File folder = new File(directoryPath);
		File[] listOfFiles = folder.listFiles();

		List<String> filenameList = new ArrayList<String>();

		for (int i = 0; i < listOfFiles.length; i++)
			if (listOfFiles[i].isFile())
				filenameList.add(listOfFiles[i].getName());

		return filenameList;
	}
}