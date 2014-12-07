package br.com.grandev.acesso;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.grandev.acesso.dao.ConnectionFactory;
import br.com.grandev.acesso.service.ClientSocket;

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
	
	public static void main(String[] args) {
		ControleDeRegistros cdr = new ControleDeRegistros();
		cdr.enviar();
	}
	
	public ControleDeRegistros() {
		printSM();
	}
	
	public void gerenciarRegistros() {
		coletar();
		analizar();
		enviar();	
	}
	
	private void coletar() {
		DisplayMessage.display(LOGNAME, "Iniciando a coleta de dados...");
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("select NumInner, Tipo, Data, Cartao, Status"
					+ " from tb_INNER");
			rs = ps.executeQuery();
			while (rs.next()) {
				Inner inner = new Inner();
				inner.setNumInner(rs.getInt(1));
				inner.setTipo(rs.getString(2));
				inner.setData(rs.getDate(3));
				inner.setCartao(rs.getString(4));
				inner.setStatus(rs.getInt(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
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
		ClientSocket cs = new ClientSocket();
		DisplayMessage.display(LOGNAME, "Enviando dados...");
		for (Registro pendente : registrosNaoEnviados) {
			pendente.setStatus(Status.ENVIADO);
			cs.sendData(pendente.getData(), pendente.getHora(), pendente.getCodigo());
		}
		cs.sendData(112222, 1344, 1234);
		printGD();
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
	
	private void printSM() {
		System.out.println();
		System.out.println(" .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------.  .----------------. ");
		System.out.println(" | .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. || .--------------. |");
		System.out.println(" | |    _______   | || |     ____     | || | ____    ____ | || |  _________   | || |  _________   | || |      __      | || |   _____      | |");
		System.out.println(" | |   /  ___  |  | || |   .'    `.   | || ||_   \\  /   _|| || | |_   ___  |  | || | |  _   _  |  | || |     /  \\     | || |  |_   _|     | |");
		System.out.println(" | |  |  (__ \\_|  | || |  /  .--.  \\  | || |  |   \\/   |  | || |   | |_  \\_|  | || | |_/ | | \\_|  | || |    / /\\ \\    | || |    | |       | |");
		System.out.println(" | |   '.___`-.   | || |  | |    | |  | || |  | |\\  /| |  | || |   |  _|  _   | || |     | |      | || |   / ____ \\   | || |    | |   _   | |");
		System.out.println(" | |  |`\\____) |  | || |  \\  `--'  /  | || | _| |_\\/_| |_ | || |  _| |___/ |  | || |    _| |_     | || | _/ /    \\ \\_ | || |   _| |__/ |  | |");
		System.out.println(" | |  |_______.'  | || |   `.____.'   | || ||_____||_____|| || | |_________|  | || |   |_____|    | || ||____|  |____|| || |  |________|  | |");
		System.out.println(" | |              | || |              | || |              | || |              | || |              | || |              | || |              | |");
		System.out.println(" | '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' || '--------------' |");
		System.out.println("  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'  '----------------'"); 
		System.out.println();
		
	}
	
	private void printGD() {
		System.out.println();
		System.out.println("    _____                 _____        			");
		System.out.println("   / ____|               |  __ \\            	");
		System.out.println("  | |  __ _ __ __ _ _ __ | |  | | _____   __	");
		System.out.println("  | | |_ | '__/ _` | '_ \\| |  | |/ _ \\ \\ / /	");
		System.out.println("  | |__| | | | (_| | | | | |__| |  __/\\ V / 	");
		System.out.println("   \\_____|_|  \\__,_|_| |_|_____/ \\___| \\_/  ");
		System.out.println();
	}
}
