package br.com.sometal.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Thiago Carvalho
 * 
 */
@Entity
@Table(name = "user_roles")
public class Role implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="user_name")
	private String userName;
	
	@Column(name="role_name")
	private String roleName;

	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return userName;
	}
}
