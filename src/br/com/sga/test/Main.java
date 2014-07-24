package br.com.sga.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Main {
	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		sdf.setLenient(false);
		Calendar calNovoVencimento = Calendar.getInstance();
		
		int diaDoVencimento = 22;
		int mesDoVencimento = 6-1;
		int anoDoVencimento = 2012;
		int numeroDeMeses = 0;
		
		calNovoVencimento.set(Calendar.DAY_OF_MONTH, diaDoVencimento);
		calNovoVencimento.set(Calendar.MONTH, mesDoVencimento + numeroDeMeses);
		calNovoVencimento.set(Calendar.YEAR, anoDoVencimento);
		
		System.out.println(sdf.format(calNovoVencimento.getTime()));
		
		System.out.println((mesDoVencimento) % 1);
		System.out.println((mesDoVencimento) % 3);
		System.out.println((10) % 6);
		System.out.println((10) % 12);
	
		System.out.println(sdf.format(calNovoVencimento.getTime()));
	}
}
