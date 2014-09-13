package br.com.grandev.dao;

import java.util.Date;
import java.util.List;

import br.com.grandev.model.Abastecimento;
import br.com.grandev.model.Veiculo;

/**
 * @author Thiago Carvalho
 * 
 */
public interface AbastecimentoDao extends DaoGenerico<Abastecimento, Long> {
	
	public List<Abastecimento> todasOsAbastecimentos(Veiculo veiculo, Date dataDeSel, Date dataAteSel);

}
