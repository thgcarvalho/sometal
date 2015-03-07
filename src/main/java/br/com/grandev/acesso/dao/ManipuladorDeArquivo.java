package br.com.grandev.acesso.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.grandev.acesso.model.Bilhete;

public class ManipuladorDeArquivo {

	private static final File BILHETES = new File("C:\\Program Files (x86)\\Gerenciador de Inners 5\\bilhetes");
	//private static File fileToWrite = new File("C:\\Program Files (x86)\\Gerenciador de Inners 5\\bilhetes_enviados");

	public List<Bilhete> read() throws IOException, ParseException {
		SimpleDateFormat DMY= new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat HM = new SimpleDateFormat("HH:mm");
		FileReader fileReader = new FileReader(BILHETES);
		BufferedReader reader = new BufferedReader(fileReader);
		String data = null;
		String[] cols = null;
		List<Bilhete> bilhetes = new ArrayList<Bilhete>();
		Bilhete bilhete = null;
		while ((data = reader.readLine()) != null) {
			cols = data.split(" ");
			bilhete = new Bilhete();
			bilhete.setOrigem("SOMETAL");
			bilhete.setTipo(cols[0]);
			bilhete.setData(DMY.parse(cols[1]));
			bilhete.setHora(HM.parse(cols[2]));
			bilhete.setCodigo(Integer.parseInt(cols[3]));
//			System.out.println(cols[0]);
//			System.out.println(cols[1]);
//			System.out.println(cols[2]);
//			System.out.println(cols[3]);
//			System.out.println(cols[4]);
//			System.out.println();
			System.out.println(data);
			bilhetes.add(bilhete);
		}
		fileReader.close();
		reader.close();
		return bilhetes;
	}
	
	public void clean() throws IOException {
		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(BILHETES));
		buffWrite.append("");
		buffWrite.close();
	}
	
//	public static void write(String path) throws IOException {
//		File file = new File(path);
//		long begin = System.currentTimeMillis();
//		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//		writer.write("Arquivo gravado em : " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(new Date()));
//		writer.newLine();
//		writer.write("Caminho da gravação: " + path);
//		writer.newLine();
//		long end = System.currentTimeMillis();
//		writer.write("Tempo de gravação: " + (end - begin) + "ms.");
//		// Criando o conteúdo do arquivo
//		writer.flush();
//		// Fechando conexão e escrita do arquivo.
//		writer.close();
//
//		System.out.println("Arquivo gravado em: " + path);
//	}
//
//	public static void leitor(String path) throws IOException {
//		BufferedReader buffRead = new BufferedReader(new FileReader(path));
//		String linha = "";
//		while (true) {
//			if (linha != null) {
//				System.out.println(linha);
//			} else {
//				break;
//			}
//			linha = buffRead.readLine();
//		}
//		buffRead.close();
//	}
//
//	public static void escritor(String path) throws IOException {
//		BufferedWriter buffWrite = new BufferedWriter(new FileWriter(path));
//		String linha = "";
//		Scanner in = new Scanner(System.in);
//		System.out.println("Escreva algo: ");
//		linha = in.nextLine();
//		buffWrite.append(linha + "\n");
//		buffWrite.close();
//	}
}