package ar.edu.utn.frba.bioinformatica;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
	public String getFechaYHoraActual() {
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	}
}