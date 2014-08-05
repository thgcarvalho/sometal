package br.com.sometal.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.sometal.model.Auth;


/**
 * @author Thiago Carvalho
 * 
 */
public class TestAuth {

	@Test
	public void getRoleName() {
		assertEquals(Auth.getRoleName(Auth.ROLE_ADMIN_DESC), Auth.ROLE_ADMIN);
		assertEquals(Auth.getRoleName(Auth.ROLE_ENCRR_DESC), Auth.ROLE_ENCRR);
		assertEquals(Auth.getRoleName(Auth.ROLE_ESCRT_DESC), Auth.ROLE_ESCRT);
		assertNull(Auth.getRoleName(null));
		assertNull(Auth.getRoleName("null"));
	}
	
	@Test
	public void getRoleDesc() {
		assertEquals(Auth.getRoleDesc(Auth.ROLE_ADMIN), Auth.ROLE_ADMIN_DESC);
		assertEquals(Auth.getRoleDesc(Auth.ROLE_ENCRR), Auth.ROLE_ENCRR_DESC);
		assertEquals(Auth.getRoleDesc(Auth.ROLE_ESCRT), Auth.ROLE_ESCRT_DESC);
		assertNull(Auth.getRoleDesc(null));
		assertNull(Auth.getRoleDesc("null"));
	}
	
	@Test
	public void setAuth() {
		List<String> rolesDesc = null;
		Auth auth = null;
		
		// all
		rolesDesc = new ArrayList<String>();
		rolesDesc.add(Auth.ROLE_ADMIN_DESC);
		rolesDesc.add(Auth.ROLE_ENCRR_DESC);
		rolesDesc.add(Auth.ROLE_ESCRT_DESC);
		auth = new Auth();
		auth.setAuth(rolesDesc);
		assertTrue(auth.getIsAdmin());
		assertTrue(auth.getIsEncrr());
		assertTrue(auth.getIsEscrt());
		
		// admin and encrr
		rolesDesc = new ArrayList<String>();
		rolesDesc.add(Auth.ROLE_ADMIN_DESC);
		rolesDesc.add(Auth.ROLE_ENCRR_DESC);
		auth = new Auth();
		auth.setAuth(rolesDesc);
		assertTrue(auth.getIsAdmin());
		assertTrue(auth.getIsEncrr());
		assertFalse(auth.getIsEscrt());
		
		// only admin
		rolesDesc = new ArrayList<String>();
		rolesDesc.add(Auth.ROLE_ADMIN_DESC);
		auth = new Auth();
		auth.setAuth(rolesDesc);
		assertTrue(auth.getIsAdmin());
		assertFalse(auth.getIsEncrr());
		assertFalse(auth.getIsEscrt());
		
		// none
		rolesDesc = new ArrayList<String>();
		auth = new Auth();
		auth.setAuth(rolesDesc);
		assertFalse(auth.getIsAdmin());
		assertFalse(auth.getIsEncrr());
		assertFalse(auth.getIsEscrt());
		
		// blank
		rolesDesc = new ArrayList<String>();
		rolesDesc.add("");
		rolesDesc.add("");
		rolesDesc.add("");
		auth = new Auth();
		auth.setAuth(rolesDesc);
		assertFalse(auth.getIsAdmin());
		assertFalse(auth.getIsEncrr());
		assertFalse(auth.getIsEscrt());
	}
}
