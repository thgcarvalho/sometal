package br.com.grandev.acesso;

import java.util.ArrayList;
import java.util.List;

public class ControleDeRegistros {

	private List<Registro> registrosDasCatracas = new ArrayList<Registro>();
	private List<Registro> registrosGerenciados = new ArrayList<Registro>();
	private List<Registro> registrosNaoEnviados = new ArrayList<Registro>();
	
	public ControleDeRegistros() {
		Display.print("------------------------------------");
	}
	
	public void gerenciarRegistros() {
		coletar();
		analizar();
		enviar();	
	}
	
	private void coletar() {
		Display.print("Iniciando a coleta de dados...");
		// TODO coletar registros das catracas
		// TODO coletar registros gerenciados
	}
	
	private void analizar() {
		boolean enviado = false;
		Display.print("Analizando dados...");
		Display.print(registrosDasCatracas.size()  + " registros coletados");
		Display.print(registrosGerenciados.size()  + " registros gerenciados");
		if (registrosDasCatracas.size() == registrosGerenciados.size()) {
			Display.print("Não houve alterações!");
		}
		
		for (Registro catraca : registrosDasCatracas) {
			enviado = false;
			for (Registro gerenciado : registrosGerenciados) {
				if (catraca.equals(gerenciado)) {
					enviado = true;
					break;
				}
			}
			if (!enviado) {
				registrosNaoEnviados.add(catraca);
				Display.print("Registro novo = " + catraca);
			}
		}
	}
	
	private void enviar() {
		Display.print("Enviando dados...");
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
}
