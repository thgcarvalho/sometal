package br.com.grandev.acesso.model;

public enum Status {

	NAOENVIADO(0), ENVIADO(1), FALHA(2);
	int status;

	private Status(int status) {
		this.status = status;
	}

	public int getCodigo() {
		return status;
	}
}
