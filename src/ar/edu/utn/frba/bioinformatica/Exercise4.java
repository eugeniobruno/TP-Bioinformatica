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
	private static final String PATH_FOLDER_EMBOSS = "C:\\mEMBOSS\\";
	private static final String PATH_INPUT_FILE = "data\\input\\Ex4_input.fasta";
	private static final String PATH_FOLDER_TEMP_OUTPUT_GETORF = "data\\output\\Ex4_temp_getorf\\";
	private static final String PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS = "data\\output\\Ex4_temp_patmatmotifs\\";
	private static final String PATH_OUTPUT_FILE = "data\\output\\Ex4_output.txt";

	public static void main(String[] args) {
		// Obtengo las secuencias de aminoácidos posibles a partir de la
		// secuencia
		// de nucleótidos del ejercicio 1...
		createTempDirectory(getAbsolutePath(PATH_FOLDER_TEMP_OUTPUT_GETORF));

		String getorfCommand = PATH_FOLDER_EMBOSS
				+ "getorf -sequence "
				+ getAbsolutePath(PATH_INPUT_FILE)
				+ " -table 0 -minsize 30 -maxsize 1000000 -find 0 -methionine -nocircular -reverse -flanking 100 -ossingle2 -osdirectory2 "
				+ getAbsolutePath(PATH_FOLDER_TEMP_OUTPUT_GETORF) + " -auto";

		executeCommand(getorfCommand);

		// Por cada secuencia de aminoácidos obtenida hago un análisis de
		// dominios...
		createTempDirectory(getAbsolutePath(PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS));

		String patmatmotifsCommand = null;

		for (String filename : getDirectoryFilenameList(getAbsolutePath(PATH_FOLDER_TEMP_OUTPUT_GETORF))) {
			patmatmotifsCommand = PATH_FOLDER_EMBOSS
					+ "patmatmotifs  -sequence "
					+ getAbsolutePath(PATH_FOLDER_TEMP_OUTPUT_GETORF)
					+ filename + " -outfile "
					+ getAbsolutePath(PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS)
					+ filename
					+ ".patmatmotifs -nofull -prune -rformat dbmotif -auto";
			executeCommand(patmatmotifsCommand);
		}

		// Reúno los resultados en un único archivo...
		List<Path> inputs = new ArrayList<Path>();

		for (String filename : getDirectoryFilenameList(getAbsolutePath(PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS)))
			inputs.add(Paths
					.get(getAbsolutePath(PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS)
							+ filename));

		Path output = Paths.get(getAbsolutePath(PATH_OUTPUT_FILE));

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

		// Elimino los directorios temporales...
		try {
			FileUtils.deleteDirectory(new File(
					getAbsolutePath(PATH_FOLDER_TEMP_OUTPUT_GETORF)));
			FileUtils.deleteDirectory(new File(
					getAbsolutePath(PATH_FOLDER_TEMP_OUTPUT_PATMATMOTIFS)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getAbsolutePath(String path) {
		String currentDir = new File(new File("").getAbsolutePath())
				.getAbsolutePath();

		return currentDir + "\\" + path;
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
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(output.toString());
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