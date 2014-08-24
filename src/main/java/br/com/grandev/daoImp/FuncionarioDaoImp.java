package br.com.grandev.daoImp;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.grandev.dao.FuncionarioDao;
import br.com.grandev.model.Funcionario;
import br.com.grandev.util.JPAUtil;

/**
 * @author Thiago Carvalho
 * 
 */
public class FuncionarioDaoImp extends DaoGenericoImp<Funcionario, String> implements FuncionarioDao {

	private static final long serialVersionUID = 1L;
	
	@Override
	public int getLastCode() {
		int lastCode;
		String strQuery = "SELECT MAX(f.codigo) FROM Funcionario f";
		EntityManager em = JPAUtil.getEntityManager();
		Query query = em.createQuery(strQuery, Integer.class);
		try {
			lastCode = (Integer) query.getSingleResult();
		} catch (NullPointerException npEx) {
			lastCode = 0;
		}
		em.close();
		return lastCode;
	}

}
