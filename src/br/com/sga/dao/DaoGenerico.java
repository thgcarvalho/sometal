package br.com.sga.dao;

import java.io.Serializable;
import java.util.List;


/**
 * @author Thiago Carvallho
 * 
 */
public interface DaoGenerico<T, ID extends Serializable> {

	public Class<T> getObjectClass();

	public T findById(ID id) throws Exception;
	
	public T criar(T object) throws Exception;

	public T editar(T object) throws Exception;

	public void excluir(T object) throws Exception;

	public List<T> todos(String ordem) throws Exception;

}
