package br.com.grandev.model;

/**
 * @author Thiago Carvalho
 * 
 */
public class Status {

	public static final String STATUS_EXCLUIDO = "0";
	public static final String STATUS_ATIVO = "1";

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
