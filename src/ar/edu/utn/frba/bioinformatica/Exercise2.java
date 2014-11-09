package ar.edu.utn.frba.bioinformatica;

import static org.biojava3.ws.alignment.qblast.BlastAlignmentParameterEnum.ENTREZ_QUERY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.biojava3.core.sequence.io.util.IOUtils;
import org.biojava3.ws.alignment.qblast.BlastProgramEnum;
import org.biojava3.ws.alignment.qblast.NCBIQBlastAlignmentProperties;
import org.biojava3.ws.alignment.qblast.NCBIQBlastOutputProperties;
import org.biojava3.ws.alignment.qblast.NCBIQBlastService;

public class Exercise2 {
	private static final String SEQUENCE = "MKWVTFISLLFLFSSAYSRGVFRRDAHKSEVAHRFKDLGEENFKALVLIAFAQYLQQCP";
	private final static String PATH_FOLDER_OUTPUT = "data/output/";
	private final static String BLAST_OUTPUT_FILE_NAME = "Ex2_output";
	private final static String BLAST_OUTPUT_FILE_EXTENSION = "xml";

	public static void main(String[] args) {
		NCBIQBlastService service = new NCBIQBlastService();

		// set alignment options
		NCBIQBlastAlignmentProperties props = new NCBIQBlastAlignmentProperties();
		props.setBlastProgram(BlastProgramEnum.blastp);
		props.setBlastDatabase("swissprot");
		props.setAlignmentOption(ENTREZ_QUERY,
				"\"serum albumin\"[Protein name] AND mammals[Organism]");

		// set output options
		NCBIQBlastOutputProperties outputProps = new NCBIQBlastOutputProperties();
		// in this example we use default values set by constructor (XML format,
		// pairwise alignment, 100 descriptions and alignments)

		// Example of two possible ways of setting output options
		// outputProps.setAlignmentNumber(200);
		// outputProps.setOutputOption(BlastOutputParameterEnum.ALIGNMENTS,
		// "200");

		String rid = null; // blast request ID
		FileWriter writer = null;
		BufferedReader reader = null;
		try {
			// send blast request and save request id
			rid = service.sendAlignmentRequest(SEQUENCE, props);

			// wait until results become available. Alternatively, one can do
			// other computations/send other alignment requests
			while (!service.isReady(rid)) {
				System.out
						.println("Waiting for results. Sleeping for 5 seconds");
				Thread.sleep(5000);
			}

			// read results when they are ready
			InputStream in = service.getAlignmentResults(rid, outputProps);
			reader = new BufferedReader(new InputStreamReader(in));

			// write blast output to specified file
			File f = new File(PATH_FOLDER_OUTPUT + BLAST_OUTPUT_FILE_NAME + "_"
					+ new DateHelper().getFechaYHoraActual() + "."
					+ BLAST_OUTPUT_FILE_EXTENSION);
			System.out.println("Saving query results in file "
					+ f.getAbsolutePath());
			writer = new FileWriter(f);

			String line;
			while ((line = reader.readLine()) != null) {
				writer.write(line + System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			// clean up
			IOUtils.close(writer);
			IOUtils.close(reader);

			// delete given alignment results from blast server (optional
			// operation)
			service.sendDeleteRequest(rid);
		}
	}
}