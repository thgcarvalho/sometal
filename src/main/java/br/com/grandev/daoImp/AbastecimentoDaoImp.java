package br.com.grandev.daoImp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.grandev.dao.AbastecimentoDao;
import br.com.grandev.model.Abastecimento;
import br.com.grandev.model.Veiculo;
import br.com.grandev.util.JPAUtil;

/**
 * @author Thiago Carvalho
 * 
 */
public class AbastecimentoDaoImp extends DaoGenericoImp<Abastecimento, Long> implements AbastecimentoDao {

	private static final long serialVersionUID = 1L;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Abastecimento> todasOsAbastecimentos(Veiculo veiculo, Calendar dataDe, Calendar dataAte) {
		List<Abastecimento> abastecimentos = new ArrayList<Abastecimento>();
		
		String strQuery = "SELECT a FROM Abastecimento a"
				+ "	WHERE a.veiculo = :veiuculo and a.data >= :dataDe and a.data <= :dataAte";
		EntityManager em = JPAUtil.getEntityManager();
		Query query = em.createQuery(strQuery, Abastecimento.class);
		query.setParameter("veiuculo", veiculo);
		query.setParameter("dataDe", dataDe);
		query.setParameter("dataAte", dataAte);
		abastecimentos = query.getResultList();
		em.close();
		return abastecimentos;
	}
}
