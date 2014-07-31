package br.com.sometal.bean;

import static br.com.sometal.util.FacesUtils.MSG_ERROR;
import static br.com.sometal.util.FacesUtils.MSG_SUCESS;
import static br.com.sometal.util.FacesUtils.addErrorMessage;
import static br.com.sometal.util.FacesUtils.addInfoMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.sometal.dao.FuncionarioDao;
import br.com.sometal.daoImp.FuncionarioDaoImp;
import br.com.sometal.model.Funcionario;
import br.com.sometal.service.PathServer;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@ViewScoped
public class FuncionarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Resource(name = "funcionarioDao")
	private FuncionarioDao funcionarioDao;
	private Funcionario funcionario;
	private Funcionario funcionarioSelecionado;
	private List<Funcionario> funcionarios;
	private List<Funcionario> filteredFuncionarios;
	private UploadedFile uploadedFile;
	
	@PostConstruct
	public void init() {
		System.out.println("@ViewScoped FuncionarioBean");
		funcionarioDao = new FuncionarioDaoImp();
		preparaNovoFuncionario();
		carregaFuncionarios();
	}
	
	public void preparaNovoFuncionario() {
		funcionario = new Funcionario();
		int lastCode = funcionarioDao.getLastCode();
		System.out.println(lastCode);
		if (lastCode < 1000) {
			lastCode = 1000;
		}
		funcionario.setCodigo(lastCode + 1);
	}
	
	private void carregaFuncionarios() {
		funcionarios = new ArrayList<Funcionario>();
		try {
			funcionarios = funcionarioDao.todos("nome");
		} catch (Exception e) {
			e.printStackTrace();
		}
		filteredFuncionarios = funcionarios;
	}
	
	public String editar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			preparaFuncionario(funcionarioSelecionado);
			funcionarioDao.editar(funcionarioSelecionado);
//			if (uploadedFile != null) {
//				 copyFile(funcionarioSelecionado.getFoto());
//			}
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
			carregaFuncionarios();
			return "funcionarios";
		}
	}
	
	public String criar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			preparaFuncionario(funcionario);
			funcionarioDao.criar(funcionario);
			// ajustes
			funcionarios.add(funcionario);
			preparaNovoFuncionario();
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
			return "funcionarios";
		}
	}

	public String excluir() {
		boolean erro = false;
		String erroMsg = null;
		try {
			funcionarioDao.excluir(funcionarioSelecionado);
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
			carregaFuncionarios();
			return "funcionarios";
		}
	}
	
	private void preparaFuncionario(Funcionario funcionario) {
		funcionario.setNome(funcionario.getNome().trim());
	}

	public FuncionarioDao getFuncionarioDao() {
		return funcionarioDao;
	}

	public void setFuncionarioDao(FuncionarioDao funcionarioDao) {
		this.funcionarioDao = funcionarioDao;
	}

	public Funcionario getFuncionario() {
		if (funcionario == null) {
			funcionario = new Funcionario();
		}
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	public Funcionario getFuncionarioSelecionado() {
		return funcionarioSelecionado;
	}

	public void setFuncionarioSelecionado(Funcionario funcionarioSelecionado) {
		this.funcionarioSelecionado = funcionarioSelecionado;
	}
	
	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> listaFuncionarios) {
		this.funcionarios = listaFuncionarios;
	}
	
    public List<Funcionario> getFilteredFuncionarios() {  
        return filteredFuncionarios;  
    }  
  
    public void setFilteredFuncionarios(List<Funcionario> filteredFuncionarios) {  
        this.filteredFuncionarios = filteredFuncionarios;  
    }
    
	public void photoUpload(FileUploadEvent event) {
		String destination = PathServer.PATH_PUBLIC 
				+ File.separator + PathServer.PATH_DIR 
				+ File.separator + PathServer.DIR_FOTOS;
		String fileName = String.valueOf(funcionario.getCodigo());
		funcionario.setFoto(destination + File.separator + fileName);
		uploadedFile= event.getFile();
		copyFile(funcionarioSelecionado.getFoto());
		addInfoMessage(MSG_SUCESS, event.getFile().getFileName() + " foi carregado.");
	}
	
	private void copyFile(String file) {
		try {
			if (uploadedFile != null) {
				InputStream inputStream = uploadedFile.getInputstream();
				// write the inputStream to a FileOutputStream
				OutputStream out = new FileOutputStream(new File(file));
				int read = 0;
				byte[] bytes = new byte[1024];
	
				while ((read = inputStream.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				inputStream.close();
				out.flush();
				out.close();
				uploadedFile = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}