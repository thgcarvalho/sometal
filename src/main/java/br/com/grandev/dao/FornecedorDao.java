package br.com.grandev.dao;

import java.util.List;

import br.com.grandev.model.Fornecedor;


/**
 * @author Thiago Carvalho
 * 
 */
public interface FornecedorDao extends DaoGenerico<Fornecedor, Long> {

	public List<Fornecedor> fornecedores();
	
}
