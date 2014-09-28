package br.com.grandev.daoImp;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.com.grandev.dao.ViagemDao;
import br.com.grandev.model.Veiculo;
import br.com.grandev.model.Viagem;
import br.com.grandev.util.JPAUtil;

/**
 * @author Thiago Carvalho
 * 
 */
public class ViagemDaoImp extends DaoGenericoImp<Viagem, Long> implements ViagemDao {

	private static final long serialVersionUID = 1L;

	@Override
	public Veiculo findByPlaca(String placa) {
		String strQuery = "SELECT v FROM Viagem v WHERE v.veiculo.placa = :placa";
		EntityManager em = JPAUtil.getEntityManager();
		Query query = em.createQuery(strQuery, Veiculo.class);
		query.setParameter("placa", placa);
		Veiculo veiculo = null;
		try {
			veiculo = (Veiculo) query.getSingleResult();
		} catch (NoResultException nrEx) {
			
		} finally {
			em.close();
		}
		return veiculo;
	}
	
}
