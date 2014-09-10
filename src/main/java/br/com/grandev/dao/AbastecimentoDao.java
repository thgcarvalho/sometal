package br.com.grandev.dao;

import java.util.Calendar;
import java.util.List;

import br.com.grandev.model.Abastecimento;
import br.com.grandev.model.Veiculo;

/**
 * @author Thiago Carvalho
 * 
 */
public interface AbastecimentoDao extends DaoGenerico<Abastecimento, Long> {
	
	public List<Abastecimento> todasOsAbastecimentos(Veiculo veiculo, Calendar dataDe, Calendar dataAte);

}
