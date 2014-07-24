package br.com.sga.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Thiago Carvalho
 * 
 */
public class JPAUtil {

	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("SOMETAL");
	private static final EntityManagerFactory usersEmf = Persistence.createEntityManagerFactory("USERS");
	
	public static EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
	
	public static EntityManager getUsersEntityManager() {
		return usersEmf.createEntityManager();
	}
}
