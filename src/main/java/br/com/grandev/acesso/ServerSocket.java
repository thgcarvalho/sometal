package br.com.grandev.acesso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.channels.IllegalBlockingModeException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class ServerSocket {
	private static final String LOGNAME = "ServerSocket";
	static private final int IPORT_PROD = 7788;
	static private final int IPORT_TEST = 7789;
	static private final int IBACKLOG = 100;
	static private final int ISTATE_CONNECTED = 0;
	static private final int ISTATE_IDENTIFIED = 1;
	static private java.net.ServerSocket oSSock = null;
	static private boolean blDebug = true;
	static private boolean blTest = false;
	static private SimpleDateFormat sdfDMY = null;
	static private SimpleDateFormat sdfHMS = null;

	public static void main(String[] cmdline) {
		Connection connSQL = null;
		sdfDMY = new SimpleDateFormat("dd/MM/YYYY");
		sdfDMY.setLenient(false);
		sdfHMS = new SimpleDateFormat("HH:mm:ss");
		sdfHMS.setLenient(false);

		System.out.println(sdfHMS.format(new Date()) + " Starting ServerSocket...");

		Runtime.getRuntime().addShutdownHook(new ShutdownHook());

		if (!getCmdLine(cmdline)){
			System.out.println(sdfHMS.format(new Date()) + " Parametri riga di comando invalidi. Startup continuera comunque");
		}

		System.out.println(sdfHMS.format(new Date()) + " Checking SQL connection...");
		Properties propDown = null;
		try {
			propDown = ReadProperties.load();
			System.out.println(sdfHMS.format(new Date()) + "   jdbc classname=" + propDown.getProperty(ReadProperties.WEB_JDBC));
			System.out.println(sdfHMS.format(new Date()) + "   jdbc connstr=" + propDown.getProperty(ReadProperties.WEB_CONNSTR));
			Class.forName(propDown.getProperty(ReadProperties.WEB_JDBC));
			connSQL = DriverManager.getConnection(propDown.getProperty(ReadProperties.WEB_CONNSTR),
					propDown.getProperty(ReadProperties.WEB_USER), propDown.getProperty(ReadProperties.WEB_PASSWORD));
			connSQL.close();
		} catch(ClassNotFoundException excp) {
			excp.printStackTrace();
			System.exit(8);
		} catch(IOException excp) {
			excp.printStackTrace();
			System.exit(8);
		} catch(SQLException excp) {
			excp.printStackTrace();
			System.exit(8);
		}

		System.out.print(sdfHMS.format(new Date()) + " Creating socket...");
		try {
			oSSock = new java.net.ServerSocket((blTest ? IPORT_TEST : IPORT_PROD), IBACKLOG, null);
		} catch(IOException oExcp) {
			System.out.println(sdfHMS.format(new Date()) + " Exception main - socket creation:");
			oExcp.printStackTrace();
			System.exit(8);
		}
		System.out.println("Done");

		Server oServer = new Server();
		Thread oThread = new Thread(oServer);
		oThread.start();
	}

	private static boolean getCmdLine(String[] args){
		String strArg = null;
		for(int i = 0; i < args.length; i++){
			strArg = args[i].trim().toLowerCase();
			if (strArg.equals("--debug")){
				blDebug = true;
				continue;
			}
			if (strArg.equals("--test")){
				blTest = true;
				continue;
			}
			System.out.println(sdfHMS.format(new Date()) + "Invalid parameter: " + args[i]);
			return(false);
		}
		return(true);
	}

	static public class Server implements Runnable {
		private Socket oSock = null;

		public void run(){
			// start listening
			try{
				while(true){
					if (blDebug){
						System.out.println(sdfHMS.format(new Date()) + " Entering accept");
					}

					oSock = oSSock.accept();

					if (blDebug){
						System.out.println(sdfHMS.format(new Date()) + " Connection accepted");
					}

					AndroidConnection oConn = new AndroidConnection(oSock);
					Thread oThread = new Thread(oConn);
					oThread.start();

				}  // while true
			} catch(SocketTimeoutException oExcp) {
				System.out.println(sdfHMS.format(new Date()) + " Exception Server.run:");
				oExcp.printStackTrace();
			} catch(SocketException oExcp) {
				if (oExcp.getMessage().equals("Socket closed")){
					System.out.println(sdfHMS.format(new Date()) + " Socket was closed - program will end");
				}else{
					System.out.println(sdfHMS.format(new Date()) + " Exception Server.run:" + oExcp.getMessage());
					oExcp.printStackTrace();
				}
			} catch(IllegalBlockingModeException oExcp) {
				System.out.println(sdfHMS.format(new Date()) + " Exception Server.run:");
				oExcp.printStackTrace();
			} catch(IOException oExcp) {
				System.out.println(sdfHMS.format(new Date()) + " Exception Server.run:");
				oExcp.printStackTrace();
			}
		}
	}

	static public class AndroidConnection implements Runnable {
		private Socket oSock = null;
		private PrintWriter pw = null;
		private BufferedReader br = null;
		private String strUId = null;
		private String strTermId = null;
		private InetAddress inetRemote = null;
		private int iRemotePort = 0;
		private int iProtState = ISTATE_CONNECTED;
		private final String IDENTIFIER_SAVEDATA = "SAVEDATA";
		private String strDtId = null;
		private String strHrId = null;
		private Connection connSQL = null;

		public AndroidConnection(Socket inpSock){
			oSock = inpSock;
			inetRemote = oSock.getInetAddress();
			iRemotePort = oSock.getPort();

			System.out.print(sdfHMS.format(new Date()) + " Creating SQL connection...");
			Properties propDown = null;
			try{
				propDown = ReadProperties.load();
				Class.forName(propDown.getProperty(ReadProperties.WEB_JDBC));
				connSQL = DriverManager.getConnection(propDown.getProperty(ReadProperties.WEB_CONNSTR),
						propDown.getProperty(ReadProperties.WEB_USER), propDown.getProperty(ReadProperties.WEB_PASSWORD));
			} catch(ClassNotFoundException excp) {
				System.out.println(sdfHMS.format(new Date()) + " Exception main - socket creation:");
				excp.printStackTrace();
				System.exit(8);
			} catch(IOException excp) {
				System.out.println(sdfHMS.format(new Date()) + " Exception main - socket creation:");
				excp.printStackTrace();
				System.exit(8);
			} catch (SQLException excp) {
				System.out.println(sdfHMS.format(new Date()) + " Exception main - socket creation:");
				excp.printStackTrace();
				System.exit(8);
			}
			System.out.println("done");
		}

		public void run(){
			String strLine = null;
			String[] strarrTokens = null;
			boolean blStateOK = false;
			// start listening
			if (blDebug){
				System.out.println(sdfHMS.format(new Date()) + " connection with=" + inetRemote.getHostAddress() +
						" port=" + iRemotePort);
			}
			clearConnectionData();
			try{
				pw = new PrintWriter(oSock.getOutputStream());
				br = new BufferedReader(new InputStreamReader(oSock.getInputStream()));

				while((strLine = br.readLine()) != null){
					if (blDebug){
						System.out.println(sdfHMS.format(new Date())  + " uid=" + strUId + " termid=" + strTermId +
								" dtid=" + strDtId + " hrid=" + strHrId +
								" read=" + strLine);
					}

					strLine = strLine.trim();

					while(strLine.matches(".*  .*")){
						strLine = strLine.replaceAll("  ", " ");
					}

					strarrTokens = strLine.split(" ");
					if (strarrTokens.length == 0){
						quit(strarrTokens);
					} // if toks = 0

					strarrTokens[0] = strarrTokens[0].toUpperCase();
					if (!(strarrTokens[0].equals("HELO") ||
							strarrTokens[0].equals("QUIT") ||
							strarrTokens[0].equals("RESTART") ||
							strarrTokens[0].equals(IDENTIFIER_SAVEDATA)

							)){
						pw.write("500 unrecognized command" + " uid=" + strUId + " termid=" + strTermId +
								" dtid=" + strDtId + " hrid=" + strHrId + "\n");
						pw.flush();
						continue;
					}

					if (strarrTokens[0].equals("QUIT")){
						if(quit(strarrTokens)){
							iProtState = ISTATE_CONNECTED;
						}
						break;
					}  // quit

					if (strarrTokens[0].equals("RESTART")){
						if(restart(strarrTokens)){
							iProtState = ISTATE_CONNECTED;
						}
						continue;
					}  // restart

					blStateOK = false;
					switch(iProtState){
					case ISTATE_CONNECTED:
						if (strarrTokens[0].equals("HELO")){
							blStateOK = true;
							if(helo(strarrTokens)){
								iProtState = ISTATE_IDENTIFIED;
							}
						}
						break;
					case ISTATE_IDENTIFIED:
						if (strarrTokens[0].equals(IDENTIFIER_SAVEDATA)) {
							blStateOK = true;
							if (saveData(strarrTokens)) {
								break;
							}
						}
					}

					if(!blStateOK){
						pw.write("530 invalid protocol state=" + iProtState + " uid=" + strUId + " termid=" + strTermId +
								" dtid=" + strDtId + " hrid=" + strHrId + "\n");
						pw.flush();
					}

				}  // while reading socket

				oSock.close();
				if (blDebug){
					System.out.println(sdfHMS.format(new Date()) + " connection closed with=" + inetRemote.getHostAddress() +
							" port=" + iRemotePort + " uid=" + strUId + " termid=" + strTermId +
							" dtid=" + strDtId + " hrid=" + strHrId);
				}
				connSQL.close();
			}catch(SocketTimeoutException oExcp){
				System.out.println(sdfHMS.format(new Date()) + " Exception Server.run:");
				oExcp.printStackTrace();
			}catch(IllegalBlockingModeException oExcp){
				System.out.println(sdfHMS.format(new Date()) + " Exception Server.run:");
				oExcp.printStackTrace();
			}catch(IOException oExcp){
				System.out.println(sdfHMS.format(new Date()) + " Exception Server.run:");
				oExcp.printStackTrace();
			}catch(SQLException oExcp){
				System.out.println(sdfHMS.format(new Date()) + " Exception Server.run:");
				oExcp.printStackTrace();
			}
		}

		private void clearConnectionData(){
			strUId = null;
			strTermId = null;
			strDtId = null;
			strHrId = null;
		}

		private boolean helo(String[] strarrTokens){
			Date oDate = null;
			if (strarrTokens.length != 3){
				pw.write("501 invalid syntax: HELO uniqid termid\n");
				pw.flush();
				return(false);
			}
			strUId = strarrTokens[1];
			strTermId = strarrTokens[2];
			oDate = new Date();
			strDtId = sdfDMY.format(oDate);
			strHrId = sdfHMS.format(oDate);
			if (blDebug){
				System.out.println(sdfHMS.format(new Date()) + "HELO uid=" + strUId + " termid=" + strTermId +
						" dtid=" + strDtId + " hrid=" + strHrId);
			}
			if (strUId.length() > 30){
				pw.write("502 invalid parameter: uniqid must have 30 or less characters\n");
				pw.flush();
				return(false);
			}
			if (strUId.matches(".*_.*")){
				pw.write("502 invalid parameter: uniqid must not contain underscore\n");
				pw.flush();
				return(false);
			}
			if (strTermId.length() > 30){
				pw.write("502 invalid parameter: termid must have 30 or less characters\n");
				pw.flush();
				return(false);
			}
			if (strTermId.matches(".*_.*")){
				pw.write("502 invalid parameter: termid must not contain underscore\n");
				pw.flush();
				return(false);
			}
			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId +
					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
			pw.flush();
			return(true);
		}

		private boolean saveData(String[] strarrTokens) {
			String articolo = null;
			String descArt = null;
			Date oDate = null;
			boolean blFound = false;
			if (strarrTokens.length != 2) {
				pw.write("501 invalid syntax: " + IDENTIFIER_SAVEDATA + "\n");
				pw.flush();
				if (blDebug) {
					DisplayMessage.display(LOGNAME, "501 invalid syntax: " + IDENTIFIER_SAVEDATA);
				}
				return (false);
			}
			
			DisplayMessage.display(LOGNAME, "\t\t" + IDENTIFIER_SAVEDATA +" ISTATE_IDENTIFIED");
			
			articolo = strarrTokens[1];
			oDate = new Date();
			strDtId = sdfDMY.format(oDate);
			strHrId = sdfHMS.format(oDate);
			
			if (blDebug) {
				DisplayMessage.display(LOGNAME, sdfHMS.format(new Date()) + " " + IDENTIFIER_SAVEDATA 
						+ " dtid=" + strDtId + " hrid=" + strHrId);
			}
			
			try {
				PreparedStatement stmtArtDesc = connSQL.prepareStatement("select mpdsc"
						+ " from cs_articoli" 
						+ " where mitem = ? and mcosc = ?");
				stmtArtDesc.setString(1, articolo);
				ResultSet rsArtDesc = stmtArtDesc.executeQuery();
				while (rsArtDesc.next()) {
					descArt = checkNull(rsArtDesc.getString(1));
					blFound = true;
					break;
				} // while
				stmtArtDesc.close();
			} catch (Exception excp) {
				DisplayMessage.display(LOGNAME, sdfHMS.format(new Date()) + "Exception connecting sql:");
				excp.printStackTrace();
				pw.write("501 exception: " + excp.getMessage() + "\n");
				pw.flush();
				return (false);
			}
			
			if (blDebug) {
				DisplayMessage.display(LOGNAME, sdfHMS.format(new Date()) + " " + IDENTIFIER_SAVEDATA + " art=" + articolo + " desc=" + descArt);
			}

			if (blFound) {
				pw.write("200 " + descArt + "\n");
				pw.flush();
			} else {
				pw.write("500 Not found\n");
				pw.flush();
			}
			return (true);
		}
		
		private boolean quit(String[] strarrTokens){
			pw.write("201 closing connection" + " uid=" + strUId + " termid=" + strTermId +
					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
			pw.flush();
			return(true);
		}

		private boolean restart(String[] strarrTokens){
			pw.write("200 restarting connection" + " uid=" + strUId + " termid=" + strTermId +
					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
			pw.flush();
			clearConnectionData();
			return(true);
		}

		private static String checkNull(String strIn) {
			StringBuffer sb = new StringBuffer();
			if (strIn == null) {
				return ("");
			} else {
				char[] chArr = strIn.trim().toCharArray();
				byte[] byArr = null;
				for(int i = 0; i < chArr.length; i++) {
					byArr = Character.toString(chArr[i]).getBytes();
					if(byArr.length > 1) {
						continue;
					}
					sb.append(chArr[i]);
				}
				return (sb.toString());
			}
		}
	}

	static public class ShutdownHook extends Thread {

		public ShutdownHook(){
			super();
		}

		public void run(){
			System.out.println(sdfHMS.format(new Date()) + " Shutting down ServerSocket...");
			try{
				if (oSSock != null){
					oSSock.close();
				}
			} catch(IOException oExcp){
				System.out.println(sdfHMS.format(new Date()) + " Exception ShutdownHook.run:");
				oExcp.printStackTrace();
			}
		}
	}
}
