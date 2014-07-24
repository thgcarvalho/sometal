package br.com.sga.daoImp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.sga.dao.UsuarioDao;
import br.com.sga.util.JPAUtil;
import br.com.sometal.model.Usuario;

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

}
