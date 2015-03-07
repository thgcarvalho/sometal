//package br.com.grandev.acesso;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.junit.Test;
//
//import br.com.grandev.acesso.dao.BilheteDao;
//import br.com.grandev.acesso.model.Bilhete;
//import br.com.grandev.acesso.model.Status;
//import br.com.grandev.acesso.model.Tipo;
//
//public class ControleDeRegistrosTest {
//
//	@Test
//	public void deveIdentificarRegistroNovo() {
//		Date data = new Date();
//		Bilhete i1 = new Bilhete("1", Tipo.ENTRADA.getCodigo(), data, data, 1001, Status.ENVIADO.getCodigo());
//		Bilhete i2 = new Bilhete("1", Tipo.ENTRADA.getCodigo(), data, data, 1001, Status.ENVIADO.getCodigo());
//		Bilhete i3 = new Bilhete("1", Tipo.ENTRADA.getCodigo(), data, data, 1001, Status.ENVIADO.getCodigo());
//		Bilhete i4 = new Bilhete("1", Tipo.ENTRADA.getCodigo(), data, data, 1001, Status.NAOENVIADO.getCodigo());
//		Bilhete i5 = new Bilhete("1", Tipo.ENTRADA.getCodigo(), data, data, 1001, Status.NAOENVIADO.getCodigo());
//		
//		List<Bilhete> inners = new ArrayList<Bilhete>();
//		inners.add(i1);
//		inners.add(i2);
//		inners.add(i3);
//		inners.add(i4);
//		inners.add(i5);
//		
//		BilheteDao daoFalso = mock(BilheteDao.class);
//		when(daoFalso.getAll()).thenReturn(inners);
//		
//		ControleDeRegistrosDB cdr = new ControleDeRegistrosDB(daoFalso);
//		
//		assertEquals(5, cdr.getBilhetes().size());
//		assertEquals(2, cdr.getBilhetesPendentes().size());
//	}
//	
//	@Test
//	public void deveIdentificarRegistroComFalhaNoEnvio() {
//		Date data = new Date();
//		Bilhete i1 = new Bilhete("1", Tipo.ENTRADA.getCodigo(), data, data, 1001, Status.ENVIADO.getCodigo());
//		Bilhete i2 = new Bilhete("1", Tipo.ENTRADA.getCodigo(), data, data, 1001, Status.ENVIADO.getCodigo());
//		Bilhete i3 = new Bilhete("1", Tipo.ENTRADA.getCodigo(), data, data, 1001, Status.FALHA.getCodigo());
//		Bilhete i4 = new Bilhete("1", Tipo.ENTRADA.getCodigo(), data, data, 1001, Status.ENVIADO.getCodigo());
//		Bilhete i5 = new Bilhete("1", Tipo.ENTRADA.getCodigo(), data, data, 1001, Status.ENVIADO.getCodigo());
//		
//		List<Bilhete> inners = new ArrayList<Bilhete>();
//		inners.add(i1);
//		inners.add(i2);
//		inners.add(i3);
//		inners.add(i4);
//		inners.add(i5);
//		
//		BilheteDao daoFalso = mock(BilheteDao.class);
//		when(daoFalso.getAll()).thenReturn(inners);
//		
//		ControleDeRegistrosDB cdr = new ControleDeRegistrosDB(daoFalso);
//		
//		assertEquals(5, cdr.getBilhetes().size());
//		assertEquals(1, cdr.getBilhetesPendentes().size());
//	}
//}
