package br.com.sometal.bean;

import static br.com.sga.util.FacesUtils.MSG_ERROR;
import static br.com.sga.util.FacesUtils.MSG_SUCESS;
import static br.com.sga.util.FacesUtils.addErrorMessage;
import static br.com.sga.util.FacesUtils.addInfoMessage;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import br.com.sga.dao.UserDao;
import br.com.sga.dao.UsuarioDao;
import br.com.sga.daoImp.UserDaoImp;
import br.com.sga.daoImp.UsuarioDaoImp;
import br.com.sometal.model.User;
import br.com.sometal.model.Usuario;

/**
 * @author Thiago Carvalho
 * 
 */
@ManagedBean
@SessionScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String originalViewId;

	@Resource(name = "usuarioDao")
	private UsuarioDao usuarioDao;
	@Resource(name = "userDao")
	private UserDao userDao;
	private String userName;
	private Usuario usuarioAtual;
	private Usuario usuarioSelecionado;
	private Usuario usuarioNovo;
	private List<Usuario> listaUsuarios;
	private List<Usuario> filteredUsuarios;

	// captura a sessão do contexto criado pelo JavaServer Faces
	FacesContext fc = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);

	@PostConstruct
	public void init() {
		usuarioDao = new UsuarioDaoImp();
		userDao = new UserDaoImp();
		usuarioAtual = new Usuario();
		userName = fc.getExternalContext().getUserPrincipal().getName();
		usuarioAtual = verificaUsuario(userName);
		session.setAttribute("user", usuarioAtual);
		Date data = GregorianCalendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat();
		System.out.println("\nLOGIN:" + usuarioAtual + " " + sdf.format(data));
	}
	
	public void preparaNovoUsuario() {
		usuarioNovo = new Usuario();
	}
	
	public void carregaUsuarios() {
		listaUsuarios = new ArrayList<Usuario>();
		listaUsuarios = usuarioDao.todos("nome");
		filteredUsuarios = listaUsuarios;
	}
	
	
	private Usuario verificaUsuario(String user) {
		try {
			return usuarioDao.findById(user);
		} catch (Exception e) {
			addErrorMessage(MSG_ERROR, e.getMessage());
		}
		return null;
	}
	
	public String logout() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Object session = externalContext.getSession(false);
		HttpSession httpSession = (HttpSession) session;
		httpSession.invalidate();
		try {
			externalContext.redirect(externalContext.getRequestContextPath());
			Date data = GregorianCalendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat();
			System.out.println("\nLOGOUT:" + usuarioAtual + " " + sdf.format(data));
		} catch (IOException e) {
			throw new FacesException("Cannot redirect to / due to IO exception.", e);
		}
		return null;
	}

	public void resetarCampos() {
		setListaUsuarios(new ArrayList<Usuario>());
		this.setUsuario(new Usuario());
	}
	
	public String editarAtual() {
		boolean erro = false;
		String erroMsg = null;
		try {
			System.out.println(userName);
			usuarioDao.editar(usuarioAtual);
			if (!usuarioAtual.getUsuario().equals(userName)) {
				User user = userDao.findById(userName);
				user.setUserName(usuarioAtual.getUsuario());
				System.out.println(user +" "+ user.getUserPass());
				userDao.criar(user);
				user = userDao.findById(userName);
				userDao.excluir(user);
			}
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
			return "usuario";
		}
	}
	
	public String editar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			usuarioDao.editar(usuarioSelecionado);
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
			return "usuarios";
		}
	}
	
	public String salvar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			usuarioDao.criar(usuarioNovo);
		} catch (Exception e) {
			erro = true;
			erroMsg = e.getMessage();
			e.printStackTrace();
		}
		if (erro) {
			addErrorMessage(MSG_ERROR, erroMsg);
			return "";
		} else {
			return "usuarios";
		}
	}

	public String excluir() {
		boolean erro = false;
		String erroMsg = null;
		try {
			usuarioDao.excluir(usuarioSelecionado);
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
			return "usuarios";
		}
	}

	public UsuarioDao getUsuarioDao() {
		return usuarioDao;
	}

	public void setUsuarioDao(UsuarioDao usuarioDao) {
		this.usuarioDao = usuarioDao;
	}

	public Usuario getUsuario() {
		return usuarioAtual;
	}

	public void setUsuario(Usuario usuario) {
		this.usuarioAtual = usuario;
	}
	
	public Usuario getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(Usuario usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public List<Usuario> getListaUsuarios() {
		return listaUsuarios;
	}

	public void setListaUsuarios(List<Usuario> listaUsuarios) {
		this.listaUsuarios = listaUsuarios;
	}
	
    public List<Usuario> getFilteredUsuarios() {  
        return filteredUsuarios;  
    }  
  
    public void setFilteredUsuarios(List<Usuario> filteredUsuarios) {  
        this.filteredUsuarios = filteredUsuarios;  
    }
	
	public Date getDataAtual() {
		return new Date();
	}
	
	public String getOriginalViewId() {
		String temp = originalViewId;
		originalViewId = null;
		return temp;
	}

	public void setOriginalViewId(String originalViewId) {
		this.originalViewId = originalViewId;
	}

}
