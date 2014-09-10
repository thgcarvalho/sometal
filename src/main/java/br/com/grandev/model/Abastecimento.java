package br.com.grandev.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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
@Table(name = "abastecimentos")
public class Abastecimento implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Date data;
	private String notaFiscal;
	private BigDecimal valor;
	private BigDecimal litros;
	private String combustível;
	private BigDecimal km;
	private String obs;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getNotaFiscal() {
		return notaFiscal;
	}
	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public BigDecimal getLitros() {
		return litros;
	}
	public void setLitros(BigDecimal litros) {
		this.litros = litros;
	}
	public String getCombustível() {
		return combustível;
	}
	public void setCombustível(String combustível) {
		this.combustível = combustível;
	}
	public BigDecimal getKm() {
		return km;
	}
	public void setKm(BigDecimal km) {
		this.km = km;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
	
}
