package br.com.sometal.model;


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
	public final static String ROLE_PORTR = "sometal-portr";
	public final static String ROLE_PORTR_DESC = "Portaria";
	
	public String getRoleName(String roleDesc) {
		if (roleDesc.equals(ROLE_ADMIN_DESC)) {
			return ROLE_ADMIN;
		}
		if (roleDesc.equals(ROLE_ENCRR_DESC)) {
			return ROLE_ENCRR;
		}
		if (roleDesc.equals(ROLE_PORTR_DESC)) {
			return ROLE_PORTR;
		}
		return null;
	}
	
	public String getRoleDesc(String roleName) {
		if (roleName.equals(ROLE_ADMIN)) {
			return ROLE_ADMIN_DESC;
		}
		if (roleName.equals(ROLE_ENCRR)) {
			return ROLE_ENCRR_DESC;
		}
		if (roleName.equals(ROLE_PORTR)) {
			return ROLE_PORTR_DESC;
		}
		return null;
	}
	
	public boolean isAdmin() {
		return false;
	}
}
