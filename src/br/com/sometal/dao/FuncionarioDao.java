package br.com.sometal.dao;

import br.com.sometal.model.Funcionario;

/**
 * @author Thiago Carvalho
 * 
 */
public interface FuncionarioDao extends DaoGenerico<Funcionario, String> {

	public int getLastCode();
}
