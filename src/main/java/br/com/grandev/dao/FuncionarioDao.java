package br.com.grandev.dao;

import br.com.grandev.model.Funcionario;

/**
 * @author Thiago Carvalho
 * 
 */
public interface FuncionarioDao extends DaoGenerico<Funcionario, String> {

	public int getLastCode();
}
