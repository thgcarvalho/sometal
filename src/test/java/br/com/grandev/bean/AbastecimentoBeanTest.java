package br.com.grandev.bean;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.grandev.model.Abastecimento;

public class AbastecimentoBeanTest {

	@Test
	public void deveRetornarOConsumoMedioDoVeiculo() {
		AbastecimentoBean abastecimentoBean = new AbastecimentoBean();
		List<Abastecimento> abastecimentos = new ArrayList<Abastecimento>();
		Abastecimento abst;
		
		abst = new Abastecimento();
		abst.setLitros(new BigDecimal(20));
		abst.setKm(new BigDecimal(1000));
		abastecimentos.add(abst);
		
		abst = new Abastecimento();
		abst.setLitros(new BigDecimal(50));
		abst.setKm(new BigDecimal(1200));
		abastecimentos.add(abst);
		
		abst = new Abastecimento();
		abst.setLitros(new BigDecimal(30));
		abst.setKm(new BigDecimal(1500));
		abastecimentos.add(abst);
		
		abastecimentoBean.setAbastecimentos(abastecimentos);
		assertEquals(new BigDecimal("7.14"), abastecimentoBean.getConsumo());
		
		abastecimentoBean = new AbastecimentoBean();
		abastecimentos = new ArrayList<Abastecimento>();
		
		abst = new Abastecimento();
		abst.setLitros(new BigDecimal(30));
		abst.setKm(new BigDecimal(10000));
		abastecimentos.add(abst);
		
		abst = new Abastecimento();
		abst.setLitros(new BigDecimal(50));
		abst.setKm(new BigDecimal(10400));
		abastecimentos.add(abst);
		
		abst = new Abastecimento();
		abst.setLitros(new BigDecimal(40));
		abst.setKm(new BigDecimal(10900));
		abastecimentos.add(abst);
		
		abastecimentoBean.setAbastecimentos(abastecimentos);
		assertEquals(new BigDecimal("11.25"), abastecimentoBean.getConsumo());
	}
}
