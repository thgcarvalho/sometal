package br.com.sometal.bean;

import static br.com.sometal.util.FacesUtils.MSG_ERROR;
import static br.com.sometal.util.FacesUtils.MSG_SUCESS;
import static br.com.sometal.util.FacesUtils.addErrorMessage;
import static br.com.sometal.util.FacesUtils.addInfoMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import br.com.sometal.dao.ClienteDao;
import br.com.sometal.daoImp.ClienteDaoImp;
import br.com.sometal.model.Cliente;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@ViewScoped
public class ClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private ClienteDao clienteDao;
	private Cliente cliente;
	private Cliente clienteSelecionado;
	private List<Cliente> clientes;
	private List<Cliente> filteredClientes;
	
	@PostConstruct
	public void init() {
		clienteDao = new ClienteDaoImp();
		preparaNovoCliente();
		clienteSelecionado = new Cliente();
		carregaClientes();
	}
	
	public void preparaNovoCliente() {
		cliente = new Cliente();
	}
	
	private void carregaClientes() {
		clientes = new ArrayList<Cliente>();
		try {
			clientes = clienteDao.todos("nome");
		} catch (Exception e) {
			e.printStackTrace();
		}
		filteredClientes = clientes;
	}
	
	public String editar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			preparaCliente(clienteSelecionado);
			clienteDao.editar(clienteSelecionado);
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
			carregaClientes();
			return "clientes";
		}
	}
	
	public String criar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			preparaCliente(cliente);
			clienteDao.criar(cliente);
			// ajustes
			clientes.add(cliente);
			preparaNovoCliente();
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
			return "clientes";
		}
	}

	public String excluir() {
		boolean erro = false;
		String erroMsg = null;
		try {
			clienteDao.excluir(clienteSelecionado);
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
			carregaClientes();
			return "clientes";
		}
	}
	
	private void preparaCliente(Cliente cliente) {
		cliente.setNome(cliente.getNome().trim());
	}

	public ClienteDao getClienteDao() {
		return clienteDao;
	}

	public void setClienteDao(ClienteDao clienteDao) {
		this.clienteDao = clienteDao;
	}

	public Cliente getCliente() {
		if (cliente == null) {
			cliente = new Cliente();
		}
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}
	
	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> listaClientes) {
		this.clientes = listaClientes;
	}
	
    public List<Cliente> getFilteredClientes() {  
        return filteredClientes;  
    }  
  
    public void setFilteredClientes(List<Cliente> filteredClientes) {  
        this.filteredClientes = filteredClientes;  
    }
    
}
