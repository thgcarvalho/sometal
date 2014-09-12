package br.com.grandev.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.view.ViewScoped;

import br.com.grandev.dao.AbastecimentoDao;
import br.com.grandev.daoImp.AbastecimentoDaoImp;
import br.com.grandev.model.Abastecimento;
import br.com.grandev.model.Veiculo;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@ViewScoped
public class AbastecimentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private AbastecimentoDao abastecimentosDao;
	private Veiculo veiculoSel;
	private Calendar dataDeSel;
	private Calendar dataAteSel;
	private Abastecimento abastecimento;
	private List<Abastecimento> abastecimentos;
	private BigDecimal valorTotal;
	
	@PostConstruct
	public void init() {
		setAbastecimento(new Abastecimento());
		abastecimentosDao = new AbastecimentoDaoImp();
		veiculoSel = null;
	}
	
	public void carregaAbastecimentos() {
		Calendar dataAtual = Calendar.getInstance();
		abastecimentos = abastecimentosDao.todasOsAbastecimentos(veiculoSel, dataAtual, dataAtual);
		valorTotal = BigDecimal.ZERO;
		for (Abastecimento abastecimento : abastecimentos) {
			valorTotal = valorTotal.add(abastecimento.getValor());
		}
	}
	
	public Veiculo getVeiculoSel() {
		return veiculoSel;
	}

	public void setVeiculoSel(Veiculo veiculo) {
		this.veiculoSel = veiculo;
	} 
	
	public Calendar getDataDeSel() {
		return dataDeSel;
	}

	public void setdataDeSel(Calendar dataDeSel) {
		this.dataDeSel = dataDeSel;
	}
	
	public Calendar getDataAteSel() {
		return dataAteSel;
	}

	public void setAnoAteSel(Calendar dataAteSel) {
		this.dataAteSel = dataAteSel;
	} 
	
	
	public List<Abastecimento> getAbastecimentos() {
		return abastecimentos;
	}

	public void setAbastecimentos(List<Abastecimento> abastecimentos) {
		this.abastecimentos = abastecimentos;
	}
	
	public BigDecimal getValorTotal() {
		return this.valorTotal;
	}
	
	public Abastecimento getAbastecimento() {
		return abastecimento;
	}

	public void setAbastecimento(Abastecimento abastecimento) {
		this.abastecimento = abastecimento;
	}

}
