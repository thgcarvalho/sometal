package br.com.sometal.util;

/**
 * @author Thiago Carvalho
 * 
 */
public class Paths {

	public static String DOMAIN = "http://www.grandev.com.br/";
	public static String TMP_PATH = "/home/grandev/temp/";
	public static String PUBLIC_PATH = "/home/grandev/public_html/";
	public static String APP_DIR = "sometal/";
	public boolean test = false;
	
	public Paths() {
		if (test) {
			DOMAIN = "http://www.grandev.com.br/";
			TMP_PATH = "/home/grandev/temp/";
			PUBLIC_PATH = "/home/grandev/public_html/";
			APP_DIR = "sometal/";
		}
	}
	
}
