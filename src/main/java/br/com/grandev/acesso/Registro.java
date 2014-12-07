package br.com.grandev.acesso;

import java.io.Serializable;

import br.com.grandev.acesso.ControleDeRegistros.Status;

/**
 * @author Thiago Carvalho
 * 
 */
public class Registro implements Serializable {

	private static final long serialVersionUID = 1L;

	private int codigo;
	private int data;
	private int hora;
	private String tipo;
	private String origen;
	private Status status;

	public Registro() {
	}

	public Registro(int codigo, int data, int hora, String tipo, String origen) {
		this.codigo = codigo;
		this.data = data;
		this.hora = hora;
		this.tipo = tipo;
		this.origen = origen;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public int getHora() {
		return hora;
	}

	public void setHora(int hora) {
		this.hora = hora;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Registro other = (Registro) obj;
		if (codigo != other.codigo) {
			return false;
		}
		if (data != other.data) {
			return false;
		}
		if (hora != other.hora) {
			return false;
		}
		if (origen == null) {
			if (other.origen != null) {
				return false;
			}
		} else if (!origen.equals(other.origen)) {
			return false;
		}
		if (tipo == null) {
			if (other.tipo != null) {
				return false;
			}
		} else if (!tipo.equals(other.tipo)) {
			return false;
		}
		return true;
	}

	public String toString() {
		return "Registro [codigo=" + codigo + ", data=" + data
				+ ", hora=" + hora + ", tipo=" + tipo 
				+ ", origen=" + origen + "]";
	}

}
