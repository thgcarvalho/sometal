package br.com.grandev.acesso.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.grandev.acesso.dao.InnerDao;
import br.com.grandev.acesso.model.Inner;
import br.com.grandev.acesso.model.Status;
import br.com.grandev.acesso.service.ClientSocket;

/**
 * @author Thiago Carvalho
 * 
 */
public class ControleDeRegistros {

	private static final String LOGNAME = "ControleDeRegistros";
	private List<Inner> inners;
	private List<Inner> innersPendentes;
	private InnerDao innerDao;
	
	public ControleDeRegistros(InnerDao innerDao) {
		printSM();
		this.innerDao = innerDao;
		this.inners = coletar();
		this.innersPendentes = identificarPendentes();
	}
	
	private List<Inner> coletar() {
		DisplayMessage.display(LOGNAME, "Iniciando a coleta de dados...");
		return this.innerDao.getAll();
	
	}
	
	private List<Inner> identificarPendentes() {
		List<Inner> pendentes = new ArrayList<Inner>();
		for (Inner inner : this.inners) {
			if (inner.getStatus() != Status.ENVIADO.getCodigo()) {
				pendentes.add(inner);
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
		
		for (Inner inner : this.innersPendentes) {
			origem = String.valueOf(inner.getNumInner());
			tipo = inner.getTipo();
			data = Integer.parseInt(YMD.format(inner.getData()));
			hora = Integer.parseInt(HMS.format(inner.getData()));;
			codigo = Integer.parseInt(inner.getCartao());
			
			status = cs.sendData(origem, tipo, data, hora, codigo);
			inner.setStatus(status.getCodigo());
			innerDao.update(inner);
		}
		printGD();
	}

	public List<Inner> getInners() {
		return inners;
	}
	
	public void setInners(List<Inner> inners) {
		this.inners = inners;
	}
	
	public List<Inner> getInnersPendentes() {
		return innersPendentes;
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
