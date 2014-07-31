package br.com.sometal.dao;

import java.util.List;

import br.com.sometal.model.Role;

/**
 * @author Thiago Carvalho
 * 
 */
public interface RoleDao extends DaoGenerico<Role, String> {
	
	public Role findById(String userName, String roleName);

	public void excluir(String userName, String roleName);

	public List<Role> findByUserName(String userName);

}