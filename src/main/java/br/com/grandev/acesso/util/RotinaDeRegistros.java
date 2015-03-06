package br.com.grandev.acesso.util;

import java.util.Timer;
import java.util.TimerTask;

import br.com.grandev.acesso.dao.BilheteDao;

public class RotinaDeRegistros {

	private static final long TEMPO = (1000 * 10);
	
	public static void main(String[] args) {
		Timer timer = null;
		if (timer == null) {
			timer = new Timer();
			TimerTask tarefa = new TimerTask() {
				public void run() {
					try {
						ControleDeRegistros cdr = new ControleDeRegistros(new BilheteDao());
						cdr.enviar();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			timer.scheduleAtFixedRate(tarefa, TEMPO, TEMPO);
		}
	}
}
