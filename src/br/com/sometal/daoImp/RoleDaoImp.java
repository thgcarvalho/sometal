package br.com.sometal.daoImp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import br.com.sometal.dao.RoleDao;
import br.com.sometal.model.Role;
import br.com.sometal.util.JPAUtil;

/**
 * @author Thiago Carvalho
 * 
 */
public class RoleDaoImp extends DaoGenericoUsersImp<Role, String> implements RoleDao {

	private static final long serialVersionUID = 1L;
	
	@Override
	public List<Role> todos(String ordem) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role findById(String userName, String roleName) {
		String strQuery = "SELECT r FROM Role r WHERE r.userName = :user and r.roleName = :role";
		EntityManager em = JPAUtil.getUsersEntityManager();
		Query query = em.createQuery(strQuery, Role.class);
		query.setParameter("user", userName);
		query.setParameter("role", roleName);
		Role role = (Role) query.getSingleResult();
		em.close();
		return role;
	}
	
	@Override
	public List<Role> findByUserName(String userName) {
		String strQuery = "SELECT r FROM Role r WHERE r.userName = :user";
		EntityManager em = JPAUtil.getUsersEntityManager();
		Query query = em.createQuery(strQuery, Role.class);
		query.setParameter("user", userName);
		@SuppressWarnings("unchecked")
		List<Role> roles = query.getResultList();
		em.close();
		return roles;
	}
	
	@Override
	public void excluir(String userName, String roleName) {
		EntityManager em = JPAUtil.getUsersEntityManager();
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			Query query = em.createQuery("DELETE FROM Role r WHERE r.userName = :user and r.roleName = :role");
			query.setParameter("user", userName);
			query.setParameter("role", roleName);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
		} finally {
			em.close();
		}
	}

}
