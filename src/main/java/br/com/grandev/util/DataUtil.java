package br.com.grandev.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DataUtil {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
	private Date dataAtual;
	
	public DataUtil() {
		this.dataAtual = new Date();
	}
	
	public DataUtil(Date dataAtual) {
		this.dataAtual = dataAtual;
	}
	
	public Date getDataValida(int dia, int mes, int ano) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1); // seta 1, mas pode ser alterado
		calendar.set(Calendar.MONTH, mes);
		calendar.set(Calendar.YEAR, ano);
		
		int ultimoDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		
		if (dia > ultimoDia) {
			calendar.set(Calendar.DAY_OF_MONTH, ultimoDia);
		} else {
			calendar.set(Calendar.DAY_OF_MONTH, dia);
		}
		return calendar.getTime();
	}
	
	public Integer getMes(String descMes) {
		if (descMes == null) {
			return null;
		} else if (descMes.equals("JANEIRO")) {
			return 0;
		} else if (descMes.equals("FEVEREIRO")) {
			return 1;
		} else if (descMes.equals("MARÇO")) {
			return 2;
		} else if (descMes.equals("ABRIL")) {
			return 3;
		} else if (descMes.equals("MAIO")) {
			return 4;
		} else if (descMes.equals("JUNHO")) {
			return 5;
		} else if (descMes.equals("JULHO")) {
			return 6;
		} else if (descMes.equals("AGOSTO")) {
			return 7;
		} else if (descMes.equals("SETEMBRO")) {
			return 8;
		} else if (descMes.equals("OUTUBRO")) {
			return 9;
		} else if (descMes.equals("NOVEMBRO")) {
			return 10;
		} else if (descMes.equals("DEZEMBRO")) {
			return 11;
		}
		return null;
	}
	
	public String getDescMes(int mes) {
		if (mes == 0) {
			return "JANEIRO";
		} else if (mes == 1) {
			return "FEVEREIRO";
		} else if (mes == 2) {
			return "MARÇO";
		} else if (mes == 3) {
			return "ABRIL";
		} else if (mes == 4) {
			return "MAIO";
		} else if (mes == 5) {
			return "JUNHO";
		} else if (mes == 6) {
			return "JULHO";
		} else if (mes == 7) {
			return "AGOSTO";
		} else if (mes == 8) {
			return "SETEMBRO";
		} else if (mes == 9) {
			return "OUTUBRO";
		} else if (mes == 10) {
			return "NOVEMBRO";
		} else if (mes == 11) {
			return "DEZEMBRO";
		}
		return null;
	}
	
	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		this.dataAtual = dataAtual;
	}
	
	public static String formatarData(Date data) {
		String dataFormatada;
		try {
			dataFormatada = sdf.format(data);
		} catch (Exception exp) {
			dataFormatada = "";
		}
		return dataFormatada;
	}
}
