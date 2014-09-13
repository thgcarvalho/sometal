package br.com.grandev.bean;

import static br.com.grandev.util.FacesUtils.MSG_ERROR;
import static br.com.grandev.util.FacesUtils.MSG_SUCESS;
import static br.com.grandev.util.FacesUtils.addErrorMessage;
import static br.com.grandev.util.FacesUtils.addInfoMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.grandev.dao.VeiculoDao;
import br.com.grandev.daoImp.VeiculoDaoImp;
import br.com.grandev.model.Data;
import br.com.grandev.model.DataAtual;
import br.com.grandev.model.Veiculo;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@ViewScoped
public class VeiculoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private VeiculoDao veiculoDao;
	private Veiculo veiculo;
	private Veiculo veiculoSelecionado;
	private List<Veiculo> veiculos;
	private List<Veiculo> filteredVeiculos;
	private List<Integer> anos;
	private final Data data;
	
	public VeiculoBean() {
		this(new DataAtual());
	}
	
	public VeiculoBean(Data data) {
		this.data = data;
	}
	
	public enum Cor {
		AMARELO("Amarelo"), 
		AZUL("Azul"), 
		BEGE("Bege"), 
		BRANCO("Branco"),
		CINZA("Cinza"),
		DOURADO("Dourado"),
		GRAFITE("Grafite"),
		LARANJA("Laranja"),
		MARROM("Marrom"),
		PINK("Pink"),
		PRATA("Prata"),
		PRETO("Preto"),
		ROXO("Roxo"),
		VERDE("Verde"),
		VERMELHO("Vermelho"),
		TURQUESA("Turquesa"),
		OUTRA("Outra");
		
		private Cor(String nome) {
			this.nome = nome;
		}
		String nome;
		public String getNome() {
			return nome;
		}
	}
	
	public enum Combustivel {
		ALCOOL("Álcool"),
		DIESEL("Diesel"),
		GAS("Gás"),
		GASOLINA("Gasolina"), 
		FLEX("Flex");
		
		private Combustivel(String nome) {
			this.nome = nome;
		}
		String nome;
		public String getNome() {
			return nome;
		}
	}
	
	public void carregaDatas() {
		Calendar calendar = this.data.hoje();
		this.anos = new ArrayList<Integer>();
		// next year
		calendar.add(Calendar.YEAR, 1);
		this.anos.add(calendar.get(Calendar.YEAR));
		
		for (int i = 0; i < 20; i++) {
			calendar.add(Calendar.YEAR, -1);
			this.anos.add(calendar.get(Calendar.YEAR));
		}
	}
	
	@PostConstruct
	public void init() {
		veiculoDao = new VeiculoDaoImp();
		veiculoSelecionado = new Veiculo();
		preparaNovoVeiculo();
		carregaVeiculos();
		carregaDatas();
	}
	
	public void preparaNovoVeiculo() {
		veiculo = new Veiculo();
	}
	
	private void carregaVeiculos() {
		veiculos = new ArrayList<Veiculo>();
		try {
			veiculos = veiculoDao.todos("marca");
		} catch (Exception e) {
			e.printStackTrace();
		}
		filteredVeiculos = veiculos;
	}
	
	public String editar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			veiculoDao.editar(veiculoSelecionado);
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
			carregaVeiculos();
			return "veiculos";
		}
	}
	
	public String criar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			veiculoDao.criar(veiculo);
			// ajustes
			veiculos.add(veiculo);
			preparaNovoVeiculo();
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
			return "veiculos";
		}
	}

	public String excluir() {
		boolean erro = false;
		String erroMsg = null;
		try {
			veiculoDao.excluir(veiculoSelecionado);
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
			carregaVeiculos();
			return "veiculos";
		}
	}
	
	public VeiculoDao getVeiculoDao() {
		return veiculoDao;
	}

	public void setVeiculoDao(VeiculoDao veiculoDao) {
		this.veiculoDao = veiculoDao;
	}

	public Veiculo getVeiculo() {
		if (veiculo == null) {
			veiculo = new Veiculo();
		}
		return veiculo;
	}

	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
	public Veiculo getVeiculoSelecionado() {
		return veiculoSelecionado;
	}

	public void setVeiculoSelecionado(Veiculo veiculoSelecionado) {
		this.veiculoSelecionado = veiculoSelecionado;
	}
	
	public List<Veiculo> getVeiculos() {
		return veiculos;
	}

	public void setVeiculos(List<Veiculo> listaVeiculos) {
		this.veiculos = listaVeiculos;
	}
	
    public List<Veiculo> getFilteredVeiculos() {  
        return filteredVeiculos;  
    }  
  
    public void setFilteredVeiculos(List<Veiculo> filteredVeiculos) {  
        this.filteredVeiculos = filteredVeiculos;  
    }
    
	public Cor[] getCores() {
		return Cor.values();
	}
	
	public Combustivel[] getCombustiveis() {
		return Combustivel.values();
	}
	
	public List<Integer> getAnos() {
        return anos;
    } 
    
}
