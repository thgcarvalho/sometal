package br.com.sga.service;

import javax.swing.JOptionPane;

public class Backup {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			ProcessBuilder pb;
			@SuppressWarnings("unused")
			Process p;
			pb = new ProcessBuilder(
					"C:/Program Files/PostgreSQL/8.4/bin/pg_dump.exe ", "-i",
					"-h", "localhost", "-p", "5432", "-U", "seuusuario", "-F",
					"c", "-b", "-v", "-f",
					"C:\\Users\\tcarvalho\\Desktop\\TesteBKP.sql", "teste");
			pb.environment().put("PGPASSWORD", "suasenha");
			pb.redirectErrorStream(true);
			p = pb.start();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, ex);
		}

	}
}
