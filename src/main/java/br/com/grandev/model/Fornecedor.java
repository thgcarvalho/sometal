package br.com.grandev.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Thiago Carvalho
 * 
 */
@Entity
@Table(name = "fornecedores")
public class Fornecedor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String cadastro;
	private String fone1;
	private String fone2;
	private String email;
	private String endereco;
	private String numero;
	private String complemento;
	private String cep;
	private String bairro;
	private String cidade;
	private String banco;
	private String agencia;
	private String conta;
	private String tipo;
	private String obs;
	private String oferta;
	@Transient
	private List<String> ofertas = new ArrayList<String>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
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
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
	
	public String getCep() {
		return cep;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}
	
	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getOferta() {
		return oferta;
	}

	public void setOferta(String oferta) {
		this.oferta = oferta;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Fornecedor other = (Fornecedor) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
	public List<String> getOfertasDB() {
		List<String> ofertasDB = new ArrayList<String>();
		String[] arOrfertas = oferta.split("<br />");
		for (String oferta : arOrfertas) {
			ofertasDB.add(oferta);
		}
		return ofertasDB;
	}


	public List<String> getOfertas() {
		return ofertas;
	}

	public void setOfertas(List<String> ofertas) {
		this.ofertas = ofertas;
	}

}
