package br.com.sometal.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Thiago Carvalho
 */
public class MetricasDoProjeto{

	private String subDir;  
	private int somaLinhasJava = 0;
	private int somaClassesJava = 0;
	private int somaPaginasXHTML = 0;
	private int somaLinhasXHTML = 0;
	private int somaDiretorios = 0;
	private String out = "";

	public static void main(String[] args) {

		MetricasDoProjeto mp = new MetricasDoProjeto();
		String path = "C:\\Users\\tcarvalho\\git\\sga"; //JOptionPane.showInputDialog("Diretório do projeto: "); 
		mp.contador(path);

		// Apresenta os resultados no console
		System.err.println("\nSub-pastas do projeto = " + mp.getSomaDiretorios());
		System.err.println("Classes JAVA = " + mp.getSomaClassesJava());
		System.err.println("Linhas de código JAVA = " + mp.getSomaLinhasJava());
		System.err.println("Páginas XHTML = " + mp.getSomaPaginasXHTML());
		System.err.println("Linhas de páginas XHTML = " + mp.getSomaLinhasXHTML());

		// Adiciona os resultados obtidos ao atributo "out", o qual será gravado no arquivo
		mp.setOut("\n\n\n\n============================================="
				+ "\nSub-pastas do projeto = " + mp.getSomaDiretorios()
				+ "\nClasses JAVA = " + mp.getSomaClassesJava()
				+ "\nLinhas de código JAVA = " + mp.getSomaLinhasJava()
				+ "\nLinhas de código JAVA = " + mp.getSomaLinhasJava()
				+ "\nLinhas de código JAVA = " + mp.getSomaLinhasJava());
		
		mp.gravaArquivoTxt(path, mp);
	}

	public void gravaArquivoTxt (String path, MetricasDoProjeto mp){
		try{      
			// Grava no arquivo
			File file = new File(path + "/Metricas.txt");  
			FileWriter fw = new FileWriter(file, true );
			@SuppressWarnings("resource")
			PrintWriter ps = new PrintWriter(fw, true );  
			ps.println(mp.getOut());  
			//JOptionPane.showMessageDialog(null, "Um arquivo com os resultados foi gerado no diretório do seu projeto!", "Informação", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void contador(String path) {
		File diretorios = new File(path);  
		File[] files = diretorios.listFiles();

		/**
		 * É feita uma iteração com o vetor que armazenou o path dos arq. e
		 * dirs. do dir. recebido como parâmetro inicial
		 **/
		for (int i = 0; i < files.length; i++) {
			
			/**
			 * Verifica se o camniho da posição atual do vetor "files", é um
			 * arq. ou um dir., caso for um dir., ele entra no if
			 **/
			if (files[i].isDirectory()) {
				// Atribui o caminho do próximo diretório à variável "subDir"
				subDir = files[i].toString();
				setOut("\n\nPath = " + subDir);
				
				// Ignora os diretórios
				if (subDir.endsWith(".git") || subDir.endsWith(".settings")) {
					continue;
				}
				
				System.out.println("\nPath = " + subDir);
				
				// Incrementa o atributo "SomaDiretorios"
				setSomaDiretorios(1);

				try {
					/**
					 * Carrega em memória todos os "paths" de dirs. e arqs. que
					 * estão dentro do path armazenado em "subDir"
					 **/
					File dir = new File(subDir);
					File[] file = dir.listFiles();

					// Faz uma iteração com o vetor "file"
					for (int j = 0; j < file.length; j++) {

						// Carrega em memória o path da posição atual do vetor "file"
						File f = new File(file[j].getPath());

						/**
						 * Verifica se a posição atual do vetor "file" é um dir.
						 * caso não, ele entra
						 **/
						// JAVA
						if (!(file[j].isDirectory()) && file[j].getPath().endsWith(".java")){

							// Armazena em buffer todo conteúdo do arquivo
							@SuppressWarnings("resource")
							BufferedReader arquivo = new BufferedReader(new FileReader(f));  
							int somaAux = 0;

							// Enquanto não for final do arquivo ele persiste no laço
							while((arquivo.readLine()) != null){  
								// A cada iteração é correspondido a uma linha nova, assim o atributo "somaLinhas" é incrementado **/
								setSomaLinhasJava(1);
								somaAux++;
							}  

							int lastSlashIndex = file[j].getPath().lastIndexOf('/');

							// Mostra na tela o arquivo + sua quantidade de linhas				
							System.err.println(file[j].getPath().substring(lastSlashIndex + 1) + " = " + somaAux + " linhas");
							setOut("\n\t " + file[j].getPath().substring(lastSlashIndex + 1) + " = " + somaAux + " linhas");
							// O atributo "somaClasses" é incrementado à cada arquivo lido 
							setSomaClassesJava(1);
						}
						// XHTML
						if (!(file[j].isDirectory()) && file[j].getPath().endsWith(".xhtml")){

							// Armazena em buffer todo conteúdo do arquivo
							@SuppressWarnings("resource")
							BufferedReader arquivo = new BufferedReader(new FileReader(f));  
							int somaAux = 0;

							// Enquanto não for final do arquivo ele persiste no laço
							while((arquivo.readLine()) != null){  
								// A cada iteração é correspondido a uma linha nova, assim o atributo "somaLinhas" é incrementado **/
								setSomaLinhasXHTML(1);
								somaAux++;
							}  

							int lastSlashIndex = file[j].getPath().lastIndexOf('/');

							// Mostra na tela o arquivo + sua quantidade de linhas				
							System.err.println(file[j].getPath().substring(lastSlashIndex + 1) + " = " + somaAux + " linhas");
							setOut("\n\t " + file[j].getPath().substring(lastSlashIndex + 1) + " = " + somaAux + " linhas");
							// O atributo "somaClasses" é incrementado à cada arquivo lido 
							setSomaPaginasXHTML(1);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				/**
				 *  Chama recursivamente a função, só que agora com o próximo nível de diretório, 
				 *  assim consecutivamente até que o último subdiretório seja encontrado.
				 */
				contador(subDir);
			}	
		}
	}

	public void setSomaLinhasJava(int somaLinhasJava) {
		this.somaLinhasJava = this.somaLinhasJava + somaLinhasJava;
	}

	public int getSomaLinhasJava() {
		return this.somaLinhasJava;
	}

	public void setSomaClassesJava(int somaClassesJava) {
		this.somaClassesJava = this.somaClassesJava + somaClassesJava;
	}

	public int getSomaClassesJava() {
		return this.somaClassesJava;
	}
	
	public void setSomaLinhasXHTML(int somaLinhasXHTML) {
		this.somaLinhasXHTML = this.somaLinhasXHTML + somaLinhasXHTML;
	}

	public int getSomaLinhasXHTML() {
		return this.somaLinhasXHTML;
	}

	public void setSomaPaginasXHTML(int somaPaginasXHTML) {
		this.somaPaginasXHTML = this.somaPaginasXHTML + somaPaginasXHTML;
	}

	public int getSomaPaginasXHTML() {
		return this.somaPaginasXHTML;
	}

	public void setSomaDiretorios(int somaDiretorios) {
		this.somaDiretorios = this.somaDiretorios + somaDiretorios;
	}

	public int getSomaDiretorios() {
		return this.somaDiretorios;
	}

	public void setOut(String out) {
		this.out = this.out + out;
	}

	public String getOut() {
		return this.out;
	}
}
