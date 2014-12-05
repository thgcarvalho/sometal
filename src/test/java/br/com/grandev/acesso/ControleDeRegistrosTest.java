package br.com.grandev.acesso;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import br.com.grandev.acesso.ControleDeRegistros.Status;

public class ControleDeRegistrosTest {

	@Test
	public void deveIdentificarRegistroNovo() {
		Date data = new Date();
		Registro c1 = new Registro(1001, data, data, "01", "C01");
		Registro c2 = new Registro(1002, data, data, "02", "C01");
		Registro c3 = new Registro(1003, data, data, "01", "C01");
		
		Registro g1 = new Registro(1001, data, data, "01", "C01");
		Registro g2 = new Registro(1002, data, data, "02", "C01");
		
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
		
		cdr.analizar();
		
		assertEquals(3, cdr.getRegistrosDasCatracas().size());
		assertEquals(2, cdr.getRegistrosGerenciados().size());
		assertEquals(1, cdr.getRegistrosNaoEnviados().size());
	}
	
	@Test
	public void deveIdentificarRegistroComFalhaNoEnvio() {
		Date data = new Date();
		Registro c1 = new Registro(1011, data, data, "01", "C01");
		Registro c2 = new Registro(1012, data, data, "02", "C01");
		Registro c3 = new Registro(1013, data, data, "01", "C01");
		
		Registro g1 = new Registro(1011, data, data, "01", "C01");
		g1.setStatus(Status.ENVIADO);
		Registro g2 = new Registro(1012, data, data, "02", "C01"); 
		g2.setStatus(Status.FALHA);
		Registro g3 = new Registro(1013, data, data, "01", "C01");
		g3.setStatus(Status.ENVIADO);
		
		List<Registro> registrosDasCatracas = new ArrayList<Registro>();
		registrosDasCatracas.add(c1);
		registrosDasCatracas.add(c2);
		registrosDasCatracas.add(c3);
		
		List<Registro> registrosGerenciados = new ArrayList<Registro>();
		registrosGerenciados.add(g1);
		registrosGerenciados.add(g2);
		registrosGerenciados.add(g3);
		
		ControleDeRegistros cdr = new ControleDeRegistros();
		cdr.setRegistrosDasCatracas(registrosDasCatracas);
		cdr.setRegistrosGerenciados(registrosGerenciados);
		
		cdr.analizar();
		
		assertEquals(3, cdr.getRegistrosDasCatracas().size());
		assertEquals(3, cdr.getRegistrosGerenciados().size());
		assertEquals(1, cdr.getRegistrosNaoEnviados().size());
	}
}
