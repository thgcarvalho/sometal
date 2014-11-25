package br.com.grandev.acesso;

import java.util.ArrayList;
import java.util.List;

public class ControleDeRegistros {

	private static final String LOGNAME = "ControleDeRegistros";
	private List<Registro> registrosDasCatracas = new ArrayList<Registro>();
	private List<Registro> registrosGerenciados = new ArrayList<Registro>();
	private List<Registro> registrosNaoEnviados = new ArrayList<Registro>();
	
	public enum Status {
		NAOENVIADO(0), ENVIADO(1), FALHA(2);
		int status;
		private Status(int status) {
			this.status = status;
		}
		public int getNome() {
			return status;
		}
	}
	
	public ControleDeRegistros() {
		DisplayMessage.display(LOGNAME, "------------------------------------");
	}
	
	public void gerenciarRegistros() {
		coletar();
		analizar();
		enviar();	
	}
	
	private void coletar() {
		DisplayMessage.display(LOGNAME, "Iniciando a coleta de dados...");
		// TODO coletar registros das catracas
		// TODO coletar registros gerenciados
	}
	
	public void analizar() {
		boolean enviado = false;
		DisplayMessage.display(LOGNAME, "Analizando dados...");
		DisplayMessage.display(LOGNAME, registrosDasCatracas.size()  + " registros coletados");
		DisplayMessage.display(LOGNAME, registrosGerenciados.size()  + " registros gerenciados");
		if (registrosDasCatracas.size() == registrosGerenciados.size()) {
			DisplayMessage.display(LOGNAME, "Não houve alterações!");
		}
		
		for (Registro catraca : registrosDasCatracas) {
			enviado = false;
			for (Registro gerenciado : registrosGerenciados) {
				if (gerenciado.getStatus() != null && gerenciado.getStatus().equals(Status.FALHA)) {
					continue;
				}
				if (catraca.equals(gerenciado)) {
					enviado = true;
					break;
				}
			}
			if (!enviado) {
				registrosNaoEnviados.add(catraca);
				DisplayMessage.display(LOGNAME, "Registro pendente --> " + catraca);
			}
		}
	}
	
	public void enviar() {
		DisplayMessage.display(LOGNAME, "Enviando dados...");
		for (Registro pendente : registrosNaoEnviados) {
			pendente.setStatus(Status.ENVIADO);
		}
	}

	public List<Registro> getRegistrosDasCatracas() {
		return registrosDasCatracas;
	}

	public void setRegistrosDasCatracas(List<Registro> registrosDasCatracas) {
		this.registrosDasCatracas = registrosDasCatracas;
	}

	public List<Registro> getRegistrosGerenciados() {
		return registrosGerenciados;
	}

	public void setRegistrosGerenciados(List<Registro> registrosGerenciados) {
		this.registrosGerenciados = registrosGerenciados;
	}
	
	public List<Registro> getRegistrosNaoEnviados() {
		return registrosNaoEnviados;
	}
	
	public void setRegistrosNaoEnviados(List<Registro> registrosNaoEnviados) {
		this.registrosNaoEnviados = registrosNaoEnviados;
	}
	
	public Status[] getStatus() {
		return Status.values();
	}
}
