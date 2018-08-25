package br.com.fichasordens.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataUtil {
	
	public static final String DATA_INICIO = "DATA_INICIO";
	public static final String DATA_FIM = "DATA_FIM";
	
	private DataUtil() {}
	
	
	public static long calcularDiferencaDiasEntreUmaDataEAgora(final Date data) {
		LocalDate now = LocalDate.now();
		Date d = new java.util.Date(data.getTime()); 
	    LocalDate sixDaysBehind = d.toInstant()
	    	      .atZone(ZoneId.systemDefault())
	    	      .toLocalDate();
	 
	    Period period = Period.between(now, sixDaysBehind);
	    long diff = Math.abs(period.getDays());
	    return diff;
	}
	
	public static Map<String,Date> getDataInicioDataFim() {
		Map<String, Date> map = new HashMap<String,Date>();
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);
		map.put(DATA_INICIO, c.getTime());
		c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
		map.put(DATA_FIM, c.getTime());
		return map;
	}

}
