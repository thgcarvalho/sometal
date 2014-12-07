package br.com.grandev.acesso;

import java.util.Date;

public class Inner {

	private int numInner;
	private String tipo;
	private Date data;
	private String cartao;
	private int status;
	
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
