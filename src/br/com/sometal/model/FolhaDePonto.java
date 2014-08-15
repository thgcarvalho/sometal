package br.com.sometal.model;

import java.math.BigDecimal;
import java.util.Calendar;

/**
 * @author Thiago Carvalho
 * 
 */
public class FolhaDePonto {
	
	public static void main(String[] args) {
		FolhaDePonto fp = new FolhaDePonto();
		fp.horasTrabalhadas();
	}
	
	public static BigDecimal horasTrabalhadas(Calendar entrada, Calendar saida) {
        long diffMinutos = (saida.getTimeInMillis() - entrada.getTimeInMillis()) / (60 * 1000);
        BigDecimal bdMinutos = new BigDecimal(diffMinutos);
        BigDecimal bdHora = new BigDecimal(60);
		return bdMinutos.divide(bdHora);
	}

	public void horasTrabalhadas() {
		Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(Calendar.HOUR,8);
        c1.set(Calendar.MINUTE,50);

        c2.set(Calendar.HOUR,9);
        c2.set(Calendar.MINUTE,51);

        //diferenca em minutos
        long diffMinutos = (c2.getTimeInMillis() - c1.getTimeInMillis()) / (60 * 1000);
        BigDecimal bd = new BigDecimal(diffMinutos);
		System.out.println("Diferença em minutos: " + diffMinutos + " minutos");
		System.out.println("Horas: " + bd.divide(new BigDecimal(60)) );
        
        //diferenca em horas e minutos
        long diff = c2.getTimeInMillis() - c1.getTimeInMillis();
        long hours = (60 * 60 * 1000);
        long diffHoras = diff / hours;
        long diffHorasMinutos = (diff % hours) / (60 * 1000);
        System.out.println("Diferença em horas/minutos: "+diffHoras+" horas e "+diffHorasMinutos+" minutos");
	}

}
