package br.com.sometal.bean;

import static br.com.sometal.util.FacesUtils.MSG_ERROR;
import static br.com.sometal.util.FacesUtils.MSG_SUCESS;
import static br.com.sometal.util.FacesUtils.addErrorMessage;
import static br.com.sometal.util.FacesUtils.addInfoMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.sometal.dao.FuncionarioDao;
import br.com.sometal.dao.PontoDao;
import br.com.sometal.daoImp.FuncionarioDaoImp;
import br.com.sometal.daoImp.PontoDaoImp;
import br.com.sometal.model.Funcionario;
import br.com.sometal.model.Ponto;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@ViewScoped
public class PontoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private PontoDao pontoDao;
	private FuncionarioDao funcionarioDao;
	private Ponto pontoSelecionado;
	private List<Funcionario> funcionarios;
	private List<Ponto> pontos;
	private List<Ponto> filteredPontos;
	
	@PostConstruct
	public void init() {
		System.out.println("@ViewScoped PontoBean");
		funcionarioDao = new FuncionarioDaoImp();
		pontoDao = new PontoDaoImp();
		pontoSelecionado = new Ponto();
		carregaFuncionarios();
		carregaPontos();
	}
	
	private void carregaFuncionarios() {
		funcionarios = new ArrayList<Funcionario>();
		try {
			funcionarios = funcionarioDao.todos("nome");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void carregaPontos() {
		Ponto ponto;
		pontos = new ArrayList<Ponto>();
		try {
			//pontos = pontoDao.todos("data");
			for (Funcionario funcionario : funcionarios) {
				ponto = new Ponto();
				ponto.setFuncionario(funcionario);
				ponto.setData(new Date());
				ponto.setEntrada(null);
				ponto.setSaida(null);
				ponto.setObs("");
				pontos.add(ponto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		filteredPontos = pontos;
	}
	
	public String editar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			pontoDao.editar(pontoSelecionado);
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
			carregaPontos();
			return "pontos";
		}
	}
	
	public PontoDao getPontoDao() {
		return pontoDao;
	}

	public void setPontoDao(PontoDao pontoDao) {
		this.pontoDao = pontoDao;
	}

	public Ponto getPontoSelecionado() {
		return pontoSelecionado;
	}

	public void setPontoSelecionado(Ponto pontoSelecionado) {
		this.pontoSelecionado = pontoSelecionado;
	}
	
	public List<Ponto> getPontos() {
		return pontos;
	}

	public void setPontos(List<Ponto> listaPontos) {
		this.pontos = listaPontos;
	}
	
    public List<Ponto> getFilteredPontos() {  
        return filteredPontos;  
    }  
  
    public void setFilteredPontos(List<Ponto> filteredPontos) {  
        this.filteredPontos = filteredPontos;  
    }
    
    private List<String> selectedOptions;
    public List<String> getSelectedOptions() {
    	System.out.println("getSelectedOptions");
        return selectedOptions;
    }
    public void setSelectedOptions(List<String> selectedOptions) {
    	System.out.println("setSelectedOptions="+selectedOptions);
        this.selectedOptions = selectedOptions;
    }

}
