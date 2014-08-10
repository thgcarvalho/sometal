package br.com.sometal.bean;

import static br.com.sometal.util.FacesUtils.MSG_ERROR;
import static br.com.sometal.util.FacesUtils.MSG_SUCESS;
import static br.com.sometal.util.FacesUtils.addErrorMessage;
import static br.com.sometal.util.FacesUtils.addInfoMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.sometal.dao.PontoDao;
import br.com.sometal.daoImp.PontoDaoImp;
import br.com.sometal.model.Ponto;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@ViewScoped
public class PontoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Resource(name = "pontoDao")
	private PontoDao pontoDao;
	private Ponto pontoSelecionado;
	private List<Ponto> pontos;
	private List<Ponto> filteredPontos;
	
	@PostConstruct
	public void init() {
		System.out.println("@ViewScoped PontoBean");
		pontoDao = new PontoDaoImp();
		pontoSelecionado = new Ponto();
		carregaPontos();
	}
	
	private void carregaPontos() {
		pontos = new ArrayList<Ponto>();
		try {
			pontos = pontoDao.todos("data");
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

}
