package br.com.grandev.acesso;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.com.grandev.acesso.dao.InnerDao;

public class ControleDeRegistrosTest {

	@Test
	public void deveIdentificarRegistroNovo() {
		Date data = new Date();
		Inner i1 = new Inner(1,Tipo.ENTRADA.getCodigo(), data, "1001", Status.ENVIADO.getCodigo());
		Inner i2 = new Inner(1,Tipo.ENTRADA.getCodigo(), data, "1001", Status.ENVIADO.getCodigo());
		Inner i3 = new Inner(1,Tipo.ENTRADA.getCodigo(), data, "1001", Status.ENVIADO.getCodigo());
		Inner i4 = new Inner(1,Tipo.ENTRADA.getCodigo(), data, "1001", Status.NAOENVIADO.getCodigo());
		Inner i5 = new Inner(1,Tipo.ENTRADA.getCodigo(), data, "1001", Status.NAOENVIADO.getCodigo());
		
		List<Inner> inners = new ArrayList<Inner>();
		inners.add(i1);
		inners.add(i2);
		inners.add(i3);
		inners.add(i4);
		inners.add(i5);
		
		InnerDao daoFalso = mock(InnerDao.class);
		when(daoFalso.getAll()).thenReturn(inners);
		
		ControleDeRegistros cdr = new ControleDeRegistros(daoFalso);
		
		assertEquals(5, cdr.getInners().size());
		assertEquals(2, cdr.getInnersPendentes().size());
	}
	
	@Test
	public void deveIdentificarRegistroComFalhaNoEnvio() {
		Date data = new Date();
		Inner i1 = new Inner(1,Tipo.ENTRADA.getCodigo(), data, "1001", Status.ENVIADO.getCodigo());
		Inner i2 = new Inner(1,Tipo.ENTRADA.getCodigo(), data, "1001", Status.ENVIADO.getCodigo());
		Inner i3 = new Inner(1,Tipo.ENTRADA.getCodigo(), data, "1001", Status.FALHA.getCodigo());
		Inner i4 = new Inner(1,Tipo.ENTRADA.getCodigo(), data, "1001", Status.ENVIADO.getCodigo());
		Inner i5 = new Inner(1,Tipo.ENTRADA.getCodigo(), data, "1001", Status.ENVIADO.getCodigo());
		
		List<Inner> inners = new ArrayList<Inner>();
		inners.add(i1);
		inners.add(i2);
		inners.add(i3);
		inners.add(i4);
		inners.add(i5);
		
		InnerDao daoFalso = mock(InnerDao.class);
		when(daoFalso.getAll()).thenReturn(inners);
		
		ControleDeRegistros cdr = new ControleDeRegistros(daoFalso);
		
		assertEquals(5, cdr.getInners().size());
		assertEquals(1, cdr.getInnersPendentes().size());
	}
}
