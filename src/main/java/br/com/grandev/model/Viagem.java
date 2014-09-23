package br.com.grandev.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Thiago Carvalho
 * 
 */
@Entity
@Table(name = "abastecimentos")
public class Viagem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "veiculo_id", referencedColumnName = "id", insertable = true, updatable = true)
	private Veiculo veiculo;
	private Date data;
	private String motorista;
	private Date saida;
	@Column(name="km_saida")
	private BigDecimal kmSaida;
	private Date chegada;
	@Column(name="km_chegada")
	private BigDecimal kmChegada;
	private String destino;
	private String motivo;
	private String obs;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getMotorista() {
		return motorista;
	}
	public void setMotorista(String motorista) {
		this.motorista = motorista;
	}
	public Date getSaida() {
		return saida;
	}
	public void setSaida(Date saida) {
		this.saida = saida;
	}
	public BigDecimal getKmSaida() {
		return kmSaida;
	}
	public void setKmSaida(BigDecimal kmSaida) {
		this.kmSaida = kmSaida;
	}
	public Date getChegada() {
		return chegada;
	}
	public void setChegada(Date chegada) {
		this.chegada = chegada;
	}
	public BigDecimal getKmChegada() {
		return kmChegada;
	}
	public void setKmChegada(BigDecimal kmChegada) {
		this.kmChegada = kmChegada;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getMotivo() {
		return motivo;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	public String getObs() {
		return obs;
	}
	public void setObs(String obs) {
		this.obs = obs;
	}
}
