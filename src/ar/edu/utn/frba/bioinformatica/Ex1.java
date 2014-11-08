package ar.edu.utn.frba.bioinformatica;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.ProteinSequence;
import org.biojava3.core.sequence.RNASequence;
import org.biojava3.core.sequence.io.FastaWriterHelper;
import org.biojava3.core.sequence.io.GenbankReaderHelper;
import org.biojava3.core.sequence.transcription.Frame;

public class Ex1 {
	public static void main(String[] args) {
		System.out.println("----- Ejercicio 1 -----");
		System.out
				.println("Este script trabaja con una secuencia de nucleotidos en formato GenBank, ubicada en el archivo \"Ex1_input.gb\".");

		String inputFilesDirectoryPath = "input/";
		String outputFilesDirectoryPath = "output/";

		File inputGenBank = new File(inputFilesDirectoryPath + "Ex1_input.gb");

		List<ProteinSequence> secuenciasDeAminoacidos = new LinkedList<ProteinSequence>();

		List<RNASequence> traduccionesDeCadaORF = new LinkedList<RNASequence>();

		System.out.print("Leyendo la secuencia de ADN...");

		Map<String, DNASequence> dnaSequences = new LinkedHashMap<String, DNASequence>();

		try {
			dnaSequences = GenbankReaderHelper
					.readGenbankDNASequence(inputGenBank);
		} catch (Exception e) {
			System.out
					.println("Se produjo un error al intentar leer el archivo GenBank. Ejecucion abortada.");
			e.printStackTrace();
			System.exit(0);
		}

		System.out.println(" listo!");

		for (DNASequence secuenciaDeADN : dnaSequences.values()) {
			// Realizamos la traduccion a ARN en los seis marcos de lectura
			// posibles
			System.out.print("Traduciendo a ARN en cada ORF...");
			traduccionesDeCadaORF.add(secuenciaDeADN.getRNASequence(Frame.ONE));
			traduccionesDeCadaORF.add(secuenciaDeADN.getRNASequence(Frame.TWO));
			traduccionesDeCadaORF.add(secuenciaDeADN
					.getRNASequence(Frame.THREE));
			traduccionesDeCadaORF.add(secuenciaDeADN
					.getRNASequence(Frame.REVERSED_ONE));
			traduccionesDeCadaORF.add(secuenciaDeADN
					.getRNASequence(Frame.REVERSED_TWO));
			traduccionesDeCadaORF.add(secuenciaDeADN
					.getRNASequence(Frame.REVERSED_THREE));
			System.out.println(" listo!");

			// Realizamos la transcripcion de ARN a aminoacidos para cada ORF
			System.out.print("Transcribiendo ARN en aminoacidos...");
			for (RNASequence cadaSecuenciaDeARN : traduccionesDeCadaORF) {
				secuenciasDeAminoacidos.add(cadaSecuenciaDeARN
						.getProteinSequence());
			}

			System.out.println(" listo!");

			// Escribimos el resultado en un archivo FASTA
			File outputFASTA = new File(outputFilesDirectoryPath
					+ "Ex1_output_" + fechaYHoraActual() + ".fasta");

			try {
				outputFASTA.createNewFile();
			} catch (IOException e) {
				System.out
						.println("Se produjo un error al intentar crear el archivo output. Ejecucion abortada.");
				e.printStackTrace();
				System.exit(0);
			}

			System.out.print("Escribiendo el resultado en archivo FASTA...");

			try {
				FastaWriterHelper.writeProteinSequence(outputFASTA,
						secuenciasDeAminoacidos);
			} catch (Exception e) {
				System.out
						.println("Se produjo un error al intentar escribir en el archivo output. Ejecucion abortada.");
				e.printStackTrace();
			}

			System.out.println(" listo!");

			System.out.println("----- Fin del script -----");
		}
	}

	public static String fechaYHoraActual() {
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	}
}