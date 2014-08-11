package br.com.sometal.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.junit.Test;

import br.com.sometal.model.FolhaDePonto;

public class FolhaDePontoTest {

	@Test
	public void horasTrabalhadas() throws ParseException {
		Calendar cEntrada = Calendar.getInstance();
        Calendar cSaida = Calendar.getInstance();
        cEntrada.set(Calendar.HOUR,8);
        cEntrada.set(Calendar.MINUTE,10);

        cSaida.set(Calendar.HOUR,9);
        cSaida.set(Calendar.MINUTE,22);
        
		assertEquals(new BigDecimal("1.2"), FolhaDePonto.horasTrabalhadas(cEntrada, cSaida));
		
		BigDecimal horasPorPeriodo = BigDecimal.ZERO;
		BigDecimal horasPorDia = BigDecimal.ZERO;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		// p1
		cEntrada.setTimeInMillis(sdf.parse("7:00").getTime());
		cSaida.setTimeInMillis(sdf.parse("9:00").getTime());
		horasPorPeriodo = FolhaDePonto.horasTrabalhadas(cEntrada, cSaida);
        assertEquals(new BigDecimal("2"), horasPorPeriodo);
        horasPorDia = horasPorDia.add(horasPorPeriodo);
        System.out.println("P1 = " + horasPorPeriodo + " \t\t Acumudado(" + horasPorDia + ")");
        
    	// p2
		cEntrada.setTimeInMillis(sdf.parse("9:15").getTime());
		cSaida.setTimeInMillis(sdf.parse("12:00").getTime());
		horasPorPeriodo = FolhaDePonto.horasTrabalhadas(cEntrada, cSaida);
        assertEquals(new BigDecimal("2.75"), horasPorPeriodo);
        horasPorDia = horasPorDia.add(horasPorPeriodo);
        System.out.println("P2 = " + horasPorPeriodo + " \t Acumudado(" + horasPorDia + ")");
        
    	// p3
		cEntrada.setTimeInMillis(sdf.parse("1:00").getTime());
		cSaida.setTimeInMillis(sdf.parse("3:00").getTime());
		horasPorPeriodo = FolhaDePonto.horasTrabalhadas(cEntrada, cSaida);
        assertEquals(new BigDecimal("2"), horasPorPeriodo);
        horasPorDia = horasPorDia.add(horasPorPeriodo);
        System.out.println("P3 = " + horasPorPeriodo + " \t\t Acumudado(" + horasPorDia + ")");
        
    	// p4
		cEntrada.setTimeInMillis(sdf.parse("3:15").getTime());
		cSaida.setTimeInMillis(sdf.parse("5:00").getTime());
		horasPorPeriodo = FolhaDePonto.horasTrabalhadas(cEntrada, cSaida);
        assertEquals(new BigDecimal("1.75"), horasPorPeriodo);
        horasPorDia = horasPorDia.add(horasPorPeriodo);
        System.out.println("P4 = " + horasPorPeriodo + " \t Acumudado(" + horasPorDia + ")");
        
        System.out.println("TOTAL=" + horasPorDia);
	}

}
