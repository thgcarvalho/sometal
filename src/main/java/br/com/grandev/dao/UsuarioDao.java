package br.com.grandev.dao;

import java.util.List;

import br.com.grandev.model.Usuario;

/**
 * @author Thiago Carvalho
 * 
 */
public interface UsuarioDao extends DaoGenerico<Usuario, String> {

	public List<Usuario> todos(String order);

	public Usuario findByUserName(String userName);

	public Usuario findById(Long id);

}
