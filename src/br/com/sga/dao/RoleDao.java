package br.com.sga.dao;

import br.com.sometal.model.Role;

/**
 * @author Thiago Carvalho
 * 
 */
public interface RoleDao extends DaoGenerico<Role, String> {
	
	public Role findById(String userName, String roleName);

	void excluir(String userName, String roleName);

}
