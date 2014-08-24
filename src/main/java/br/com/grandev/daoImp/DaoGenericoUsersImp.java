package br.com.grandev.daoImp;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.grandev.dao.DaoGenerico;
import br.com.grandev.util.JPAUtil;

/**
 * @author Thiago Carvalho
 * 
 */
public abstract class DaoGenericoUsersImp<T, ID extends Serializable> implements
		DaoGenerico<T, ID>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Class<T> classePersistente;

	@SuppressWarnings("unchecked")
	public DaoGenericoUsersImp() {
		this.classePersistente = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<T> getClassePersistente() {
		return classePersistente;
	}

	public T findById(ID id) {
		EntityManager em = JPAUtil.getUsersEntityManager();
		T t = em.find(getClassePersistente(), id);
		em.close();
		return t;
	}
	
	public Class<T> getObjectClass() {
		return this.classePersistente;
	}
	
	public T criar(T object) throws Exception {
		EntityManager em = JPAUtil.getUsersEntityManager();
		EntityTransaction transaction = em.getTransaction();
		Exception exp = null;
		try {
			transaction.begin();
			em.persist(object);
			transaction.commit();
		} catch (Exception e) {
			exp = e;
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
		if (exp != null) {
			throw exp;
		}
		return object;
	}

	public T editar(T object) throws Exception {
		EntityManager em = JPAUtil.getUsersEntityManager();
		EntityTransaction transaction = em.getTransaction();
		Exception exp = null;
		try {
			transaction.begin();
			em.merge(object);
			transaction.commit();
		} catch (Exception e) {
			exp = e;
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
		} finally {
			em.close();
		}
		if (exp != null) {
			throw exp;
		}
		return object;
	}

	public void excluir(T object) throws Exception {
		EntityManager em = JPAUtil.getUsersEntityManager();
		EntityTransaction transaction = em.getTransaction();
		Exception exp = null;
		try {
			transaction.begin();
			object = em.merge(object);
			em.remove(object);
			transaction.commit();
		} catch (Exception e) {
			exp = e;
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
		} finally {
			em.close();
		}
		if (exp != null) {
			throw exp;
		}
	}

}