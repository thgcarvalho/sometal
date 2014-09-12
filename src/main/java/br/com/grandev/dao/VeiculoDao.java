package br.com.grandev.dao;

import br.com.grandev.model.Veiculo;


/**
 * @author Thiago Carvalho
 * 
 */
public interface VeiculoDao extends DaoGenerico<Veiculo, Long> {

	public Veiculo findByPlaca(String submittedValue);
}
