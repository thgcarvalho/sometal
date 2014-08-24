package br.com.grandev.dao;

import java.util.List;

import br.com.grandev.model.Role;

/**
 * @author Thiago Carvalho
 * 
 */
public interface RoleDao extends DaoGenerico<Role, String> {
	
	public Role findById(String userName, String roleName);

	public void excluir(String userName, String roleName);

	public List<Role> findByUserName(String userName);

}
