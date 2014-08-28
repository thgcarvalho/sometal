package br.com.grandev.bean;

import static br.com.grandev.util.FacesUtils.MSG_ERROR;
import static br.com.grandev.util.FacesUtils.MSG_SUCESS;
import static br.com.grandev.util.FacesUtils.addErrorMessage;
import static br.com.grandev.util.FacesUtils.addInfoMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.CellEditEvent;

import br.com.grandev.dao.ContaDao;
import br.com.grandev.daoImp.ContaDaoImp;
import br.com.grandev.model.Conta;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@ViewScoped
public class ContaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContaDao contaDao;
	private Conta conta;
	private Conta contaSelecionado;
	private List<Conta> contas;
	private List<Conta> filteredContas;
	
	@PostConstruct
	public void init() {
		contaDao = new ContaDaoImp();
		preparaNovaConta();
		contaSelecionado = new Conta();
		carregaContas();
	}
	
	public enum Situacao {
		AGUARDANDO("Aguardando"), RECEBIDA("Recebida"), PAGA("Paga");
		private Situacao(String nome) {
			this.nome = nome;
		}
		String nome;
		public String getNome() {
			return nome;
		}
	}
	
	public void preparaNovaConta() {
		conta = new Conta();
	}
	
	private void carregaContas() {
		contas = new ArrayList<Conta>();
		try {
			contas = contaDao.todos("nome");
		} catch (Exception e) {
			e.printStackTrace();
		}
		filteredContas = contas;
	}
	
	public String editar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			preparaConta(contaSelecionado);
			contaDao.editar(contaSelecionado);
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
			carregaContas();
			return "contas";
		}
	}
	
	public String criar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			preparaConta(conta);
			contaDao.criar(conta);
			// ajustes
			contas.add(conta);
			preparaNovaConta();
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
			return "contas";
		}
	}

	public String excluir() {
		boolean erro = false;
		String erroMsg = null;
		try {
			contaDao.excluir(contaSelecionado);
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
			carregaContas();
			return "contas";
		}
	}
	
	private void preparaConta(Conta conta) {
		conta.setNome(conta.getNome().trim());
	}

	public ContaDao getContaDao() {
		return contaDao;
	}

	public void setContaDao(ContaDao contaDao) {
		this.contaDao = contaDao;
	}

	public Conta getConta() {
		if (conta == null) {
			conta = new Conta();
		}
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}
	
	public Conta getContaSelecionado() {
		return contaSelecionado;
	}

	public void setContaSelecionado(Conta contaSelecionado) {
		this.contaSelecionado = contaSelecionado;
	}
	
	public List<Conta> getContas() {
		return contas;
	}

	public void setContas(List<Conta> listaContas) {
		this.contas = listaContas;
	}
	
    public List<Conta> getFilteredContas() {  
        return filteredContas;  
    }  
  
    public void setFilteredContas(List<Conta> filteredContas) {  
        this.filteredContas = filteredContas;  
    }
    
	public Situacao[] getSituacao() {
		return Situacao.values();
	}
	
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Situa��o Alterada", "Antes: " + oldValue + ", Agora:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
    
    
    public void onCellEdit(Conta conta) {
    	System.out.println(conta.getSituacao());
    }
}
