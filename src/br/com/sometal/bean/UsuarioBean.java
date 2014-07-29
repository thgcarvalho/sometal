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

import br.com.sga.dao.RoleDao;
import br.com.sga.dao.UserDao;
import br.com.sga.dao.UsuarioDao;
import br.com.sga.daoImp.RoleDaoImp;
import br.com.sga.daoImp.UserDaoImp;
import br.com.sga.daoImp.UsuarioDaoImp;
import br.com.sometal.model.Role;
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
	@Resource(name = "roleDao")
	private RoleDao roleDao;
	private String userName;
	private Usuario usuarioAtual;
	private Usuario usuarioSelecionado;
	private Usuario usuarioNovo;
	private List<Usuario> listaUsuarios;
	private List<Usuario> filteredUsuarios;
	private final String ROLE = "sometal";
	private final String ROLE_ADMIN = "sometal-admin";
	private final String ROLE_ADMIN_DESC = "Administrador";
	private final String ROLE_ENCRR = "sometal-encrr";
	private final String ROLE_ENCRR_DESC = "Encarregado";
	private final String ROLE_PORTR = "sometal-portr";
	private final String ROLE_PORTR_DESC = "Portaria";
    private List<String> roles;
	private SimpleDateFormat sdf = new SimpleDateFormat();

	// captura a sessão do contexto criado pelo JavaServer Faces
	FacesContext fc = FacesContext.getCurrentInstance();
	HttpSession session = (HttpSession) fc.getExternalContext().getSession(true);

	@PostConstruct
	public void init() {
		usuarioDao = new UsuarioDaoImp();
		userDao = new UserDaoImp();
		roleDao = new RoleDaoImp();
		
		usuarioNovo = new Usuario();
		usuarioAtual = new Usuario();
		usuarioSelecionado = new Usuario();
		
		roles = new ArrayList<String>();
		roles.add(ROLE_ADMIN_DESC);
		roles.add(ROLE_ENCRR_DESC);
		roles.add(ROLE_PORTR_DESC);
		
		try {
			userName = fc.getExternalContext().getUserPrincipal().getName();
			usuarioAtual = usuarioDao.findByUserName(userName);
			session.setAttribute("user", usuarioAtual);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Date data = GregorianCalendar.getInstance().getTime();
		System.out.println("\nLOGIN:" + usuarioAtual + " " + sdf.format(data));
	}
	
	public String getRoleName(String roleDesc) {
		if (roleDesc.equals(ROLE_ADMIN_DESC)) {
			return ROLE_ADMIN;
		}
		if (roleDesc.equals(ROLE_ENCRR_DESC)) {
			return ROLE_ENCRR;
		}
		if (roleDesc.equals(ROLE_PORTR_DESC)) {
			return ROLE_PORTR;
		}
		return null;
	}
	
	public String getRoleDesc(String roleName) {
		if (roleName.equals(ROLE_ADMIN)) {
			return ROLE_ADMIN_DESC;
		}
		if (roleName.equals(ROLE_ENCRR)) {
			return ROLE_ENCRR_DESC;
		}
		if (roleName.equals(ROLE_PORTR)) {
			return ROLE_PORTR_DESC;
		}
		return null;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	public void preparaNovoUsuario() {
		usuarioNovo = new Usuario();
	}
	
	public void carregaUsuarios() {
		listaUsuarios = new ArrayList<Usuario>();
		listaUsuarios = usuarioDao.todos("nome");
		listaUsuarios.remove(usuarioAtual);
		
		List<Role> rolesDoUsuario;
		List<String> rolesDoUsuarioStr;
		for (Usuario usuario : listaUsuarios) {
			rolesDoUsuario = roleDao.findByUserName(usuario.getUsuario());
			rolesDoUsuarioStr = new ArrayList<String>();
			for (Role roleDoUsuario : rolesDoUsuario) {
				rolesDoUsuarioStr.add(getRoleDesc(roleDoUsuario.getRoleName()));
			}
			usuario.setRoles(rolesDoUsuarioStr);
		}
		filteredUsuarios = listaUsuarios;
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
			// TODO distribuir responsabilidades
			System.out.println(usuarioAtual.getUsuario());
			System.out.println(userName);
			if (usuarioAtual.getUsuario().equals(userName) || userDao.findById(usuarioAtual.getUsuario()) == null) {
				// usuario
				Usuario usuarioDB = usuarioDao.findById(usuarioAtual.getId());
				usuarioDao.editar(usuarioAtual);
				
				// tomcat users
				User user = userDao.findById(usuarioDB.getUsuario());
				userDao.excluir(user);
				user = new User();
				user.setUserName(usuarioAtual.getUsuario());
				user.setUserPass(usuarioAtual.getSenha());
				userDao.criar(user);
				
				// tomcat user_role
				Role role = roleDao.findById(usuarioDB.getUsuario(), ROLE);
				roleDao.excluir(role.getUserName(), ROLE);
				role = new Role();
				role.setUserName(usuarioAtual.getUsuario());
				role.setRoleName(ROLE);
				roleDao.criar(role);
			} else {
				erro = true;
				erroMsg = "Usuário já existe!";
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
			// TODO distribuir responsabilidades
			Usuario usuarioDB = usuarioDao.findById(usuarioSelecionado.getId());
			if (usuarioSelecionado.getUsuario().equals(usuarioDB.getUsuario()) || userDao.findById(usuarioSelecionado.getUsuario()) == null) {
				// usuario
				//usuarioSelecionado.setRoles(rolesSelecionadas);
				usuarioDao.editar(usuarioSelecionado);
				
				// tomcat users
				User user = userDao.findById(usuarioDB.getUsuario());
				userDao.excluir(user);
				user = new User();
				user.setUserName(usuarioSelecionado.getUsuario());
				user.setUserPass(usuarioSelecionado.getSenha());
				userDao.criar(user);
				
				// tomcat user_role
				//Role role = roleDao.findById(usuarioDB.getUsuario(), ROLE);
				roleDao.excluir(usuarioSelecionado.getUsuario(), ROLE);
				for (String strRoles : roles) {
					roleDao.excluir(usuarioSelecionado.getUsuario(), getRoleName(strRoles));
				}
				Role role = new Role();
				role.setUserName(usuarioSelecionado.getUsuario());
				role.setRoleName(ROLE);
				roleDao.criar(role);
				for (String roleSel : usuarioSelecionado.getRoles()) {
					role.setRoleName(getRoleName(roleSel));
					roleDao.criar(role);
				}
			} else {
				erro = true;
				erroMsg = "Usuário já existe!";
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
			return "usuarios";
		}
	}
	
	public String criar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			// TODO distribuir responsabilidades
			if (userDao.findById(usuarioNovo.getUsuario()) == null) {
				// usuario
				usuarioDao.criar(usuarioNovo);
				
				// tomcat users
				User user = new User();
				user.setUserName(usuarioNovo.getUsuario());
				user.setUserPass(usuarioNovo.getSenha());
				userDao.criar(user);
				
				// tomcat user_role
				Role role = new Role();
				role.setUserName(usuarioNovo.getUsuario());
				role.setRoleName(ROLE);
				roleDao.criar(role);
				for (String roleCheck : usuarioNovo.getRoles()) {
					role.setRoleName(getRoleName(roleCheck));
					roleDao.criar(role);
				}
				
				// ajustes
				listaUsuarios.add(usuarioNovo);
				preparaNovoUsuario();
			} else {
				erro = true;
				erroMsg = "Usuário já existe!";
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
			return "usuarios";
		}
	}

	public String excluir() {
		boolean erro = false;
		String erroMsg = null;
		try {
			// TODO distribuir responsabilidades
			// usuario
			usuarioDao.excluir(usuarioSelecionado);
			
			// tomcat users
			User user = userDao.findById(usuarioSelecionado.getUsuario());
			userDao.excluir(user);
			
			// tomcat user_role
			// Role role = roleDao.findById(usuarioSelecionado.getUsuario(), ROLE);
			roleDao.excluir(usuarioSelecionado.getUsuario(), ROLE);
			for (String strRoles : roles) {
				roleDao.excluir(usuarioSelecionado.getUsuario(), getRoleName(strRoles));
			}
			
			// ajustes
			listaUsuarios.remove(usuarioSelecionado);
			usuarioSelecionado = new Usuario();
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

	public void setUsuario(Usuario usuarioAtual) {
		this.usuarioAtual = usuarioAtual;
	}
	
	public Usuario getUsuarioNovo() {
		return usuarioNovo;
	}

	public void setUsuarioNovo(Usuario usuarioNovo) {
		this.usuarioNovo = usuarioNovo;
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
