package br.com.sga.util;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author tcarvalho
 * 
 */
public class FacesUtils {
	
	public final static String MSG_SUCESS = "Operação efetuada com sucesso!";
	public final static String MSG_INFO = "Informação!";
	public final static String MSG_WARN = "Atenção!";
	public final static String MSG_ERROR = "Erro na operação!";
	public final static String MSG_NENHUM_REGISTRO = "Nenhum Registro foi localizado!";

	// INFO
	public static void addInfoMessage(String summary) {
		addInfoMessage(summary, "");
	}
	public static void addInfoMessage(String summary, String detail) {
		setMensage(summary, detail, FacesMessage.SEVERITY_INFO);
	}
	
	// WARN
	public static void addWarnMessage(String summary) {
		addWarnMessage(summary, "");
	}
	public static void addWarnMessage(String summary, String detail) {
		setMensage(summary, detail, FacesMessage.SEVERITY_WARN);
	}
	
	// ERROR
	public static void addErrorMessage(String summary) {
		addErrorMessage(summary, "");
	}
	public static void addErrorMessage(String summary, String detail) {
		setMensage(summary, detail, FacesMessage.SEVERITY_ERROR);
	}
	
	// Set Mensage
	
	public static void setMensage(String summary, String detail, FacesMessage.Severity severity) {
		System.out.println(summary +"+"+ detail);
		FacesMessage facesMsg = new FacesMessage(summary, detail);
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.getExternalContext().getFlash().setKeepMessages(true);
		fc.addMessage(null, facesMsg);
	}
	
	// Get Resource Message
	
	public String getMessage(String key) {
		try {
			FacesContext fc = FacesContext.getCurrentInstance();
			ResourceBundle rb = ResourceBundle.getBundle("messages", fc.getViewRoot().getLocale());
			String mensagem = rb.getString(key);
			return mensagem;
		} catch (Exception e) {
			return "";
		}
	}
	
	/*
	public static void addInfoMessage(String summary, String detail) {
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				summary, detail);
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, facesMsg);
	}
	
	public static String get(String param) {
		return (String) FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get(param);
	}
	*/
	
}
