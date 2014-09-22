package br.com.grandev.bean;

import static br.com.grandev.util.FacesUtils.MSG_ERROR;
import static br.com.grandev.util.FacesUtils.MSG_SUCESS;
import static br.com.grandev.util.FacesUtils.addErrorMessage;
import static br.com.grandev.util.FacesUtils.addInfoMessage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import br.com.grandev.dao.AbastecimentoDao;
import br.com.grandev.daoImp.AbastecimentoDaoImp;
import br.com.grandev.model.Abastecimento;
import br.com.grandev.model.Veiculo;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@SessionScoped
public class AbastecimentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private AbastecimentoDao abastecimentosDao;
	private Veiculo veiculoSel;
	private Date dataDeSel;
	private Date dataAteSel;
	private Abastecimento abastecimento;
	private Abastecimento abastecimentoSelecionado;
	private List<Abastecimento> abastecimentos;
	private List<Abastecimento> filteredAbastecimentos;
	private BigDecimal valorTotal;
	
	public enum Combustivel {
		ALCOOL("Álcool"),
		DIESEL("Diesel"),
		GAS("Gás"),
		GASOLINA("Gasolina");
		
		private Combustivel(String nome) {
			this.nome = nome;
		}
		String nome;
		public String getNome() {
			return nome;
		}
	}
	
	@PostConstruct
	public void init() {
		abastecimentosDao = new AbastecimentoDaoImp();
		abastecimentos = new ArrayList<Abastecimento>();
		preparaNovoAbastecimento();
		
		// dados do filtro
		Calendar dataAtual = Calendar.getInstance();
		Calendar primeiroDiaDaSDataAtual = Calendar.getInstance();
		primeiroDiaDaSDataAtual.add(Calendar.DAY_OF_MONTH, (-dataAtual.get(Calendar.DAY_OF_MONTH)) + 1);
		veiculoSel = null;
		dataDeSel = primeiroDiaDaSDataAtual.getTime();
		dataAteSel = dataAtual.getTime();
		carregaAbastecimentos();
	}
	
	public void preparaNovoAbastecimento() {
		abastecimento = new Abastecimento();
	}
	
	public void carregaAbastecimentos() {
		abastecimentos = abastecimentosDao.todasOsAbastecimentos(veiculoSel, dataDeSel, dataAteSel);
		valorTotal = BigDecimal.ZERO;
		for (Abastecimento abastecimento : abastecimentos) {
			valorTotal = valorTotal.add(abastecimento.getValor());
		}
		filteredAbastecimentos = abastecimentos;
	}
	
	public Veiculo getVeiculoSel() {
		return veiculoSel;
	}
	public void setVeiculoSel(Veiculo veiculo) {
		this.veiculoSel = veiculo;
	} 
	public Date getDataDeSel() {
		return dataDeSel;
	}
	public void setDataDeSel(Date dataDeSel) {
		this.dataDeSel = dataDeSel;
	}
	public Date getDataAteSel() {
		return dataAteSel;
	}
	public void setDataAteSel(Date dataAteSel) {
		this.dataAteSel = dataAteSel;
	} 
	public List<Abastecimento> getAbastecimentos() {
		return abastecimentos;
	}
	public void setAbastecimentos(List<Abastecimento> abastecimentos) {
		this.abastecimentos = abastecimentos;
	}
	public List<Abastecimento> getFilteredAbastecimentos() {
		return filteredAbastecimentos;
	}
	public void setFilteredAbastecimentos(List<Abastecimento> filteredAbastecimentos) {
		this.filteredAbastecimentos = filteredAbastecimentos;
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
	public Abastecimento getAbastecimentoSelecionado() {
		return abastecimentoSelecionado;
	}
	public void setAbastecimentoSelecionado(Abastecimento abastecimentoSelecionado) {
		this.abastecimentoSelecionado = abastecimentoSelecionado;
	}
	public Combustivel[] getCombustiveis() {
		return Combustivel.values();
	}
	public BigDecimal getConsumo() {
		BigDecimal totalKM = BigDecimal.ZERO;
		BigDecimal totalDeLitros = BigDecimal.ZERO;
		BigDecimal primeiraKM = BigDecimal.ZERO;
		BigDecimal ultimaKM = BigDecimal.ZERO;
		BigDecimal consumo = BigDecimal.ZERO;
		Abastecimento abastecimento;
		
		for (int i = 0; i < abastecimentos.size(); i++) {
			abastecimento = abastecimentos.get(i);
			if (i == 0) {
				primeiraKM = abastecimento.getKm();
			}
			if (i == abastecimentos.size() - 1) {
				ultimaKM = abastecimento.getKm();
				break;
			}
			totalDeLitros = totalDeLitros.add(abastecimento.getLitros());
		}
		totalKM = ultimaKM.subtract(primeiraKM);
		if (totalKM != BigDecimal.ZERO && totalDeLitros != BigDecimal.ZERO) {
			consumo = totalKM.divide(totalDeLitros, 2, RoundingMode.HALF_UP);
		}
		return consumo;
	}
	
	public String editar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			abastecimentosDao.editar(abastecimentoSelecionado);
		} catch (Exception e) {
			erro = true;
			erroMsg = e.getMessage();
			e.printStackTrace();
		}
		if (erro) {
			addErrorMessage(MSG_ERROR, erroMsg);
			return "";
		} else {
			addInfoMessage(MSG_SUCESS);
			carregaAbastecimentos();
			return "abastecimentos";
		}
	}
	
	public String criar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			abastecimentosDao.criar(abastecimento);
			// ajustes
			abastecimentos.add(abastecimento);
		} catch (Exception e) {
			erro = true;
			erroMsg = e.getMessage();
			e.printStackTrace();
		}
		if (erro) {
			addErrorMessage(MSG_ERROR, erroMsg);
			return "";
		} else {
			addInfoMessage(MSG_SUCESS);
			return "abastecimentos";
		}
	}

	public String excluir() {
		boolean erro = false;
		String erroMsg = null;
		try {
			abastecimentosDao.excluir(abastecimentoSelecionado);
		} catch (Exception e) {
			erro = true;
			erroMsg = e.getMessage();
			e.printStackTrace();
		}
		if (erro) {
			addErrorMessage(MSG_ERROR, erroMsg);
			return "";
		} else {
			addInfoMessage(MSG_SUCESS);
			carregaAbastecimentos();
			return "abastecimentos";
		}
	}

}
