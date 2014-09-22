package br.com.grandev.daoImp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
	public List<Abastecimento> todasOsAbastecimentos(Veiculo veiculo, Date dataDe, Date dataAte) {
		List<Abastecimento> abastecimentos = new ArrayList<Abastecimento>();
		Abastecimento abastecimento = null;
		
		String strQuery = "SELECT a FROM Abastecimento a"
				+ "	WHERE a.data >= :dataDe and a.data <= :dataAte"
				+ " order by a.data";
		EntityManager em = JPAUtil.getEntityManager();
		Query query = em.createQuery(strQuery, Abastecimento.class);
		query.setParameter("dataDe", dataDe);
		query.setParameter("dataAte", dataAte);
		abastecimentos = query.getResultList();
		em.close();
		// remvove os abastecimentos dos veículos que não estão no filtro
		if (veiculo != null) {
			for (Iterator<Abastecimento> iterator = abastecimentos.iterator(); iterator.hasNext();) {
				abastecimento = iterator.next();
				if (!abastecimento.getVeiculo().equals(veiculo)) {
					iterator.remove();
				}
			}
		}
		return abastecimentos;
	}
}
