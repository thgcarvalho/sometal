package br.com.grandev.acesso;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Thiago Carvalho
 * 
 */
public class Registro implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long codigo;
	private Date data;
	private Date hora;
	private String tipo;
	private String origen;

	public Registro() {
	}

	public Registro(Long codigo, Date data, Date hora, String tipo,
			String origen) {
		this.codigo = codigo;
		this.data = data;
		this.hora = hora;
		this.tipo = tipo;
		this.origen = origen;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Date getHora() {
		return hora;
	}

	public void setHora(Date hora) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		if (hora == null) {
			if (other.hora != null) {
				return false;
			}
		} else if (!hora.equals(other.hora)) {
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
		SimpleDateFormat sdfHM = new SimpleDateFormat("HH:mm");
		SimpleDateFormat sdfDMY = new SimpleDateFormat("dd/MM/YYYY");
		return "Registro [codigo=" + codigo + ", data=" + (data == null ? "null" : sdfDMY.format(data))
				+ ", hora=" + (hora == null ? "null" : sdfHM.format(hora)) + ", tipo=" + tipo 
				+ ", origen=" + origen + "]";
	}
	

}
