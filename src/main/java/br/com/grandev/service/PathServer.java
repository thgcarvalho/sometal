package br.com.grandev.service;

public class PathServer {
	
	public static String PATH_DIR = "/home/sometal";
	public static String DIR_FOTOS = "fotos";
	private boolean test = false;
	
	public PathServer() {
		if (test) {
			PATH_DIR = "files\\sometal";
			DIR_FOTOS = "FOTOS";
		}
	}
	
}
