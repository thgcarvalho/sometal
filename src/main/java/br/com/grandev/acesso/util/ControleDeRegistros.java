package br.com.grandev.acesso.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.grandev.acesso.dao.BilheteDao;
import br.com.grandev.acesso.model.Bilhete;
import br.com.grandev.acesso.model.Status;
import br.com.grandev.acesso.service.ClientSocket;

/**
 * @author Thiago Carvalho
 * 
 */
public class ControleDeRegistros {

	private static final String LOGNAME = "ControleDeRegistros";
	private List<Bilhete> bilhetes;
	private List<Bilhete> bilhetesPendentes;
	private BilheteDao bilheteDao;
	
	public ControleDeRegistros(BilheteDao bilheteDao) {
		printSM();
		this.bilheteDao = bilheteDao;
		this.bilhetes = coletar();
		this.bilhetesPendentes = identificarPendentes();
	}
	
	private List<Bilhete> coletar() {
		DisplayMessage.display(LOGNAME, "Iniciando a coleta de dados...");
		return this.bilheteDao.getAll();
	
	}
	
	private List<Bilhete> identificarPendentes() {
		List<Bilhete> pendentes = new ArrayList<Bilhete>();
		for (Bilhete bilhete : this.bilhetes) {
			if (bilhete.getStatus() != Status.ENVIADO.getCodigo()) {
				pendentes.add(bilhete);
			}
		}
		return pendentes;
	}
	
	public void enviar() {
		DisplayMessage.display(LOGNAME, "Enviando dados...");
		ClientSocket cs = new ClientSocket();
		String origem;
		String tipo;
		int data;
		int hora;
		int codigo;
		Status status = null;
		SimpleDateFormat YMD = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat HMS = new SimpleDateFormat("HHmmss");
		
		for (Bilhete bilhete : this.bilhetesPendentes) {
			origem = bilhete.getOrigem();
			tipo = bilhete.getTipo();
			data = Integer.parseInt(YMD.format(bilhete.getData()));
			hora = Integer.parseInt(HMS.format(bilhete.getData()));;
			codigo = bilhete.getCodigo();
			
			status = cs.sendData(origem, tipo, data, hora, codigo);
			bilhete.setStatus(status.getCodigo());
			bilheteDao.update(bilhete);
		}
		printGD();
	}

	public List<Bilhete> getBilhetes() {
		return bilhetes;
	}
	
	public void setBilhetes(List<Bilhete> bilhetes) {
		this.bilhetes = bilhetes;
	}
	
	public List<Bilhete> getBilhetesPendentes() {
		return bilhetesPendentes;
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
