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

import br.com.grandev.dao.ViagemDao;
import br.com.grandev.daoImp.ViagemDaoImp;
import br.com.grandev.model.Viagem;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@ViewScoped
public class ViagemBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ViagemDao viagemDao;
	private Viagem viagem;
	private Viagem viagemSelecionada;
	private List<Viagem> viagens;
	private List<Viagem> filteredViagens;
	
	@PostConstruct
	public void init() {
		viagemDao = new ViagemDaoImp();
		viagemSelecionada = new Viagem();
		preparaNovaViagem();
		carregaViagens();
	}
	
	public void preparaNovaViagem() {
		viagem = new Viagem();
	}
	
	private void carregaViagens() {
		viagens = new ArrayList<Viagem>();
		try {
			viagens = viagemDao.todos("data DESC");
		} catch (Exception e) {
			e.printStackTrace();
		}
		filteredViagens = viagens;
	}
	
	public String editar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			viagemDao.editar(viagemSelecionada);
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
			carregaViagens();
			return "viagens";
		}
	}
	
	public String criar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			viagemDao.criar(viagem);
			// ajustes
			viagens.add(viagem);
			preparaNovaViagem();
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
			return "viagens";
		}
	}

	public String excluir() {
		boolean erro = false;
		String erroMsg = null;
		try {
			viagemDao.excluir(viagemSelecionada);
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
			carregaViagens();
			return "viagens";
		}
	}
	
	public ViagemDao getViagemDao() {
		return viagemDao;
	}

	public void setViagemDao(ViagemDao viagemDao) {
		this.viagemDao = viagemDao;
	}

	public Viagem getViagem() {
		if (viagem == null) {
			viagem = new Viagem();
		}
		return viagem;
	}

	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}
	
	public Viagem getViagemSelecionada() {
		return viagemSelecionada;
	}

	public void setViagemSelecionada(Viagem viagemSelecionada) {
		this.viagemSelecionada = viagemSelecionada;
	}
	
	public List<Viagem> getViagens() {
		return viagens;
	}

	public void setViagens(List<Viagem> listaViagens) {
		this.viagens = listaViagens;
	}
	
    public List<Viagem> getFilteredViagens() {  
        return filteredViagens;  
    }  
  
    public void setFilteredViagens(List<Viagem> filteredViagens) {  
        this.filteredViagens = filteredViagens;  
    }
    
}
