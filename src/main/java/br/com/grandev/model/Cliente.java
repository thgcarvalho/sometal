package br.com.grandev.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Thiago Carvalho
 * 
 */
@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nome;
	private String cadastro;
	private String endereco;
	private String numero;
	private String complemento;
	private String bairro;
	private String cidade;
	private String cep;
	private String fone1;
	private String fone2;
	private String email;
	private String obs;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCadastro() {
		return cadastro;
	}

	public void setCadastro(String cadastro) {
		if (cadastro != null) {
			this.cadastro = cadastro;
		}
	}
	
	/*
	 * getters e setters cpf e cnpj auxiliam para inserir na colula (cadastro)
	 * no db somentente o campo preenchido e permite a separacao desse campo
	 * quando se obtem o (cadastro) no db
	 */
	public String getCpf() {
		if (getIsPF()) {
			return getCadastro();
		}
		return "";
	}

	public void setCpf(String cpf) {
		if (getCnpj().equals("")) {
			setCadastro(cpf);
		}
	}

	public String getCnpj() {
		if (getIsPJ()) {
			return getCadastro();
		}
		return "";
	}

	public void setCnpj(String cnpj) {
		if (getCpf().equals("")) {
			setCadastro(cnpj);
		}
	}

	public boolean getIsPF() {
		if (getCadastro() != null && getCadastro().length() == 14) {
			return true;
		}
		return false;
	}

	public boolean getIsPJ() {
		if (getCadastro() != null && getCadastro().length() == 18) {
			return true;
		}
		return false;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getFone1() {
		return fone1;
	}

	public void setFone1(String fone1) {
		this.fone1 = fone1;
	}

	public String getFone2() {
		return fone2;
	}

	public void setFone2(String fone2) {
		this.fone2 = fone2;
	}

	public String getFones() {
		String fones = "";
		if (fone1 != null && !fone1.equals("")) {
			fones += fone1;
			if (fone2 != null && !fone2.equals("")) {
				fones += " / ";
				fones += fone2;
			}
		} else if (fone2 != null && !fone2.equals("")) {
			fones += fone2;
		}
		return fones;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

}
