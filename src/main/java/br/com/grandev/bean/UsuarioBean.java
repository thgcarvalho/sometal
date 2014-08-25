package br.com.grandev.bean;

import static br.com.grandev.model.Auth.ROLE;
import static br.com.grandev.model.Auth.ROLE_ADMIN_DESC;
import static br.com.grandev.model.Auth.ROLE_ENCRR_DESC;
import static br.com.grandev.model.Auth.ROLE_ESCRT_DESC;
import static br.com.grandev.util.FacesUtils.MSG_ERROR;
import static br.com.grandev.util.FacesUtils.MSG_SUCESS;
import static br.com.grandev.util.FacesUtils.addErrorMessage;
import static br.com.grandev.util.FacesUtils.addInfoMessage;

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

import br.com.grandev.dao.RoleDao;
import br.com.grandev.dao.UserDao;
import br.com.grandev.dao.UsuarioDao;
import br.com.grandev.daoImp.RoleDaoImp;
import br.com.grandev.daoImp.UserDaoImp;
import br.com.grandev.daoImp.UsuarioDaoImp;
import br.com.grandev.model.Auth;
import br.com.grandev.model.Role;
import br.com.grandev.model.User;
import br.com.grandev.model.Usuario;

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
		roles.add(ROLE_ESCRT_DESC);
		
		try {
			userName = fc.getExternalContext().getUserPrincipal().getName();
			// usuario atual
			usuarioAtual = usuarioDao.findByUserName(userName);
			usuarioAtual.setRoles(getRolesDoUsuario(userName));
			usuarioAtual.setSenha(getSenhaDoUsuario(userName));
			session.setAttribute("user", usuarioAtual);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Date data = GregorianCalendar.getInstance().getTime();
		System.out.println("\nLOGIN:" + this.usuarioAtual + " " + this.usuarioAtual.getRoles() + " " + sdf.format(data));
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
		
		for (Usuario usuario : listaUsuarios) {
			usuario.setRoles(getRolesDoUsuario(usuario.getUsuario()));
			usuario.setSenha(getSenhaDoUsuario(usuario.getUsuario()));
		}
		filteredUsuarios = listaUsuarios;
	}
	
	private String getSenhaDoUsuario(String usuario) {
		User user = null;
		String senha = null;
		try {
			user = userDao.findById(usuario);
			senha = user.getUserPass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return senha;
	}
	
	private List<String> getRolesDoUsuario(String usuario) {
		System.out.print("USUÁRIO:" + usuario + " ");
		List<Role> rolesDoUsuario;
		List<String> rolesDoUsuarioStr;
		rolesDoUsuario = roleDao.findByUserName(usuario);
		rolesDoUsuarioStr = new ArrayList<String>();
		String strRole;
		for (Role roleDoUsuario : rolesDoUsuario) {
			strRole = Auth.getRoleDesc(roleDoUsuario.getRoleName());
			if (strRole != null) {
				rolesDoUsuarioStr.add(Auth.getRoleDesc(roleDoUsuario.getRoleName()));
			}
		}
		System.out.print(rolesDoUsuario);
		System.out.println();
		return rolesDoUsuarioStr;
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
			System.out.println("\nLOGOUT:" + this.usuarioAtual + " " + sdf.format(data));
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
			String strUsuario = usuarioAtual.getUsuario();
			if (strUsuario.equals(userName) || userDao.findById(strUsuario) == null) {
				// usuario
				Usuario usuarioDB = usuarioDao.findById(usuarioAtual.getId());
				usuarioDao.editar(usuarioAtual);
				
				// tomcat users
				User user = userDao.findById(usuarioDB.getUsuario());
				userDao.excluir(user);
				user = new User();
				user.setUserName(strUsuario);
				user.setUserPass(usuarioAtual.getSenha());
				userDao.criar(user);
				
				// tomcat user_role
				Role role = roleDao.findById(usuarioDB.getUsuario(), ROLE);
				roleDao.excluir(role.getUserName(), ROLE);
				for (String strRoles : roles) {
					roleDao.excluir(strUsuario, Auth.getRoleName(strRoles));
				}
				role = new Role();
				role.setUserName(strUsuario);
				role.setRoleName(ROLE);
				roleDao.criar(role);
				for (String roleSel : usuarioAtual.getRoles()) {
					role = new Role();
					role.setUserName(strUsuario);
					role.setRoleName(Auth.getRoleName(roleSel));
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
			return "usuario";
		}
	}
	
	public String editar() {
		boolean erro = false;
		String erroMsg = null;
		try {
			// TODO distribuir responsabilidades
			Usuario usuarioDB = usuarioDao.findById(usuarioSelecionado.getId());
			String strUsuario = usuarioSelecionado.getUsuario();
			if (strUsuario.equals(usuarioDB.getUsuario()) || userDao.findById(strUsuario) == null) {
				// usuario
				usuarioDao.editar(usuarioSelecionado);
				
				// tomcat users
				User user = userDao.findById(usuarioDB.getUsuario());
				userDao.excluir(user);
				user = new User();
				user.setUserName(strUsuario);
				user.setUserPass(usuarioSelecionado.getSenha());
				userDao.criar(user);
				
				// tomcat user_role
				Role role = roleDao.findById(usuarioDB.getUsuario(), ROLE); //TODO confimar tipos de user
				roleDao.excluir(strUsuario, ROLE);
				for (String strRoles : roles) {
					roleDao.excluir(strUsuario, Auth.getRoleName(strRoles));
				}
				role = new Role();
				role.setUserName(strUsuario);
				role.setRoleName(ROLE);
				roleDao.criar(role);
				for (String roleSel : usuarioSelecionado.getRoles()) {
					role = new Role();
					role.setUserName(strUsuario);
					role.setRoleName(Auth.getRoleName(roleSel));
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
			String strUsuario = usuarioNovo.getUsuario();
			if (userDao.findById(strUsuario) == null) {
				// usuario
				usuarioDao.criar(usuarioNovo);
				
				// tomcat users
				User user = new User();
				user.setUserName(strUsuario);
				user.setUserPass(usuarioNovo.getSenha());
				userDao.criar(user);
				
				// tomcat user_role
				Role role = new Role();
				role.setUserName(strUsuario);
				role.setRoleName(ROLE);
				roleDao.criar(role);
				for (String roleCheck : usuarioNovo.getRoles()) {
					role = new Role();
					role.setUserName(strUsuario);
					role.setRoleName(Auth.getRoleName(roleCheck));
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
				roleDao.excluir(usuarioSelecionado.getUsuario(), Auth.getRoleName(strRoles));
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
    
//	private String convertStringToMd5(String valor) {
//		MessageDigest mDigest;
//		try {
//			// strat HASH MD5.
//			mDigest = MessageDigest.getInstance("MD5");
//			// convert a string value to MD5 array of bytes
//			byte[] valorMD5 = mDigest.digest(valor.getBytes("UTF-8"));
//			// convert bytes to hexadecimal
//			StringBuffer sb = new StringBuffer();
//			for (byte b : valorMD5) {
//				sb.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
//			}
//			return sb.toString();
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//			return null;
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

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
