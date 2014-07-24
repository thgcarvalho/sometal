package br.com.sga.daoImp;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import br.com.sga.dao.DaoGenerico;
import br.com.sga.util.JPAUtil;

/**
 * @author Thiago Carvalho
 * 
 */
public abstract class DaoGenericoImp<T, ID extends Serializable> implements
		DaoGenerico<T, ID>, Serializable {

	private static final long serialVersionUID = 1L;
	
	private final Class<T> classePersistente;

	@SuppressWarnings("unchecked")
	public DaoGenericoImp() {
		this.classePersistente = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public Class<T> getClassePersistente() {
		return classePersistente;
	}

	public T findById(ID id) {
		EntityManager em = JPAUtil.getEntityManager();
		T t = em.find(getClassePersistente(), id);
		em.close();
		return t;
	}
	
	public Class<T> getObjectClass() {
		return this.classePersistente;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> todos(String ordem) throws Exception {
		List<T> todos;
		try {
			String strQuery = "SELECT obj FROM " + classePersistente.getSimpleName() + " obj";
			if (ordem != null && !ordem.equals("")) {
				strQuery += " order by " + ordem;
			}
			EntityManager em = JPAUtil.getEntityManager();
			Query query = em.createQuery(strQuery);
			todos = query.getResultList();
			em.close();
		} catch (Exception e) {
			throw e;
		}
		return todos;
	}

	public T criar(T object) throws Exception {
		EntityManager em = JPAUtil.getEntityManager();
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
		EntityManager em = JPAUtil.getEntityManager();
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
		EntityManager em = JPAUtil.getEntityManager();
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