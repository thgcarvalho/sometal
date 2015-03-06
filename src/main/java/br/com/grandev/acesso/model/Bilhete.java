package br.com.grandev.acesso.model;

import java.util.Date;

public class Bilhete {

	private String origem;
	private String tipo;
	private Date data;
	private Date hora;
	private int codigo;
	private int status;
	
	public Bilhete() {}
	
	public Bilhete(String origem, String tipo, Date data, Date hora, int codigo, int status) {
		this.origem = origem;
		this.tipo = tipo;
		this.data = data;
		this.hora = hora;
		this.codigo = codigo;
		this.status = status;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
		this.hora = hora;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
