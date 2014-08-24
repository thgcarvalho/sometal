package br.com.grandev.daoImp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.grandev.dao.FornecedorDao;
import br.com.grandev.model.Fornecedor;
import br.com.grandev.util.JPAUtil;

/**
 * @author Thiago Carvalho
 * 
 */
public class FornecedorDaoImp extends DaoGenericoImp<Fornecedor, Long> implements FornecedorDao {

	private static final long serialVersionUID = 1L;

	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> fornecedores() {
		String strQuery = "SELECT f FROM Fornecedor f"
				+ " order by f.nome";
		EntityManager em = JPAUtil.getEntityManager();
		Query query = em.createQuery(strQuery, Fornecedor.class);
		List<Fornecedor> fornecedores = query.getResultList();
		em.close();
		return fornecedores;
	}

}
