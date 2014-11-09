package ar.edu.utn.frba.bioinformatica;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.biojava3.core.sequence.io.FastaReaderHelper;
import org.biojava3.core.sequence.io.util.IOUtils;
import org.biojava3.ws.alignment.qblast.BlastOutputFormatEnum;
import org.biojava3.ws.alignment.qblast.BlastProgramEnum;
import org.biojava3.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava3.ws.alignment.qblast.NCBIQBlastOutputProperties;
import org.biojava3.ws.alignment.qblast.NCBIQBlastService;
import org.biojava3.core.sequence.ProteinSequence;

public class Ex2 {

	public static void main(String[] args) {
		
		System.out.println("----- Ejercicio 2 -----");
		System.out.println("Este script trabaja con una secuencia de aminoacidos"
				+ " en formato FASTA, ubicada en el archivo \"Ex1_output.fasta\".");
		
		
		// Instanciamos el servicio de BioJava para hacer queries al NCBI
		NCBIQBlastService service = new NCBIQBlastService();

		// El archivo input de este script es el output del Ex1
		String inputFilesDirectoryPath = "output/";
		// Se llama "Ex1_output.fasta"
		String fastaInputPath = inputFilesDirectoryPath + "Ex1_output.fasta";
		// Levantamos el archivo con Java
		File fastaInputFile = new File(fastaInputPath);
		
		// El archivo output de este script va en el directorio correspondiente
		String outputFilesDirectoryPath = "output/";
		// Se llama "blast.out"
		String blastOutputPath = outputFilesDirectoryPath + "blast.out";
		// Levantamos el archivo con Java
		File blastOutputFile = new File(blastOutputPath);
		
		// Este mapa contendrá las secuencias de aminoacidos obtenidas en el Ex1
		Map<String, ProteinSequence> secuenciasDeAminoacidos = new LinkedHashMap<String, ProteinSequence>();
		
		System.out.print("Leyendo secuencias input en formato FASTA...");
		
		try {
			// Obtenemos las seis secuencias desde el archivo FASTA
			secuenciasDeAminoacidos = FastaReaderHelper.readFastaProteinSequence(fastaInputFile);
		} catch (Exception e) {
			System.out.println("Se produjo un error al intentar leer"
					+ " el archivo fasta input. Ejecucion abortada.");
			e.printStackTrace();
			System.exit(0);
		}
		
		System.out.println(" listo!");
		
		// De las seis secuencias, sólo vamos a trabajar con la primera.
		// Sabemos que las otras fueron traducidas con marcos de lectura incorrectos
		// La versión local de este ejercicio trabaja con las seis.
		// Usando esa versión nos dimos cuenta que la correcta es la 1ra.
		
		System.out.print("Descartando las erroneas...");
		ProteinSequence laLeidaConElORFCorrecto = secuenciasDeAminoacidos.get(secuenciasDeAminoacidos.keySet().toArray()[0]);
		System.out.println(" listo!");
		
		System.out.print("Configurando el servicio de Blast del NCBI...");
		
		// Configuramos opciones del servicio de alineamiento
		NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
		// El programa Blast a utilizar es blastp (para proteínas)
		props.setBlastProgram(BlastProgramEnum.blastp);
		// La base de datos a consultar es swissprot
		props.setBlastDatabase("swissprot");
				
		
		// Configuramos opciones de salida
		NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();
		// El output lo queremos como texto plano
		outputProps.setOutputFormat(BlastOutputFormatEnum.Text);
		
		System.out.println(" listo!");

		// A los resultados se accede con un Request ID
		String rid = null;
		
		FileWriter writer = null;
		BufferedReader reader = null;
		try {
			// Enviamos la query al NCBI, guardando nuestro request ID
			
			System.out.print("Enviando la consulta...");
			rid = service.sendAlignmentRequest(laLeidaConElORFCorrecto.getSequenceAsString(), props);
			System.out.println(" listo!");
			// Ya que no hay nada más que hacer, esperamos que los resultados estén listos
			System.out.println("Esperando resultados...");
			while (!service.isReady(rid)) {
				System.out.println("Esperando los resultados por otros 5 segundos...");
				Thread.sleep(5000);
			}
			
			System.out.println("Resultados listos!");

			// Leemos los resultados
			System.out.print("Obteniendo resultados...");
			InputStream in = service.getAlignmentResults(rid, outputProps);
			reader = new BufferedReader(new InputStreamReader(in));
			System.out.println(" listo!");
			
			// Escribimos en el archivo output
			System.out.print("Guardando los resultados en " + blastOutputPath + "...");
			writer = new FileWriter(blastOutputFile);

			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line + System.getProperty("line.separator"));
			}
			System.out.println(" listo!");
			
			System.out.println("----- Fin del script -----");
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			// Cerramos streams
			IOUtils.close(writer);
			IOUtils.close(reader);

			// Borramos los resultados del servidor
			// No necesitamos que se almacenen remotamente
			service.sendDeleteRequest(rid);
		}
	}
}
