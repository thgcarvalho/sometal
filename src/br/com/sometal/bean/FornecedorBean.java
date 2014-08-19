package br.com.sometal.bean;

import static br.com.sometal.util.FacesUtils.MSG_ERROR;
import static br.com.sometal.util.FacesUtils.MSG_SUCESS;
import static br.com.sometal.util.FacesUtils.addErrorMessage;
import static br.com.sometal.util.FacesUtils.addInfoMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FlowEvent;

import br.com.sometal.dao.FornecedorDao;
import br.com.sometal.daoImp.FornecedorDaoImp;
import br.com.sometal.model.Fornecedor;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@ViewScoped
public class FornecedorBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Resource(name = "fornecedorDao")
	private FornecedorDao fornecedorDao;
	private Fornecedor fornecedor;
	private Fornecedor fornecedorSelecionado;
	private List<Fornecedor> listaFornecedores;
	private List<Fornecedor> filteredFornecedores;
	private String oferta = new String();
	
	@PostConstruct
	public void init() {
		fornecedor = new Fornecedor();
		fornecedorDao = new FornecedorDaoImp();
		carregaFornecedores();
	}
	
	public void preparaNovoFornecedor() {
		fornecedor = new Fornecedor();
	}
	
	public void carregaFornecedores() {
		fornecedor = new Fornecedor();
		listaFornecedores = new ArrayList<Fornecedor>();
		try {
			listaFornecedores = fornecedorDao.fornecedores();
		} catch (Exception e) {
			e.printStackTrace();
		}
		filteredFornecedores = listaFornecedores;
	}
	
	public String editar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			preparaFornecedor(fornecedorSelecionado);
			fornecedorDao.editar(fornecedorSelecionado);
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
			carregaFornecedores();
			return "fornecedores";
		}
	}
	
	public String criar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			preparaFornecedor(fornecedor);
			fornecedorDao.criar(fornecedor);
			fornecedor = new Fornecedor();
			oferta = new String();
			carregaFornecedores();
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
			carregaFornecedores();
			return "fornecedores";
		}
	}

	public String excluir() {
		boolean erro = false;
		String erroMsg = null;
		try {
			fornecedorDao.excluir(fornecedorSelecionado);
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
			carregaFornecedores();
			return "fornecedores";
		}
	}
	
	private void preparaFornecedor(Fornecedor fornecedor) {
		fornecedor.setNome(fornecedor.getNome().trim());
		String todasAsOfertas = "";
		for (int i = 0; i < fornecedor.getOfertas().size(); i++) {
			if (i < 10) {
				todasAsOfertas += fornecedor.getOfertas().get(i) + "<br />";
			}
		}
		fornecedor.setOferta(todasAsOfertas);
	}
	
	// getters and setters

	public FornecedorDao getFornecedorDao() {
		return fornecedorDao;
	}

	public void setFornecedorDao(FornecedorDao fornecedorDao) {
		this.fornecedorDao = fornecedorDao;
	}

	public Fornecedor getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new Fornecedor();
		}
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public Fornecedor getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}

	public void setFornecedorSelecionado(Fornecedor fornecedorSelecionado) {
		// copia a lista para evitar org.hibernate.LazyInitializationException: 
		// failed to lazily initialize a collection, no session or session was closed
		if (fornecedorSelecionado != null) {
			fornecedorSelecionado.setOfertas(fornecedorSelecionado.getOfertasDB());
		}
		this.fornecedorSelecionado = fornecedorSelecionado;
	}
	
	public List<Fornecedor> getFornecedores() {
		return listaFornecedores;
	}

	public void setFornecedores(List<Fornecedor> listaFornecedores) {
		this.listaFornecedores = listaFornecedores;
	}
	
    public List<Fornecedor> getFilteredFornecedores() {  
        return filteredFornecedores;  
    }  
  
    public void setFilteredFornecedores(List<Fornecedor> filteredFornecedores) {  
        this.filteredFornecedores = filteredFornecedores;  
    }
    
    public String reinit() {  
    	oferta = new String();  
    	if (fornecedor != null && fornecedor.getOfertas() != null) {
    		String oferta;
    		for (Iterator<String> iterator = fornecedor.getOfertas().iterator(); iterator.hasNext();) {
    			oferta = iterator.next();
    			if (oferta == null || oferta.equals("")) {
    				iterator.remove();
    			}
    		}
    	}
    	if (fornecedorSelecionado != null && fornecedorSelecionado.getOfertas() != null) {
    		String oferta;
    		for (Iterator<String> iterator = fornecedorSelecionado.getOfertas().iterator(); iterator.hasNext();) {
    			oferta = iterator.next();
    			if (oferta == null || oferta.equals("")) {
    				iterator.remove();
    			}
    		}
    	}
        return null;  
    }  
  
    public String getOferta() {  
        return oferta;  
    }  
  
    public void setOferta(String oferta) {  
        this.oferta = oferta;  
    }
	
	public String onFlowProcess(FlowEvent event) {
			return event.getNewStep();
	}
    
}
