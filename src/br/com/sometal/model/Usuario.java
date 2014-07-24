package br.com.sometal.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Thiago Carvalho
 * 
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String usuario;
	
	private String nome;

	private String funcao;

	private String email;
	
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String login) {
		this.usuario = login;
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return usuario;
	}
}
