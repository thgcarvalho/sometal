package br.com.grandev.acesso.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import br.com.grandev.acesso.model.Status;
import br.com.grandev.acesso.util.DisplayMessage;

public class ClientSocket {

	private static final String LOGNAME = "ClientSocket";
    private static boolean blDebug = true;
    public static final String NETWORK_UNREACHABLE = "Falha na conexão!";
    public static final String HTTP_200 = "Sucesso";
    public static final String HTTP_500 = "Erro interno do servidor";
    public static final String HTTP_501 = "Invalid syntax"; 
    public static final String HTTP_502 = "Invalid parameter"; 
    private String strUId = "Empresa";
    
    public Status sendData(String origem, String tipo, int data, int hora, int codigo) {
        String heloResp;
        String sendResp;
        BufferedReader br;
        PrintWriter pw;
        Socket socket;
        Status status = Status.NAOENVIADO;
        boolean OK_200 = false;

        try {
            if (blDebug) {
                DisplayMessage.display(LOGNAME, "sendData");
            }

            socket = new Socket();
            socket.connect(new InetSocketAddress(Service.IP, Service.PORT), 10 * 1000);

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream());

            pw.write("HELO " + strUId + " " + InetAddress.getLocalHost().getHostName() + "\n");
            pw.flush();
            
            while ((heloResp = br.readLine()) != null) {
    		    if (blDebug) {
                    DisplayMessage.display(LOGNAME, "heloResp=" + heloResp);
                }
    			if (heloResp.substring(0, 3).equals("200")) {
    				heloResp  = HTTP_200;
    				OK_200 = true;
    				break;
    			}
    			if (heloResp.substring(0, 3).equals("500")) {
    				heloResp = HTTP_500;
    				break;
    			}
    			if (heloResp.substring(0, 3).equals("501")) {
    				heloResp = HTTP_501;
    				break;
    			}
    			if (heloResp.substring(0, 3).equals("502")) {
    				heloResp = HTTP_502;
    				break;
    			}
    		}
            
            if (OK_200) {
	            pw.write("SAVEDATA " + origem + " " + tipo + " " + data + " " + hora + " " + codigo + "\n");
	            pw.flush();
	            
	            while ((sendResp = br.readLine()) != null) {
	    		    if (blDebug) {
	                    DisplayMessage.display(LOGNAME, "sendResp=" + sendResp);
	                }
	    			if (sendResp.length() < 4) {
	    				break;
	    			}
	    			if (sendResp.substring(3, 4).equals(" ")) {
	    				break;
	    			}
	    			if (sendResp.substring(0, 3).equals("200")) {
	    				sendResp  = HTTP_200;
	    				status = Status.ENVIADO;
	    				break;
	    			}
	    			if (sendResp.substring(0, 3).equals("500")) {
	    				sendResp = HTTP_500;
	    				status = Status.FALHA;
	    				break;
	    			}
	    		}
	            
	            if (blDebug) {
	            	DisplayMessage.display(LOGNAME, "sendResp/2=" + sendResp);
	            }
            } else {
            	status = Status.FALHA;
            }
            socket.close();
        } catch (IOException oExcp) {
        	DisplayMessage.display(LOGNAME, "Exception..." + oExcp.getMessage());
        	DisplayMessage.display(LOGNAME, NETWORK_UNREACHABLE);
            status = Status.FALHA;
        }
        return status;
    }

}