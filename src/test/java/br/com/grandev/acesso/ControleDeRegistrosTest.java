package br.com.grandev.acesso;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class ControleDeRegistrosTest {

	@Test
	public void deveIdentificarRegistroNovo() {
		Date data = new Date();
		Registro c1 = new Registro(1001L, data, data, "01", "C01");
		Registro c2 = new Registro(1002L, data, data, "02", "C01");
		Registro c3 = new Registro(1003L, data, data, "01", "C01");
		
		Registro g1 = new Registro(1001L, data, data, "01", "C01");
		Registro g2 = new Registro(1002L, data, data, "02", "C01");
		
		List<Registro> registrosDasCatracas = new ArrayList<Registro>();
		registrosDasCatracas.add(c1);
		registrosDasCatracas.add(c2);
		registrosDasCatracas.add(c3);
		
		List<Registro> registrosGerenciados = new ArrayList<Registro>();
		registrosGerenciados.add(g1);
		registrosGerenciados.add(g2);
		
		ControleDeRegistros cdr = new ControleDeRegistros();
		cdr.setRegistrosDasCatracas(registrosDasCatracas);
		cdr.setRegistrosGerenciados(registrosGerenciados);
		
		cdr.gerenciarRegistros();
		
		assertEquals(3, cdr.getRegistrosDasCatracas().size());
		assertEquals(2, cdr.getRegistrosGerenciados().size());
		assertEquals(1, cdr.getRegistrosNaoEnviados().size());
	}
}
