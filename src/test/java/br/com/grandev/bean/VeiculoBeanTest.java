package br.com.grandev.bean;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import br.com.grandev.model.Data;

public class VeiculoBeanTest {

	@Test
	public void deveObter20AnosAtrasAteProximoAno() {
		Calendar _2014 = Calendar.getInstance();
		_2014.set(2014, Calendar.JULY, 20);
		
		Data data = mock(Data.class);
		when(data.hoje()).thenReturn(_2014);
		
		VeiculoBean veiculoBean = new VeiculoBean(data);
		veiculoBean.carregaDatas();
		List<Integer> anos = veiculoBean.getAnos();
		
		assertEquals(21, anos.size());
		
		assertEquals(2015, anos.get(0).intValue());
		assertEquals(2014, anos.get(1).intValue());
		assertEquals(2013, anos.get(2).intValue());
		assertEquals(2012, anos.get(3).intValue());
		assertEquals(2011, anos.get(4).intValue());
		assertEquals(2010, anos.get(5).intValue());
		assertEquals(2009, anos.get(6).intValue());
		assertEquals(2008, anos.get(7).intValue());
		assertEquals(2007, anos.get(8).intValue());
		assertEquals(2006, anos.get(9).intValue());
		assertEquals(2005, anos.get(10).intValue());
		assertEquals(2004, anos.get(11).intValue());
		assertEquals(2003, anos.get(12).intValue());
		assertEquals(2002, anos.get(13).intValue());
		assertEquals(2001, anos.get(14).intValue());
		assertEquals(2000, anos.get(15).intValue());
		assertEquals(1999, anos.get(16).intValue());
		assertEquals(1998, anos.get(17).intValue());
		assertEquals(1997, anos.get(18).intValue());
		assertEquals(1996, anos.get(19).intValue());
		assertEquals(1995, anos.get(20).intValue());
	}
}
