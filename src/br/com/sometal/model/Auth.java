package br.com.sometal.model;

import java.util.List;


/**
 * @author Thiago Carvalho
 * 
 */
public class Auth {

	public final static String ROLE = "sometal";
	public final static String ROLE_ADMIN = "sometal-admin";
	public final static String ROLE_ADMIN_DESC = "Administrador";
	public final static String ROLE_ENCRR = "sometal-encrr";
	public final static String ROLE_ENCRR_DESC = "Encarregado";
	public final static String ROLE_ESCRT = "sometal-escrt";
	public final static String ROLE_ESCRT_DESC = "Escritório";
	
	private boolean admin = false;
	private boolean encrr = false;
	private boolean escrt = false;
	
	public static String getRoleName(String roleDesc) {
		if (roleDesc != null && roleDesc.equals(ROLE_ADMIN_DESC)) {
			return ROLE_ADMIN;
		}
		if (roleDesc != null && roleDesc.equals(ROLE_ENCRR_DESC)) {
			return ROLE_ENCRR;
		}
		if (roleDesc != null && roleDesc.equals(ROLE_ESCRT_DESC)) {
			return ROLE_ESCRT;
		}
		return null;
	}
	
	public static String getRoleDesc(String roleName) {
		if (roleName != null && roleName.equals(ROLE_ADMIN)) {
			return ROLE_ADMIN_DESC;
		}
		if (roleName != null && roleName.equals(ROLE_ENCRR)) {
			return ROLE_ENCRR_DESC;
		}
		if (roleName != null && roleName.equals(ROLE_ESCRT)) {
			return ROLE_ESCRT_DESC;
		}
		return null;
	}
	
	public void setAuth(List<String> rolesDesc) {
		this.admin = false;
		this.encrr = false;
		this.escrt = false;
		for (String roleDesc : rolesDesc) {
			if (roleDesc != null && roleDesc.equals(ROLE_ADMIN_DESC)) {
				this.admin = true;
			}
			if (roleDesc != null && roleDesc.equals(ROLE_ENCRR_DESC)) {
				this.encrr = true;
			}
			if (roleDesc != null && roleDesc.equals(ROLE_ESCRT_DESC)) {
				this.escrt = true;
			}
		}
	}
	
	public boolean getIsAdmin() {
		return this.admin;
	}
	
	public boolean getIsEncrr() {
		return this.encrr;
	}
	
	public boolean getIsEscrt() {
		return this.escrt;
	}

}
