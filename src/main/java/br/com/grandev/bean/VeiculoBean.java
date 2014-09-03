package br.com.grandev.bean;

import static br.com.grandev.util.FacesUtils.MSG_ERROR;
import static br.com.grandev.util.FacesUtils.MSG_SUCESS;
import static br.com.grandev.util.FacesUtils.addErrorMessage;
import static br.com.grandev.util.FacesUtils.addInfoMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.grandev.dao.VeiculoDao;
import br.com.grandev.daoImp.VeiculoDaoImp;
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
	
	@PostConstruct
	public void init() {
		veiculoDao = new VeiculoDaoImp();
		preparaNovoVeiculo();
		veiculoSelecionado = new Veiculo();
		carregaVeiculos();
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
    
}
