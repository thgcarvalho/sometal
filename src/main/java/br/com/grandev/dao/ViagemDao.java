package br.com.grandev.dao;

import br.com.grandev.model.Veiculo;
import br.com.grandev.model.Viagem;


/**
 * @author Thiago Carvalho
 * 
 */
public interface ViagemDao extends DaoGenerico<Viagem, Long> {

	public Veiculo findByPlaca(String submittedValue);
}
