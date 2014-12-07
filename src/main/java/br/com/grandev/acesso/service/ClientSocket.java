package br.com.grandev.acesso.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import br.com.grandev.acesso.DisplayMessage;
import br.com.grandev.acesso.Status;

public class ClientSocket {

	private static final String LOGNAME = "ClientSocket";
    private static boolean blDebug = true;
    public static final String NETWORK_UNREACHABLE = "Falha na conexão!";
    public static final String HTTP_200 = "Sucesso";
    public static final String HTTP_500 = "Erro interno do servidor";
    private String strUId = "Empresa";
    
    public Status sendData(String origem, String tipo, int data, int hora, int codigo) {
        String strResp;
        BufferedReader br;
        PrintWriter pw;
        Socket socket;
        Status status = Status.NAOENVIADO;

        try {
            if (blDebug) {
                DisplayMessage.display(LOGNAME, "SENDDATA");
            }

            socket = new Socket();
            socket.connect(new InetSocketAddress(Service.IP, Service.PORT), 10 * 1000);

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(socket.getOutputStream());

            pw.write("HELO " + strUId + " " + InetAddress.getLocalHost().getHostName() + "\n");
            pw.flush();
            
            pw.write("SENDDATA " + origem + " " + tipo + " " + data + " " + hora + " " + codigo + "\n");
            pw.flush();
            
            while ((strResp = br.readLine()) != null) {
    		    if (blDebug) {
                    DisplayMessage.display(LOGNAME, "WHILE=" + strResp);
                }
    			if (strResp.length() < 4) {
    				break;
    			}
    			if (strResp.substring(3, 4).equals(" ")) {
    				break;
    			}
    			if (strResp.substring(0, 3).equals("200")) {
    				strResp  = HTTP_200;
    				status = Status.ENVIADO;
    				break;
    			}
    			if (strResp.length() > 3 && strResp.substring(0, 3).equals("500")) {
    				strResp = HTTP_500;
    				break;
    			}
    		}
            
            if (blDebug) {
            	DisplayMessage.display(LOGNAME, "SENDDATA/Response=" + strResp);
            }
            socket.close();
        } catch (IOException oExcp) {
        	DisplayMessage.display(LOGNAME, "Exception..." + oExcp.getMessage());
            strResp = NETWORK_UNREACHABLE ;
        }
        return status;
    }

}