package br.com.grandev.acesso.model;

import java.util.Date;

public class Inner {

	private int numInner;
	private String tipo;
	private Date data;
	private String cartao;
	private int status;
	
	public Inner() {}
	
	public Inner(int numInner, String tipo, Date data, String cartao, int status) {
		this.numInner = numInner;
		this.tipo = tipo;
		this.data = data;
		this.cartao = cartao;
		this.status = status;
	}
	
	public int getNumInner() {
		return numInner;
	}
	public void setNumInner(int numInner) {
		this.numInner = numInner;
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
	public String getCartao() {
		return cartao;
	}
	public void setCartao(String cartao) {
		this.cartao = cartao;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
