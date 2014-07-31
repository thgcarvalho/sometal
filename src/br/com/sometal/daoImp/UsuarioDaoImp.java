package br.com.sometal.daoImp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.sometal.dao.UsuarioDao;
import br.com.sometal.model.Usuario;
import br.com.sometal.util.JPAUtil;

/**
 * @author Thiago Carvalho
 * 
 */
public class UsuarioDaoImp extends DaoGenericoImp<Usuario, String> implements UsuarioDao {

	private static final long serialVersionUID = 1L;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Usuario> todos(String ordem) {
		String strQuery = "SELECT u FROM Usuario u";
		if (ordem != null && !ordem.equals("")) {
			strQuery += " order by " + ordem;
		}
		EntityManager em = JPAUtil.getEntityManager();
		Query query = em.createQuery(strQuery, Usuario.class);
		List<Usuario> todos = query.getResultList();
		em.close();
		return todos;
	}

	@Override
	public Usuario findByUserName(String userName) {
		String strQuery = "SELECT u FROM Usuario u WHERE u.usuario = :usuario";
		EntityManager em = JPAUtil.getEntityManager();
		Query query = em.createQuery(strQuery, Usuario.class);
		query.setParameter("usuario", userName);
		Usuario usuario = (Usuario) query.getSingleResult();
		em.close();
		return usuario;
	}

	@Override
	public Usuario findById(Long id) {
		String strQuery = "SELECT u FROM Usuario u WHERE u.id = :id";
		EntityManager em = JPAUtil.getEntityManager();
		Query query = em.createQuery(strQuery, Usuario.class);
		query.setParameter("id", id);
		Usuario usuario = (Usuario) query.getSingleResult();
		em.close();
		return usuario;
	}

}
