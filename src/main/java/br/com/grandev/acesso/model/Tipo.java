package br.com.grandev.acesso.model;

public enum Tipo {

	ENTRADA("01"), SAIDA("02");
	String tipo;

	private Tipo(String tipo) {
		this.tipo = tipo;
	}

	public String getCodigo() {
		return tipo;
	}
}
