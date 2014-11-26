///*
// * Created on Sep 14, 2011
// *
// * This class provides support to connect PDAs to AS/400.
// *
// * A server listener is created on port 7788. The PDA must connect to this port and send the data.
// *
// * The protocol was designed to be simple, efficient and of simple testing (even using a telnet).
// *
// * It was created based on SMTP. This means that all the data is sent using ascii characters.
// *
// * PDAs require 2 kinds of connection to central system:
// *    download data
// *    send data for purchase order creation
// *
// */
//
//package com.casagrandegroup.pdaserver;
//
//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.math.BigDecimal;
//import java.net.BindException;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.SocketException;
//import java.net.SocketTimeoutException;
//import java.nio.channels.IllegalBlockingModeException;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Date;
//import java.util.Properties;
//import java.util.Vector;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipOutputStream;
//
//import com.casagrandegroup.beans.ImportEKanbanBean;
//import com.casagrandegroup.data.Articolo;
//import com.casagrandegroup.data.Constants;
//import com.casagrandegroup.data.InfoCompScaffPDA;
//import com.casagrandegroup.data.InfoODRPDA;
//import com.casagrandegroup.data.Scaffale;
//import com.casagrandegroup.data.ScaffaleArticolo;
//import com.casagrandegroup.properties.ReadProperties;
//import com.casagrandegroup.util.DisplayMessage;
//
//public class PDAServer {
//	private static final String LOGNAME = "PDAServer";
//	static private final int IPORT_PROD = 7788;
//	static private final int IPORT_TEST = 7789;
//	static private final int IBACKLOG = 100;
//	static private final String EKANBAN_DATA_DIR = File.separator + "pdadata" + File.separator;
//	static private final int ISTATE_CONNECTED = 0;
//	static private final int ISTATE_IDENTIFIED = 1;
//	static private final int ISTATE_EKAFILE = 2;
//	static private final int ISTATE_CSCAFFFILE = 3;
//	static private final String SOCIETA = "CSPRO";
//	static private ServerSocket oSSock = null;
//	static private boolean blDebug = true;
//	static private boolean blTest = false;
//	static private SimpleDateFormat sdfLog = null;
//	static private SimpleDateFormat sdfYMD = null;
//	static private SimpleDateFormat sdfYM = null;
//	static private SimpleDateFormat sdfHMS = null;
//	static private SimpleDateFormat sdfHMSS = null;
//
//	public static void main(String[] cmdline) {
//		Connection connSQL = null;
//		sdfLog = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
//		sdfLog.setLenient(false);
//		sdfYMD = new SimpleDateFormat("yyyyMMdd");
//		sdfYMD.setLenient(false);
//		sdfYM = new SimpleDateFormat("yyyyMM");
//		sdfYM.setLenient(false);
//		sdfHMS = new SimpleDateFormat("HHmmss");
//		sdfHMS.setLenient(false);
//		sdfHMSS = new SimpleDateFormat("HHmmssSSS");
//		sdfHMSS.setLenient(false);
//
//		System.out.println(sdfLog.format(new Date()) + "Starting PDAServer...");
//
//		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
//
//		if (!getCmdLine(cmdline)){
//			System.out.println(sdfLog.format(new Date()) + "Parametri riga di comando invalidi. Startup continuera comunque");
//		}
//
//		System.out.println(sdfLog.format(new Date()) + "Checking SQL connection...");
//		Properties propDown = null;
//		try{
//			propDown = ReadProperties.load();
//			System.out.println(sdfLog.format(new Date()) + "   jdbc classname=" + propDown.getProperty(ReadProperties.WEB_JDBC));
//			System.out.println(sdfLog.format(new Date()) + "   jdbc connstr=" + propDown.getProperty(ReadProperties.WEB_CONNSTR));
//			Class.forName(propDown.getProperty(ReadProperties.WEB_JDBC));
//			connSQL = DriverManager.getConnection(propDown.getProperty(ReadProperties.WEB_CONNSTR),
//					propDown.getProperty(ReadProperties.WEB_USER), propDown.getProperty(ReadProperties.WEB_PASSWORD));
//			connSQL.close();
//		}catch(ClassNotFoundException excp){
//			excp.printStackTrace();
//			System.exit(8);
//		}catch(IOException excp){
//			excp.printStackTrace();
//			System.exit(8);
//		}catch(SQLException excp){
//			excp.printStackTrace();
//			System.exit(8);
//		}
//
//		if(blTest){
//		}else{
//		}
//
//		System.out.print(sdfLog.format(new Date()) + "Creating socket...");
//		try{
//			oSSock = new ServerSocket((blTest?IPORT_TEST:IPORT_PROD), IBACKLOG, null);
//		}catch(IOException oExcp){
//			System.out.println(sdfLog.format(new Date()) + "Exception main - socket creation:");
//			oExcp.printStackTrace();
//			System.exit(8);
//		}
//		System.out.println("done");
//
//		Server oServer = new Server();
//		Thread oThread = new Thread(oServer);
//		oThread.start();
//	}
//
//	private static boolean getCmdLine(String[] args){
//		String strArg = null;
//		for(int i = 0; i < args.length; i++){
//			strArg = args[i].trim().toLowerCase();
//			if (strArg.equals("--debug")){
//				blDebug = true;
//				continue;
//			}
//			if (strArg.equals("--test")){
//				blTest = true;
//				continue;
//			}
//			System.out.println(sdfLog.format(new Date()) + "Invalid parameter: " + args[i]);
//			return(false);
//		}
//		return(true);
//	}
//
//	/*
//	private static String getArgValue(String[] args, int i) {
//		i++;
//		if (i < args.length){
//			return(args[i].trim());
//		} else {
//			return(null);
//		}
//	}
//	*/
//
//	static public class Server implements Runnable {
//		private Socket oSock = null;
//
//		public void run(){
//			// start listening
//			try{
//				while(true){
//					if (blDebug){
//						System.out.println(sdfLog.format(new Date()) + "entering accept");
//					}
//
//					oSock = oSSock.accept();
//
//					if (blDebug){
//						System.out.println(sdfLog.format(new Date()) + "connection accepted");
//					}
//
//					PDAConnection oConn = new PDAConnection(oSock);
//					Thread oThread = new Thread(oConn);
//					oThread.start();
//
//				}  // while true
//			}catch(SocketTimeoutException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception Server.run:");
//				oExcp.printStackTrace();
//			}catch(SocketException oExcp){
//				if (oExcp.getMessage().equals("Socket closed")){
//					System.out.println(sdfLog.format(new Date()) + "Socket was closed - program will end");
//				}else{
//					System.out.println(sdfLog.format(new Date()) + "Exception Server.run:" + oExcp.getMessage());
//					oExcp.printStackTrace();
//				}
//			}catch(IllegalBlockingModeException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception Server.run:");
//				oExcp.printStackTrace();
//			}catch(IOException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception Server.run:");
//				oExcp.printStackTrace();
//			}
//		}
//	}
//
//	static public class PDAConnection implements Runnable {
//		private Socket oSock = null;
//		private PrintWriter pw = null;
//		private BufferedReader br = null;
//		private String strUId = null;
//		private String strTermId = null;
//		private InetAddress inetRemote = null;
//		private int iRemotePort = 0;
//		private int iProtState = ISTATE_CONNECTED;
//		private final String IDENTIFIER_CHECK_ARTICLE = "CHECKARTICOLO";
//		private final String IDENTIFIER_CHECK_ARTIMAGE = "CHECKARTIMAGE";
//		private final String IDENTIFIER_SAVE_ARTIMAGE = "SAVEARTIMAGE";
//		private final String IDENTIFIER_GETIMAGE = "GETIMAGE";
//		static private final String ARTIMAGE_DIR = "/home/casagrande/artimages/";
//		private String strDtId = null;
//		private String strHrId = null;
//		private String strFileName = null;
//		private String strLastMod = null;
//		private String strFileDate = null;
//		private String strFileTime = null;
//		private Vector<InfoODRPDA> veFileODA = null;
//		private Vector<InfoCompScaffPDA> veFileCompScaff = null;
//		private Connection connSQL = null;
//
//		public PDAConnection(Socket inpSock){
//			oSock = inpSock;
//			inetRemote = oSock.getInetAddress();
//			iRemotePort = oSock.getPort();
//
//			System.out.print(sdfLog.format(new Date()) + "Creating SQL connection...");
//			Properties propDown = null;
//			try{
//				propDown = ReadProperties.load();
//				Class.forName(propDown.getProperty(ReadProperties.WEB_JDBC));
//				connSQL = DriverManager.getConnection(propDown.getProperty(ReadProperties.WEB_CONNSTR),
//						propDown.getProperty(ReadProperties.WEB_USER), propDown.getProperty(ReadProperties.WEB_PASSWORD));
//			}catch(ClassNotFoundException excp){
//				System.out.println(sdfLog.format(new Date()) + "Exception main - socket creation:");
//				excp.printStackTrace();
//				System.exit(8);
//			}catch(IOException excp){
//				System.out.println(sdfLog.format(new Date()) + "Exception main - socket creation:");
//				excp.printStackTrace();
//				System.exit(8);
//			}catch(SQLException excp){
//				System.out.println(sdfLog.format(new Date()) + "Exception main - socket creation:");
//				excp.printStackTrace();
//				System.exit(8);
//			}
//			System.out.println("done");
//		}
//
//		public void run(){
//			String strLine = null;
//			String[] strarrTokens = null;
//			boolean blStateOK = false;
//			// start listening
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "connection with=" + inetRemote.getHostAddress() +
//						" port=" + iRemotePort);
//			}
//			clearConnectionData();
//			try{
//				pw = new PrintWriter(oSock.getOutputStream());
//				br = new BufferedReader(new InputStreamReader(oSock.getInputStream()));
//
//				while((strLine = br.readLine()) != null){
//					if (blDebug){
//						System.out.println(sdfLog.format(new Date())  + " uid=" + strUId + " termid=" + strTermId +
//								" dtid=" + strDtId + " hrid=" + strHrId +
//								" read=" + strLine);
//					}
//
//					strLine = strLine.trim();
//
//					while(strLine.matches(".*  .*")){
//						strLine = strLine.replaceAll("  ", " ");
//					}
//
//					strarrTokens = strLine.split(" ");
//					if (strarrTokens.length == 0){
//						quit(strarrTokens);
//					} // if toks = 0
//
//					strarrTokens[0] = strarrTokens[0].toUpperCase();
//					if (!(strarrTokens[0].equals("HELO") ||
//							strarrTokens[0].equals("QUIT") ||
//							strarrTokens[0].equals("RESTART") ||
//
//							strarrTokens[0].equals("ARTICOLIZIP") ||
//							strarrTokens[0].equals("SCAFFALIZIP") ||
//							strarrTokens[0].equals("SCAFFALIARTICOLOZIP") ||
//
//							strarrTokens[0].equals("EKAFILE") ||
//							strarrTokens[0].equals("EKADATA") ||
//							strarrTokens[0].equals("EKAENDDATA") ||
//
//							strarrTokens[0].equals("CSCAFFFILE") ||
//							strarrTokens[0].equals("CSCAFFDATA") ||
//							strarrTokens[0].equals("CSCAFFENDDATA") ||
//							
//							strarrTokens[0].equals(IDENTIFIER_CHECK_ARTICLE) || 
//							strarrTokens[0].equals(IDENTIFIER_CHECK_ARTIMAGE) || 
//							strarrTokens[0].equals(IDENTIFIER_SAVE_ARTIMAGE) ||
//							strarrTokens[0].equals(IDENTIFIER_GETIMAGE)
//
//							)){
//						pw.write("500 unrecognized command" + " uid=" + strUId + " termid=" + strTermId +
//								" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//						pw.flush();
//						continue;
//					}
//
//					if (strarrTokens[0].equals("QUIT")){
//						if(quit(strarrTokens)){
//							iProtState = ISTATE_CONNECTED;
//						}
//						break;
//					}  // quit
//
//					if (strarrTokens[0].equals("RESTART")){
//						if(restart(strarrTokens)){
//							iProtState = ISTATE_CONNECTED;
//						}
//						continue;
//					}  // restart
//
//					blStateOK = false;
//					switch(iProtState){
//					case ISTATE_CONNECTED:
//						if (strarrTokens[0].equals("HELO")){
//							blStateOK = true;
//							if(helo(strarrTokens)){
//								iProtState = ISTATE_IDENTIFIED;
//							}
//						}
//						break;
//					case ISTATE_IDENTIFIED:
//						if (strarrTokens[0].equals("ARTICOLIZIP")){
//							blStateOK = true;
//							if(getArticoliZip(strarrTokens)){
//								iProtState = ISTATE_IDENTIFIED;  // no change
//							}
//						}
//						if (strarrTokens[0].equals("SCAFFALIZIP")){
//							blStateOK = true;
//							if(getScaffaliZip(strarrTokens)){
//								iProtState = ISTATE_IDENTIFIED;  // no change
//							}
//						}
//						if (strarrTokens[0].equals("SCAFFALIARTICOLOZIP")){
//							blStateOK = true;
//							if(getScaffaliArticoloZip(strarrTokens)){
//								iProtState = ISTATE_IDENTIFIED;  // no change
//							}
//						}
//						if (strarrTokens[0].equals("EKAFILE")){
//							blStateOK = true;
//							if(eKanbanFile(strarrTokens)){
//								iProtState = ISTATE_EKAFILE;
//							}
//						}
//						if (strarrTokens[0].equals("CSCAFFFILE")){
//							blStateOK = true;
//							if(compScaffFile(strarrTokens)){
//								iProtState = ISTATE_CSCAFFFILE;
//							}
//						}
//						if (strarrTokens[0].equals(IDENTIFIER_CHECK_ARTICLE)) {
//							blStateOK = true;
//							if (checkArticolo(strarrTokens)) {
//								break;
//							}
//						}
//						if (strarrTokens[0].equals(IDENTIFIER_CHECK_ARTIMAGE)) {
//							blStateOK = true;
//							if (checkArtImage(strarrTokens)) {
//								break;
//							}
//						}
//						if (strarrTokens[0].equals(IDENTIFIER_SAVE_ARTIMAGE)) {
//							blStateOK = true;
//							if (saveArtImage(strarrTokens)) {
//								break;
//							}
//						}
//						if (strarrTokens[0].equals(IDENTIFIER_GETIMAGE)) {
//							blStateOK = true;
//							if (getImage(strarrTokens)) {
//								break;
//							}
//						}
//						break;
//					case ISTATE_EKAFILE:
//						if (strarrTokens[0].equals("EKADATA")){
//							blStateOK = true;
//							if(eKanbanData(strarrTokens)){
//								iProtState = ISTATE_EKAFILE;  // no change
//							}
//						}
//						if (strarrTokens[0].equals("EKAENDDATA")){
//							blStateOK = true;
//							if(eKanbanEndData(strarrTokens)){
//								iProtState = ISTATE_IDENTIFIED;
//							}
//						}
//						break;
//					case ISTATE_CSCAFFFILE:
//						if (strarrTokens[0].equals("CSCAFFDATA")){
//							blStateOK = true;
//							if(compScaffData(strarrTokens)){
//								iProtState = ISTATE_CSCAFFFILE;  // no change
//							}
//						}
//						if (strarrTokens[0].equals("CSCAFFENDDATA")){
//							blStateOK = true;
//							if(compScaffEndData(strarrTokens)){
//								iProtState = ISTATE_IDENTIFIED;
//							}
//						}
//						break;
//					}
//
//					if(!blStateOK){
//						pw.write("530 invalid protocol state=" + iProtState + " uid=" + strUId + " termid=" + strTermId +
//								" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//						pw.flush();
//					}
//
//				}  // while reading socket
//
//				oSock.close();
//				if (blDebug){
//					System.out.println(sdfLog.format(new Date()) + "connection closed with=" + inetRemote.getHostAddress() +
//							" port=" + iRemotePort + " uid=" + strUId + " termid=" + strTermId +
//							" dtid=" + strDtId + " hrid=" + strHrId);
//				}
//				connSQL.close();
//			}catch(SocketTimeoutException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception Server.run:");
//				oExcp.printStackTrace();
//			}catch(IllegalBlockingModeException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception Server.run:");
//				oExcp.printStackTrace();
//			}catch(IOException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception Server.run:");
//				oExcp.printStackTrace();
//			}catch(SQLException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception Server.run:");
//				oExcp.printStackTrace();
//			}
//		}
//
//		private void clearConnectionData(){
//			strUId = null;
//			strTermId = null;
//			strDtId = null;
//			strHrId = null;
//			strFileName = null;
//			strLastMod = null;
//			strFileDate = null;
//			strFileTime = null;
//		}
//
//		private boolean helo(String[] strarrTokens){
//			Date oDate = null;
//			if (strarrTokens.length != 3){
//				pw.write("501 invalid syntax: HELO uniqid termid\n");
//				pw.flush();
//				return(false);
//			}
//			strUId = strarrTokens[1];
//			strTermId = strarrTokens[2];
//			oDate = new Date();
//			strDtId = sdfYMD.format(oDate);
//			strHrId = sdfHMSS.format(oDate);
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "HELO uid=" + strUId + " termid=" + strTermId +
//						" dtid=" + strDtId + " hrid=" + strHrId);
//			}
//			if (strUId.length() > 30){
//				pw.write("502 invalid parameter: uniqid must have 30 or less characters\n");
//				pw.flush();
//				return(false);
//			}
//			if (strUId.matches(".*_.*")){
//				pw.write("502 invalid parameter: uniqid must not contain underscore\n");
//				pw.flush();
//				return(false);
//			}
//			if (strTermId.length() > 30){
//				pw.write("502 invalid parameter: termid must have 30 or less characters\n");
//				pw.flush();
//				return(false);
//			}
//			if (strTermId.matches(".*_.*")){
//				pw.write("502 invalid parameter: termid must not contain underscore\n");
//				pw.flush();
//				return(false);
//			}
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId +
//					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//			return(true);
//		}
//
//		private boolean getArticoliZip(String[] strarrTokens){
//			Vector<Articolo> veArticoli = null;
//			ServerSocket ssock = null;
//			Socket sock = null;
//			ZipOutputStream oZOS = null;
//			BufferedOutputStream oOS = null;
//			File tempFile = null;
//			String record = null;
//			int dataPort = 0;
//			InputStream is = null;
//			OutputStream os = null;
//			byte[] bytes = new byte[16384];
//			int read = 0;
//			//int iLimit = 0;
//
//			if (strarrTokens.length != 1){
//				pw.write("501 invalid syntax: ARTICOLIZIP\n");
//				pw.flush();
//				return(false);
//			}
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "ARTICOLIZIP uid=" + strUId + " termid=" + strTermId +
//						" dtid=" + strDtId + " hrid=" + strHrId);
//			}
//
//			// read data
//			try {
//				veArticoli = readDataArticoli();
//				// send data to client
//				tempFile = File.createTempFile("articoli", ".zip", new File("/tmp"));
//				if (blDebug) {
//					System.out.println(sdfLog.format(new Date()) + "file=" + tempFile.getAbsolutePath());
//				}
//				oZOS = new ZipOutputStream(new FileOutputStream(tempFile));
//				oZOS.setLevel(9);
//				oZOS.setMethod(ZipOutputStream.DEFLATED);
//				oZOS.putNextEntry(new ZipEntry("articoli.txt"));
//				oOS = new BufferedOutputStream(oZOS);
//				for(Articolo art : veArticoli){
//					/*
//					if(++iLimit > 10) {
//						break;
//					}
//					*/
//					record = art.articolo + "\t" + art.descrizione + "\t" + art.qtaxconf + "\n";
//					oOS.write(record.getBytes());
//				}
//				oOS.flush();
//				oZOS.closeEntry();
//				oZOS.close();
//				oZOS = null;
//				oOS.close();
//				oOS = null;
//			}catch(Exception excp) {
//				System.out.println(sdfLog.format(new Date()) + "Exception getArticoliZip");
//				excp.printStackTrace();
//				pw.write("505-Errore lettura articoli\n");
//				pw.write("505 " + excp.toString() + "\n");
//				pw.flush();
//				return(false);
//			}
//
//			for(int i = 1; i <= IBACKLOG; i++) {
//				dataPort = IPORT_PROD + i;
//				System.out.print(sdfLog.format(new Date()) + "Creating data socket " + dataPort + "...");
//				try{
//					ssock = new ServerSocket(dataPort);
//					ssock.setSoTimeout(10*1000);
//				}catch(BindException oExcp){
//					System.out.println("bind error: " + oExcp.getMessage());
//					System.out.println(sdfLog.format(new Date()) + "BindException data socket creation: " + oExcp.getMessage());
//					continue;
//				}catch(IOException oExcp){
//					System.out.println("error");
//					System.out.println(sdfLog.format(new Date()) + "Exception main - socket creation:");
//					oExcp.printStackTrace();
//					return(false);
//				}
//				System.out.println("done");
//				break;
//			}
//
//			pw.write("202-" + dataPort + "\n");
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId + " dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//
//			try{
//				sock = ssock.accept();
//				os = sock.getOutputStream();
//				is = new FileInputStream(tempFile);
//
//				while((read = is.read(bytes)) != -1){
//					os.write(bytes, 0, read);
//				}
//				is.close();
//				os.close();
//				sock.close();
//				ssock.close();
//			}catch(SocketTimeoutException oExcp){
//				System.out.println("timeout");
//				System.out.println(sdfLog.format(new Date()) + "Timeout accepting data connection");
//				try {
//					ssock.close();
//				}catch(IOException excp) {
//					System.out.println(sdfLog.format(new Date()) + "Exception closing data connection on timeout/1");
//					excp.printStackTrace();
//				}
//				return(false);
//			}catch(IOException oExcp){
//				System.out.println("error");
//				System.out.println(sdfLog.format(new Date()) + "Exception accepting data connection:");
//				oExcp.printStackTrace();
//				return(false);
//			}
//
//			if(!blDebug) {
//				tempFile.delete();
//			}
//
//			return(true);
//		}
//
//		private Vector<Articolo> readDataArticoli()
//		throws SQLException {
//			Vector<Articolo> veArticoli = new Vector<Articolo>();
//
//			PreparedStatement stmt = connSQL.prepareStatement("select" +
//					" a.mitem, a.mpdsc, b.qtaxconf" +
//					" from cs_articoli as a" +
//					" left outer join cs_fornkanban as b on (b.articolo = a.mitem and b.societa = a.mcosc)" +
//					" where a.mtypn = 'N'" +
//					//" where ( a.mtypn = 'N' or a.mcommcd in ('FA-2111', 'FA-215') )" +
//					//"   and a.mitem like '99900%'" +
//					" order by 1");
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()) {
//				veArticoli.add(new Articolo(checkNull(rs.getString(1)), checkNull(rs.getString(2)), rs.getBigDecimal(3)));
//			}
//			rs.close();
//			stmt.close();
//
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "readDataArticoli size=" + veArticoli.size());
//			}
//
//			return(veArticoli);
//		}
//
//		private boolean getScaffaliZip(String[] strarrTokens){
//			Vector<Scaffale> veScaffali = null;
//			ServerSocket ssock = null;
//			Socket sock = null;
//			ZipOutputStream oZOS = null;
//			BufferedOutputStream oOS = null;
//			File tempFile = null;
//			String record = null;
//			int dataPort = 0;
//			InputStream is = null;
//			OutputStream os = null;
//			byte[] bytes = new byte[16384];
//			int read = 0;
//			//int iLimit = 0;
//
//			if (strarrTokens.length != 1){
//				pw.write("501 invalid syntax: SCAFFALIZIP\n");
//				pw.flush();
//				return(false);
//			}
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "SCAFFALIZIP uid=" + strUId + " termid=" + strTermId +
//						" dtid=" + strDtId + " hrid=" + strHrId);
//			}
//
//			// read data
//			try {
//				veScaffali = readDataScaffali();
//				// send data to client
//				tempFile = File.createTempFile("scaffali", ".zip", new File("/tmp"));
//				if (blDebug) {
//					System.out.println(sdfLog.format(new Date()) + "file=" + tempFile.getAbsolutePath());
//				}
//				oZOS = new ZipOutputStream(new FileOutputStream(tempFile));
//				oZOS.setLevel(9);
//				oZOS.setMethod(ZipOutputStream.DEFLATED);
//				oZOS.putNextEntry(new ZipEntry("scaffali.txt"));
//				oOS = new BufferedOutputStream(oZOS);
//				for(Scaffale scaf : veScaffali){
//					/*
//					if(++iLimit > 10) {
//						break;
//					}
//					*/
//					record = scaf.scaffale + "\t" + scaf.descrizione + "\n";
//					oOS.write(record.getBytes());
//				}
//				oOS.flush();
//				oZOS.closeEntry();
//				oZOS.close();
//				oZOS = null;
//				oOS.close();
//				oOS = null;
//			}catch(Exception excp) {
//				System.out.println(sdfLog.format(new Date()) + "Exception getScaffaliZip");
//				excp.printStackTrace();
//				pw.write("505-Errore lettura scaffali\n");
//				pw.write("505 " + excp.toString() + "\n");
//				pw.flush();
//				return(false);
//			}
//
//			for(int i = 1; i <= IBACKLOG; i++) {
//				dataPort = IPORT_PROD + i;
//				System.out.print(sdfLog.format(new Date()) + "Creating data socket " + dataPort + "...");
//				try{
//					ssock = new ServerSocket(dataPort);
//					ssock.setSoTimeout(10*1000);
//				}catch(BindException oExcp){
//					System.out.println("bind error: " + oExcp.getMessage());
//					System.out.println(sdfLog.format(new Date()) + "BindException data socket creation: " + oExcp.getMessage());
//					continue;
//				}catch(IOException oExcp){
//					System.out.println("error");
//					System.out.println(sdfLog.format(new Date()) + "Exception main - socket creation:");
//					oExcp.printStackTrace();
//					return(false);
//				}
//				System.out.println("done");
//				break;
//			}
//
//			pw.write("202-" + dataPort + "\n");
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId + " dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//
//			try{
//				sock = ssock.accept();
//				os = sock.getOutputStream();
//				is = new FileInputStream(tempFile);
//
//				while((read = is.read(bytes)) != -1){
//					os.write(bytes, 0, read);
//				}
//				is.close();
//				os.close();
//				sock.close();
//				ssock.close();
//			}catch(SocketTimeoutException oExcp){
//				System.out.println("timeout");
//				System.out.println(sdfLog.format(new Date()) + "Timeout accepting data connection");
//				try {
//					ssock.close();
//				}catch(IOException excp) {
//					System.out.println(sdfLog.format(new Date()) + "Exception closing data connection on timeout/1");
//					excp.printStackTrace();
//				}
//				return(false);
//			}catch(IOException oExcp){
//				System.out.println("error");
//				System.out.println(sdfLog.format(new Date()) + "Exception accepting data connection:");
//				oExcp.printStackTrace();
//				return(false);
//			}
//
//			if(!blDebug) {
//				tempFile.delete();
//			}
//
//			return(true);
//		}
//
//		private Vector<Scaffale> readDataScaffali()
//		throws SQLException {
//			Vector<Scaffale> veScaffali = new Vector<Scaffale>();
//
//			PreparedStatement stmt = connSQL.prepareStatement("select" +
//					" scaffale, descrizione" +
//					" from cs_scaffali" +
//					" order by 1");
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()) {
//				veScaffali.add(new Scaffale(checkNull(rs.getString(1)), checkNull(rs.getString(2))));
//			}
//			rs.close();
//			stmt.close();
//
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "readDataScaffali size=" + veScaffali.size());
//			}
//
//			return(veScaffali);
//		}
//
//		private boolean getScaffaliArticoloZip(String[] strarrTokens){
//			Vector<ScaffaleArticolo> veScaffArt = null;
//			ServerSocket ssock = null;
//			Socket sock = null;
//			ZipOutputStream oZOS = null;
//			BufferedOutputStream oOS = null;
//			File tempFile = null;
//			String record = null;
//			int dataPort = 0;
//			InputStream is = null;
//			OutputStream os = null;
//			byte[] bytes = new byte[16384];
//			int read = 0;
//			//int iLimit = 0;
//
//			if (strarrTokens.length != 1){
//				pw.write("501 invalid syntax: SCAFFALIARTICOLO\n");
//				pw.flush();
//				return(false);
//			}
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "SCAFFALIARTICOLO uid=" + strUId + " termid=" + strTermId +
//						" dtid=" + strDtId + " hrid=" + strHrId);
//			}
//
//			// read data
//			try {
//				veScaffArt = readDataScaffaliArticolo();
//				// send data to client
//				tempFile = File.createTempFile("scaffaliarticolo", ".zip", new File("/tmp"));
//				if (blDebug) {
//					System.out.println(sdfLog.format(new Date()) + "file=" + tempFile.getAbsolutePath());
//				}
//				oZOS = new ZipOutputStream(new FileOutputStream(tempFile));
//				oZOS.setLevel(9);
//				oZOS.setMethod(ZipOutputStream.DEFLATED);
//				oZOS.putNextEntry(new ZipEntry("scaffaliarticolo.txt"));
//				oOS = new BufferedOutputStream(oZOS);
//				for(ScaffaleArticolo sa : veScaffArt){
//					/*
//					if(++iLimit > 10) {
//						break;
//					}
//					*/
//					record = sa.scaffale + "\t" + sa.articolo + "\n";
//					oOS.write(record.getBytes());
//				}
//				oOS.flush();
//				oZOS.closeEntry();
//				oZOS.close();
//				oZOS = null;
//				oOS.close();
//				oOS = null;
//			}catch(Exception excp) {
//				System.out.println(sdfLog.format(new Date()) + "Exception getScaffaliArticoloZip");
//				excp.printStackTrace();
//				pw.write("505-Errore lettura scaffaliarticolo\n");
//				pw.write("505 " + excp.toString() + "\n");
//				pw.flush();
//				return(false);
//			}
//
//			for(int i = 1; i <= IBACKLOG; i++) {
//				dataPort = IPORT_PROD + i;
//				System.out.print(sdfLog.format(new Date()) + "Creating data socket " + dataPort + "...");
//				try{
//					ssock = new ServerSocket(dataPort);
//					ssock.setSoTimeout(10*1000);
//				}catch(BindException oExcp){
//					System.out.println("bind error: " + oExcp.getMessage());
//					System.out.println(sdfLog.format(new Date()) + "BindException data socket creation: " + oExcp.getMessage());
//					continue;
//				}catch(IOException oExcp){
//					System.out.println("error");
//					System.out.println(sdfLog.format(new Date()) + "Exception main - socket creation:");
//					oExcp.printStackTrace();
//					return(false);
//				}
//				System.out.println("done");
//				break;
//			}
//
//			pw.write("202-" + dataPort + "\n");
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId + " dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//
//			try{
//				sock = ssock.accept();
//				os = sock.getOutputStream();
//				is = new FileInputStream(tempFile);
//
//				while((read = is.read(bytes)) != -1){
//					os.write(bytes, 0, read);
//				}
//				is.close();
//				os.close();
//				sock.close();
//				ssock.close();
//			}catch(SocketTimeoutException oExcp){
//				System.out.println("timeout");
//				System.out.println(sdfLog.format(new Date()) + "Timeout accepting data connection");
//				try {
//					ssock.close();
//				}catch(IOException excp) {
//					System.out.println(sdfLog.format(new Date()) + "Exception closing data connection on timeout/1");
//					excp.printStackTrace();
//				}
//				return(false);
//			}catch(IOException oExcp){
//				System.out.println("error");
//				System.out.println(sdfLog.format(new Date()) + "Exception accepting data connection:");
//				oExcp.printStackTrace();
//				return(false);
//			}
//
//			if(!blDebug) {
//				tempFile.delete();
//			}
//
//			return(true);
//		}
//
//		private Vector<ScaffaleArticolo> readDataScaffaliArticolo()
//		throws SQLException {
//			Vector<ScaffaleArticolo> veScaffArt = new Vector<ScaffaleArticolo>();
//
//			PreparedStatement stmt = connSQL.prepareStatement("select" +
//					" scaffale, articolo" +
//					" from cs_artscaffali" +
//					" order by 2,1");
//			ResultSet rs = stmt.executeQuery();
//			while(rs.next()) {
//				veScaffArt.add(new ScaffaleArticolo(checkNull(rs.getString(1)), checkNull(rs.getString(2))));
//			}
//			rs.close();
//			stmt.close();
//
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "readDataScaffaliArticolo size=" + veScaffArt.size());
//			}
//
//			return(veScaffArt);
//		}
//
//		private boolean eKanbanFile(String[] strarrTokens){
//			File oFile = null;
//			Date oDate = null;
//			long lastMod = 0;
//			if (strarrTokens.length != 5){
//				pw.write("501 invalid syntax: EKAFILE filename lastmod date time\n");
//				pw.flush();
//				return(false);
//			}
//			strFileName = strarrTokens[1];
//			strLastMod = strarrTokens[2];
//			strFileDate = strarrTokens[3];
//			strFileTime = strarrTokens[4];
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "EKAFILE name=" + strFileName + " lastmod=" + strLastMod +
//						" date=" + strFileDate + " time=" + strFileTime);
//			}
//			oFile = new File(System.getProperty("user.home") + EKANBAN_DATA_DIR + strUId + "_" + strTermId + "_" + strDtId +
//					"_" + strHrId);
//
//			if(oFile.exists()){
//				pw.write("502 invalid parameter: filename already exists - " + oFile.getName() + "\n");
//				pw.flush();
//				return(false);
//			}
//
//			try{
//				lastMod = Long.parseLong(strLastMod);
//			}catch(NumberFormatException oExcp){
//				lastMod = 0;
//			}
//			if (lastMod == 0){
//				pw.write("502 invalid parameter: lastmod must be a long different from zero\n");
//				pw.flush();
//				return(false);
//			}
//
//			if (strFileDate.length() != 8){
//				pw.flush();
//				pw.write("502 invalid parameter: date must have 8 characters\n");
//				return(false);
//			}
//			try{
//				oDate = sdfYMD.parse(strFileDate);
//			}catch(ParseException oExcp){
//				oDate = null;
//			}
//			if (oDate == null){
//				pw.write("502 invalid parameter: date must have format YYYYMMDD\n");
//				pw.flush();
//				return(false);
//			}
//
//			if (strFileTime.length() != 6){
//				pw.write("502 invalid parameter: time must have 6 characters\n");
//				pw.flush();
//				return(false);
//			}
//			try{
//				oDate = sdfHMS.parse(strFileTime);
//			}catch(ParseException oExcp){
//				oDate = null;
//			}
//			if (oDate == null){
//				pw.write("502 invalid parameter: time must have format HHMMSS\n");
//				pw.flush();
//				return(false);
//			}
//
//			veFileODA = new Vector<InfoODRPDA>();
//
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId +
//					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//			return(true);
//		}
//
//		private boolean eKanbanData(String[] strarrTokens){
//			String scaffale = null;
//			String articolo = null;
//			String qta = null;
//			int iQta = 0;
//			//Date oDate = null;
//			if (strarrTokens.length != 4){
//				pw.write("501 invalid syntax: EKADATA scaffale articolo qta#\n");
//				pw.flush();
//				return(false);
//			}
//			scaffale = strarrTokens[1];
//			articolo = strarrTokens[2];
//			qta = strarrTokens[3];
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "EKADATA uid=" + strUId + " termid=" + strTermId +
//						" dtid=" + strDtId + " hrid=" + strHrId + " scaffale=" + scaffale + " articolo=" + articolo +
//						" qta=" + qta);
//			}
//
//			if (scaffale.length() > 8){
//				pw.write("502 invalid parameter: scaffale must have up to 8 characters\n");
//				pw.flush();
//				return(false);
//			}
//
//			if (articolo.length() > 19){
//				pw.write("502 invalid parameter: articolo must have up to 19 characters\n");
//				pw.flush();
//				return(false);
//			}
//			if (qta.length() > 5){
//				pw.write("502 invalid parameter: qta must have up to 3 characters\n");
//				pw.flush();
//				return(false);
//			}
//			try{
//				iQta = Integer.parseInt(qta);
//			}catch(NumberFormatException oExcp){
//				pw.write("502 invalid parameter: qta must be an integer\n");
//				pw.flush();
//				return(false);
//			}
//
//			veFileODA.add(new InfoODRPDA(scaffale, articolo, iQta));
//
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId +
//					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//			return(true);
//		}
//
//		private boolean eKanbanEndData(String[] strarrTokens){
//			File oFile = null;
//			String strQtyRecords = null;
//			int iQtyRecords = 0;
//			int dtId = 0;
//			int hrId = 0;
//			if (strarrTokens.length != 2){
//				pw.write("501 invalid syntax: EKAENDDATA qty_records\n");
//				pw.flush();
//				return(false);
//			}
//			strQtyRecords = strarrTokens[1];
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "EKAENDDATA name=" + strFileName + " lastmod=" +
//						strLastMod + " date=" + strFileDate + " time=" + strFileTime + " rec=" + strQtyRecords);
//			}
//
//			try{
//				iQtyRecords = Integer.parseInt(strQtyRecords);
//			}catch(NumberFormatException oExcp){
//				pw.write("502 invalid parameter: qty_records must be an integer\n");
//				pw.flush();
//				return(false);
//			}
//
//			if (iQtyRecords != veFileODA.size()){
//				pw.write("502 invalid parameter: qty_records does not match received EKADATA (" +
//						veFileODA.size() + ")\n");
//				pw.flush();
//				return(false);
//			}
//
//			oFile = new File(System.getProperty("user.home") + EKANBAN_DATA_DIR + strUId + "_" + strTermId + "_" + strDtId +
//					"_" + strHrId);
//
//			if(oFile.exists()){
//				pw.write("502 invalid parameter: filename was already created during data reception\n");
//				pw.flush();
//				return(false);
//			}
//			try{
//				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(oFile)));
//				pw.println("header" + "\t" + strDtId + "\t" + strHrId + "\t" + strLastMod + "\t" + strFileDate + "\t" +
//						strFileTime + "\t" + iQtyRecords);
//				for(InfoODRPDA iop : veFileODA){
//					pw.println(iop.scaffale + "\t" + iop.articolo + "\t" + iop.qta);
//				}
//				pw.close();
//			}catch(IOException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception EKAENDDATA:");
//				oExcp.printStackTrace();
//				pw.write("503-error writing file=" + oFile.getAbsoluteFile() + "\n");
//				pw.write("503 " + oExcp.getMessage() + "\n");
//				pw.flush();
//				return(false);
//			}
//
//			try{
//				int seq = 0;
//				dtId = Integer.parseInt(strDtId);
//				hrId = Integer.parseInt(strHrId);
//				PreparedStatement stmtInsT = connSQL.prepareStatement("insert into cs_importekanbant" +
//						" (dtid, hrid, termid, uniqid, odrcreato)" +
//						" values (?,?,?,?,?)");
//				PreparedStatement stmtInsR = connSQL.prepareStatement("insert into cs_importekanbanr" +
//						" (dtid, hrid, termid, uniqid, seq, scaffale, articolo, qtaconf)" +
//						" values (?,?,?,?,?,?,?,?)");
//				stmtInsT.setInt(1, Integer.parseInt(strDtId));
//				stmtInsT.setInt(2, Integer.parseInt(strHrId));
//				stmtInsT.setString(3, strTermId);
//				stmtInsT.setString(4, strUId);
//				stmtInsT.setString(5, "0");
//				stmtInsT.executeUpdate();
//
//				for(InfoODRPDA iop : veFileODA){
//					stmtInsR.setInt(1, dtId);
//					stmtInsR.setInt(2, hrId);
//					stmtInsR.setString(3, strTermId);
//					stmtInsR.setString(4, strUId);
//					stmtInsR.setInt(5, ++seq);
//					stmtInsR.setString(6, iop.scaffale);
//					stmtInsR.setString(7, iop.articolo);
//					stmtInsR.setInt(8, iop.qta);
//					stmtInsR.executeUpdate();
//				}
//				stmtInsT.close();
//				stmtInsR.close();
//
//				// create ODA
//				ImportEKanbanBean odaBean = new ImportEKanbanBean(connSQL, dtId, hrId, strTermId, strUId);
//				odaBean.createODA();
//
//			} catch(SQLException oSQLExcp) {
//				System.out.println(sdfLog.format(new Date()) + "Exception EKAENDDATA:");
//				oSQLExcp.printStackTrace();
//				pw.write("503-Errore processo creazione ODA\n");
//				pw.flush();
//				pw.write("503 " + oSQLExcp.getMessage() + "\n");
//				pw.flush();
//				return(false);
//			}
//
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId +
//					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//			return(true);
//		}
//
//		private boolean compScaffFile(String[] strarrTokens){
//			File oFile = null;
//			Date oDate = null;
//			long lastMod = 0;
//			if (strarrTokens.length != 5){
//				pw.write("501 invalid syntax: CSCAFFFILE filename lastmod date time\n");
//				pw.flush();
//				return(false);
//			}
//			strFileName = strarrTokens[1];
//			strLastMod = strarrTokens[2];
//			strFileDate = strarrTokens[3];
//			strFileTime = strarrTokens[4];
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "CSCAFFFILE name=" + strFileName + " lastmod=" + strLastMod +
//						" date=" + strFileDate + " time=" + strFileTime);
//			}
//			oFile = new File(System.getProperty("user.home") + EKANBAN_DATA_DIR + strUId + "_" + strTermId + "_" + strDtId +
//					"_" + strHrId);
//
//			if(oFile.exists()){
//				pw.write("502 invalid parameter: filename already exists - " + oFile.getName() + "\n");
//				pw.flush();
//				return(false);
//			}
//
//			try{
//				lastMod = Long.parseLong(strLastMod);
//			}catch(NumberFormatException oExcp){
//				lastMod = 0;
//			}
//			if (lastMod == 0){
//				pw.write("502 invalid parameter: lastmod must be a long different from zero\n");
//				pw.flush();
//				return(false);
//			}
//
//			if (strFileDate.length() != 8){
//				pw.flush();
//				pw.write("502 invalid parameter: date must have 8 characters\n");
//				return(false);
//			}
//			try{
//				oDate = sdfYMD.parse(strFileDate);
//			}catch(ParseException oExcp){
//				oDate = null;
//			}
//			if (oDate == null){
//				pw.write("502 invalid parameter: date must have format YYYYMMDD\n");
//				pw.flush();
//				return(false);
//			}
//
//			if (strFileTime.length() != 6){
//				pw.write("502 invalid parameter: time must have 6 characters\n");
//				pw.flush();
//				return(false);
//			}
//			try{
//				oDate = sdfHMS.parse(strFileTime);
//			}catch(ParseException oExcp){
//				oDate = null;
//			}
//			if (oDate == null){
//				pw.write("502 invalid parameter: time must have format HHMMSS\n");
//				pw.flush();
//				return(false);
//			}
//
//			veFileCompScaff = new Vector<InfoCompScaffPDA>();
//
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId +
//					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//			return(true);
//		}
//
//		private boolean compScaffData(String[] strarrTokens){
//			String scaffale = null;
//			String articolo = null;
//			if (strarrTokens.length != 3){
//				pw.write("501 invalid syntax: CSCAFFDATA scaffale articolo#\n");
//				pw.flush();
//				return(false);
//			}
//			scaffale = strarrTokens[1];
//			articolo = strarrTokens[2];
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "CSCAFFDATA uid=" + strUId + " termid=" + strTermId +
//						" dtid=" + strDtId + " hrid=" + strHrId + " scaffale=" + scaffale + " articolo=" + articolo);
//			}
//
//			if (scaffale.length() > 8){
//				pw.write("502 invalid parameter: scaffale must have up to 8 characters\n");
//				pw.flush();
//				return(false);
//			}
//
//			if (articolo.length() > 19){
//				pw.write("502 invalid parameter: articolo must have up to 19 characters\n");
//				pw.flush();
//				return(false);
//			}
//
//			veFileCompScaff.add(new InfoCompScaffPDA(scaffale, articolo));
//
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId +
//					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//			return(true);
//		}
//
//		private boolean compScaffEndData(String[] strarrTokens){
//			File oFile = null;
//			String strQtyRecords = null;
//			int iQtyRecords = 0;
//			if (strarrTokens.length != 2){
//				pw.write("501 invalid syntax: CSCAFFENDDATA qty_records\n");
//				pw.flush();
//				return(false);
//			}
//			strQtyRecords = strarrTokens[1];
//			if (blDebug){
//				System.out.println(sdfLog.format(new Date()) + "CSCAFFENDDATA name=" + strFileName + " lastmod=" +
//						strLastMod + " date=" + strFileDate + " time=" + strFileTime + " rec=" + strQtyRecords);
//			}
//
//			try{
//				iQtyRecords = Integer.parseInt(strQtyRecords);
//			}catch(NumberFormatException oExcp){
//				pw.write("502 invalid parameter: qty_records must be an integer\n");
//				pw.flush();
//				return(false);
//			}
//
//			if (iQtyRecords != veFileCompScaff.size()){
//				pw.write("502 invalid parameter: qty_records does not match received CSCAFFDATA (" +
//						veFileCompScaff.size() + ")\n");
//				pw.flush();
//				return(false);
//			}
//
//			oFile = new File(System.getProperty("user.home") + EKANBAN_DATA_DIR + strUId + "_" + strTermId + "_" + strDtId +
//					"_" + strHrId);
//
//			if(oFile.exists()){
//				pw.write("502 invalid parameter: filename was already created during data reception\n");
//				pw.flush();
//				return(false);
//			}
//			try{
//				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(oFile)));
//				pw.println("header" + "\t" + strDtId + "\t" + strHrId + "\t" + strLastMod + "\t" + strFileDate + "\t" +
//						strFileTime + "\t" + iQtyRecords);
//				for(InfoCompScaffPDA icsp : veFileCompScaff){
//					pw.println(icsp.scaffale + "\t" + icsp.articolo);
//				}
//				pw.close();
//			}catch(IOException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception CSCAFFENDDATA:");
//				oExcp.printStackTrace();
//				pw.write("503-error writing file=" + oFile.getAbsoluteFile() + "\n");
//				pw.write("503 " + oExcp.getMessage() + "\n");
//				pw.flush();
//				return(false);
//			}
//
//			try{
//				Collections.sort(veFileCompScaff);
//				setQtaCelle();
//				String lastScaffale = null;
//				PreparedStatement stmtDel = connSQL.prepareStatement("delete from cs_artscaffali" +
//						" where scaffale = ?");
//				PreparedStatement stmtIns = connSQL.prepareStatement("insert into cs_artscaffali" +
//						" (scaffale, articolo, societa, qtacelle)" +
//						" values (?,?,?,?)");
//
//				for(InfoCompScaffPDA icsp : veFileCompScaff){
//					if(lastScaffale == null ||
//							lastScaffale.compareTo(icsp.scaffale) != 0 ) {
//						lastScaffale = icsp.scaffale;
//						stmtDel.setString(1, lastScaffale);
//						stmtDel.executeUpdate();
//					}
//					stmtIns.setString(1, icsp.scaffale);
//					stmtIns.setString(2, icsp.articolo);
//					stmtIns.setString(3, SOCIETA);
//					stmtIns.setInt(4, icsp.qtaCelle);
//					stmtIns.executeUpdate();
//				}
//				stmtDel.close();
//				stmtIns.close();
//			}catch(SQLException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception CSCAFFENDDATA:");
//				oExcp.printStackTrace();
//				pw.write("503-Errore processo composizione scaffali\n");
//				pw.write("503 " + oExcp.getMessage() + "\n");
//				pw.flush();
//				return(false);
//			}
//
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId +
//					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//			return(true);
//		}
//
//		private void setQtaCelle() {
//			InfoCompScaffPDA icsp = null;
//			InfoCompScaffPDA icspNext = null;
//
//			for(int i = 0; i < veFileCompScaff.size(); i++) {
//				icsp = veFileCompScaff.get(i);
//				icsp.qtaCelle = 1;
//				for(int j = i + 1; j< veFileCompScaff.size(); j++) {
//					icspNext = veFileCompScaff.get(j);
//					if(icspNext.scaffale.compareTo(icsp.scaffale) == 0 &&
//							icspNext.articolo.compareTo(icsp.articolo) == 0) {
//						icsp.qtaCelle++;
//						veFileCompScaff.remove(j--);
//					}
//				}
//			}
//		}
//
//		private boolean checkArticolo(String[] strarrTokens) {
//			String articolo = null;
//			String descArt = null;
//			Date oDate = null;
//			boolean blFound = false;
//			if (strarrTokens.length != 2) {
//				pw.write("501 invalid syntax: " + IDENTIFIER_CHECK_ARTICLE + "\n");
//				pw.flush();
//				if (blDebug) {
//					DisplayMessage.display(LOGNAME, "501 invalid syntax: " + IDENTIFIER_CHECK_ARTICLE);
//				}
//				return (false);
//			}
//			
//			DisplayMessage.display(LOGNAME, "\t\t" + IDENTIFIER_CHECK_ARTICLE +" ISTATE_IDENTIFIED");
//			
//			articolo = strarrTokens[1];
//			oDate = new Date();
//			strDtId = sdfYMD.format(oDate);
//			strHrId = sdfHMSS.format(oDate);
//			
//			if (blDebug) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + " " + IDENTIFIER_CHECK_ARTICLE 
//						+ " dtid=" + strDtId + " hrid=" + strHrId);
//			}
//			
//			try {
//				PreparedStatement stmtArtDesc = connSQL.prepareStatement("select mpdsc"
//						+ " from cs_articoli" 
//						+ " where mitem = ? and mcosc = ?");
//				stmtArtDesc.setString(1, articolo);
//				stmtArtDesc.setString(2, Constants.SOCIETA);
//				ResultSet rsArtDesc = stmtArtDesc.executeQuery();
//				while (rsArtDesc.next()) {
//					descArt = checkNull(rsArtDesc.getString(1));
//					blFound = true;
//					break;
//				} // while
//				stmtArtDesc.close();
//			} catch (Exception excp) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception connecting sql:");
//				excp.printStackTrace();
//				pw.write("501 exception: " + excp.getMessage() + "\n");
//				pw.flush();
//				return (false);
//			}
//			
//			if (blDebug) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + " " + IDENTIFIER_CHECK_ARTICLE + " art=" + articolo + " desc=" + descArt);
//			}
//
//			if (blFound) {
//				pw.write("200 <" + descArt + ">\n");
//				pw.flush();
//			} else {
//				pw.write("500 Not found\n");
//				pw.flush();
//			}
//			return (true);
//		}
//		
//		private boolean checkArtImage(String[] strarrTokens) {
//			String articolo = null;
//			String matricola = null;
//			int qtaFoto = 0;
//			Date oDate = null;
//			boolean blFound = false;
//
//			if (strarrTokens.length != 3) {
//				pw.write("501 invalid syntax: " + IDENTIFIER_CHECK_ARTIMAGE + "\n");
//				pw.flush();
//				if (blDebug) {
//					DisplayMessage.display(LOGNAME, "501 invalid syntax: " + IDENTIFIER_CHECK_ARTIMAGE);
//				}
//				return (false);
//			}
//			
//			DisplayMessage.display(LOGNAME, "\t\t" + IDENTIFIER_CHECK_ARTIMAGE +" ISTATE_IDENTIFIED");
//			
//			articolo = strarrTokens[1];
//			matricola = strarrTokens[2];
//			oDate = new Date();
//			strDtId = sdfYMD.format(oDate);
//			strHrId = sdfHMSS.format(oDate);
//			
//			if (blDebug) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + " " + IDENTIFIER_CHECK_ARTIMAGE
//						+ " dtid=" + strDtId + " hrid=" + strHrId);
//			}
//			
//			try {
//				PreparedStatement stmtArtImage = connSQL.prepareStatement("select qtafoto"
//						+ " from cs_artimage" 
//						+ " where societa = ? and articolo = ? and matricola = ?");
//				stmtArtImage.setString(1, Constants.SOCIETA);
//				stmtArtImage.setString(2, articolo);
//				stmtArtImage.setString(3, matricola);
//				ResultSet rsArtImage= stmtArtImage.executeQuery();
//				while (rsArtImage.next()) {
//					qtaFoto = rsArtImage.getInt(1);
//					blFound = true;
//					break;
//				} // while
//				stmtArtImage.close();
//			} catch (Exception excp) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception connecting sql:");
//				excp.printStackTrace();
//				pw.write("501 exception: " + excp.getMessage() + "\n");
//				pw.flush();
//				return (false);
//			}
//			
//			if (blDebug) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + " " + IDENTIFIER_CHECK_ARTIMAGE + " art=" + articolo + " mat=" + matricola + " qft=" + qtaFoto);
//			}
//
//			if (blFound) {
//				pw.write("200 <" + qtaFoto + ">\n");
//				pw.flush();
//			} else {
//				pw.write("500 Not found\n");
//				pw.flush();
//			}
//			return (true);
//		}
//		
//		private boolean saveArtImage(String[] strarrTokens) {
//			String articolo = null;
//			String matricola = null;
//			int dtInvio = 0;
//			int hrInvio = 0;
//			BigDecimal peso = null;
//			int qtaFoto = 1;
//			Date oDate = null;
//			boolean blFound = false;
//			ServerSocket ssock = null;
//			Socket sock = null;
//			int dataPort = 0;
//			InputStream is = null;
//			OutputStream os = null;
//			File file = null;
//			byte[] bytes = new byte[16384];
//			int read = 0;
//			
//			if (strarrTokens.length != 4) {
//				pw.write("501 invalid syntax: " + IDENTIFIER_SAVE_ARTIMAGE + "\n");
//				pw.flush();
//				if (blDebug) {
//					DisplayMessage.display(LOGNAME, "501 invalid syntax: " + IDENTIFIER_SAVE_ARTIMAGE);
//				}
//				return (false);
//			}
//			
//			DisplayMessage.display(LOGNAME, "\t\t" + IDENTIFIER_SAVE_ARTIMAGE + " ISTATE_IDENTIFIED");
//			
//			articolo = strarrTokens[1];
//			matricola = (strarrTokens[2].equals("\"\"") ? "" : strarrTokens[2]);
//			peso = new BigDecimal(strarrTokens[3]);
//			oDate = new Date();
//			strDtId = sdfYMD.format(oDate);
//			strHrId = sdfHMSS.format(oDate);
//			dtInvio = Integer.parseInt(sdfYMD.format(oDate));
//			hrInvio = Integer.parseInt(sdfHMS.format(oDate));
//			
//			if (blDebug) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + " " + IDENTIFIER_SAVE_ARTIMAGE
//						+ " dtid=" + strDtId + " hrid=" + strHrId);
//			}
//			
//			try {
//				PreparedStatement stmtSel = connSQL.prepareStatement("select count(*) from cs_artimage"
//						+ " where societa = ? and articolo = ? and matricola = ?");
//				
//				PreparedStatement stmtIns = connSQL.prepareStatement("insert into cs_artimage"
//						+ " (societa, articolo, matricola, peso, qtafoto, dtinvio, hrinvio)" 
//						+ " values (?,?,?,?,?,?,?)");
//				
//				PreparedStatement stmtUpd = connSQL.prepareStatement("update cs_artimage"
//						+ " set qtafoto = qtafoto + ?"
//						+ " where societa = ? and articolo = ? and matricola = ?");
//				
//				stmtSel.setString(1, Constants.SOCIETA);
//				stmtSel.setString(2, articolo);
//				stmtSel.setString(3, matricola);
//				ResultSet rsSel = stmtSel.executeQuery();
//
//				if (rsSel.next()) {
//					if (rsSel.getInt(1) >= 1) {
//						blFound = true;
//					}
//				}
//				stmtSel.close();
//				
//				if (!blFound) {
//					stmtIns.setString(1, Constants.SOCIETA);
//					stmtIns.setString(2, articolo);
//					stmtIns.setString(3, matricola);
//					stmtIns.setBigDecimal(4, peso);
//					stmtIns.setInt(5, qtaFoto);
//					stmtIns.setInt(6, dtInvio);
//					stmtIns.setInt(7, hrInvio);
//					stmtIns.executeUpdate();
//					stmtIns.close();
//					if (blDebug) {
//						DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + " " + IDENTIFIER_CHECK_ARTIMAGE + " ins art=" + articolo + " mat=" + matricola + " pso=" + peso + " qft=" + qtaFoto);
//					}
//				} else {
//					stmtUpd.setInt(1, qtaFoto);
//					stmtUpd.setString(2, Constants.SOCIETA);
//					stmtUpd.setString(3, articolo);
//					stmtUpd.setString(4, matricola);
//					stmtUpd.executeUpdate();
//					stmtUpd.close();
//					if (blDebug) {
//						DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + " " + IDENTIFIER_SAVE_ARTIMAGE + " upd qft=" + qtaFoto);
//					}
//				}
//			} catch (Exception excp) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception connecting sql:");
//				excp.printStackTrace();
//				pw.write("501 exception: " + excp.getMessage() + "\n");
//				pw.flush();
//				return (false);
//			}
//
//			for (int i = 1; i <= IBACKLOG; i++) {
//				dataPort = IPORT_PROD + i;
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Creating data socket " + dataPort + "...");
//				try {
//					ssock = new ServerSocket(dataPort);
//					ssock.setSoTimeout(10 * 1000);
//				} catch (BindException oExcp) {
//					DisplayMessage.display(LOGNAME, "bind error: " + oExcp.getMessage());
//					DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "BindException data socket creation: " + oExcp.getMessage());
//					continue;
//				} catch (IOException oExcp) {
//					DisplayMessage.display(LOGNAME, "error");
//					DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception main - socket creation:");
//					oExcp.printStackTrace();
//					return (false);
//				}
//				DisplayMessage.display(LOGNAME, "done");
//				break;
//			}
//
//			pw.write("201-" + dataPort + "\n");
//			pw.write("200 OK" + " dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//
//			try {
//				sock = ssock.accept();
//			} catch (SocketTimeoutException oExcp) {
//				DisplayMessage.display(LOGNAME, "timeout");
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Timeout accepting data connection");
//				try {
//					ssock.close();
//				} catch (IOException excp) {
//					DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception closing data connection on timeout/1");
//					excp.printStackTrace();
//				}
//				return (false);
//			} catch (IOException oExcp) {
//				DisplayMessage.display(LOGNAME, "error");
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception accepting data connection:");
//				oExcp.printStackTrace();
//				return (false);
//			}
//
//			if (blDebug) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Addr=" +
//						((InetSocketAddress) sock.getRemoteSocketAddress()).getHostName() + " port=" +
//						((InetSocketAddress) sock.getRemoteSocketAddress()).getPort());
//			}
//
//			try {
//				File fDir = new File(getArtImageDir(articolo, matricola));
//				if (!fDir.exists()) {
//					fDir.mkdirs();
//					DisplayMessage.display(LOGNAME, fDir.getPath() + " was created!");
//				}
//				file = File.createTempFile("foto" + "_", ".jpg", fDir);
//				
//				os = new FileOutputStream(file);
//				is = sock.getInputStream();
//				if (blDebug) {
//					DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "File=" + file.getAbsolutePath());
//				}
//				while ((read = is.read(bytes)) != -1) {
//					os.write(bytes, 0, read);
//				}
//				os.flush();
//				os.close();
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Inserted - File=" + file.getAbsolutePath());
//			} catch (SocketTimeoutException oExcp) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception reading data:");
//				oExcp.printStackTrace();
//			} catch (IllegalBlockingModeException oExcp) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception reading data:");
//				oExcp.printStackTrace();
//			} catch (IOException oExcp) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception reading data:");
//				oExcp.printStackTrace();
//			}
//
//			try {
//				sock.close();
//			} catch (IOException excp) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception closing data connection on timeout/2");
//				excp.printStackTrace();
//			}
//
//			try {
//				ssock.close();
//			} catch (IOException excp) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception closing data connection on timeout/3");
//				excp.printStackTrace();
//			}
//
//			return (true);
//		}
//		
//		private boolean getImage(String[] strarrTokens) {
//			String articolo = null;
//			String matricola = null;
//			int index = 0;
//			int dataPort = 0;
//			Date oDate = null;
//			File fDir = null;
//			File file = null;
//			File[] fList = null;
//			ServerSocket ssock = null;
//			Socket sock = null;
//			InputStream is = null;
//			OutputStream os = null;
//			byte[] bytes = new byte[16384];
//			int read = 0;
//			
//			if (strarrTokens.length != 4) {
//				pw.write("501 invalid syntax: " + IDENTIFIER_GETIMAGE + "\n");
//				pw.flush();
//				if (blDebug) {
//					DisplayMessage.display(LOGNAME, "501 invalid syntax: " + IDENTIFIER_GETIMAGE);
//				}
//				return (false);
//			}
//			
//			articolo = strarrTokens[1];
//			matricola = strarrTokens[2];
//			index = Integer.parseInt(strarrTokens[3]);
//			oDate = new Date();
//			strDtId = sdfYMD.format(oDate);
//			strHrId = sdfHMSS.format(oDate);
//			
//			if (blDebug) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + " " + IDENTIFIER_GETIMAGE 
//						+ " dtid=" + strDtId + " hrid=" + strHrId);
//			}
//			
//			try {
//				fDir = new File(getArtImageDir(articolo, matricola));
//				fList = fDir.listFiles();
//				if (fList == null) {
//					pw.write("500 no directory " + fDir + "\n");
//					pw.flush();
//					return (false);
//				}
//				if (fList.length <= index) {
//					pw.write("500 no image\n");
//					pw.flush();
//					return (false);
//				}
//				Arrays.sort(fList);
//				file = fList[index];
//			} catch (Exception ex) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + "Exception: " + ex.getMessage());
//				ex.printStackTrace();
//				pw.write("501 exception: " + ex.getMessage() + "\n");
//				pw.flush();
//				return (false);
//			}
//			
//			for(int i = 1; i <= IBACKLOG; i++) {
//				dataPort = IPORT_PROD + i;
//				System.out.print(sdfLog.format(new Date()) + "Creating data socket " + dataPort + "...");
//				try{
//					ssock = new ServerSocket(dataPort);
//					ssock.setSoTimeout(10*1000);
//				}catch(BindException oExcp){
//					System.out.println("bind error: " + oExcp.getMessage());
//					System.out.println(sdfLog.format(new Date()) + "BindException data socket creation: " + oExcp.getMessage());
//					continue;
//				}catch(IOException oExcp){
//					System.out.println("error");
//					System.out.println(sdfLog.format(new Date()) + "Exception main - socket creation:");
//					oExcp.printStackTrace();
//					return(false);
//				}
//				System.out.println("done");
//				break;
//			}
//
//			pw.write("202-" + dataPort + "\n");
//			pw.write("200 OK" + " uid=" + strUId + " termid=" + strTermId + " dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//
//			if (blDebug) {
//				DisplayMessage.display(LOGNAME, sdfLog.format(new Date()) + " " + IDENTIFIER_GETIMAGE +
//						" art=" + articolo + " mat=" + matricola + " foto=" + file.getAbsolutePath());
//			}
//			try{
//				sock = ssock.accept();
//				os = sock.getOutputStream();
//				is = new FileInputStream(file);
//				while((read = is.read(bytes)) != -1){
//					os.write(bytes, 0, read);
//				}
//				is.close();
//				os.close();
//				sock.close();
//				ssock.close();
//			}catch(SocketTimeoutException oExcp){
//				System.out.println("timeout");
//				System.out.println(sdfLog.format(new Date()) + "Timeout accepting data connection");
//				try {
//					ssock.close();
//				}catch(IOException excp) {
//					System.out.println(sdfLog.format(new Date()) + "Exception closing data connection on timeout/1");
//					excp.printStackTrace();
//				}
//				return(false);
//			}catch(IOException oExcp){
//				System.out.println("error");
//				System.out.println(sdfLog.format(new Date()) + "Exception accepting data connection:");
//				oExcp.printStackTrace();
//				return(false);
//			}
//			return (true);
//		}
//		
//		private String getArtImageDir(String articolo, String matricola) {
//			return ARTIMAGE_DIR + articolo + File.separator + (matricola.equals("") ? "no-matr" : matricola);
//		}
//		
//		private boolean quit(String[] strarrTokens){
//			pw.write("201 closing connection" + " uid=" + strUId + " termid=" + strTermId +
//					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//			return(true);
//		}
//
//		private boolean restart(String[] strarrTokens){
//			pw.write("200 restarting connection" + " uid=" + strUId + " termid=" + strTermId +
//					" dtid=" + strDtId + " hrid=" + strHrId + "\n");
//			pw.flush();
//			clearConnectionData();
//			return(true);
//		}
//
//		private static String checkNull(String strIn) {
//			/*
//			if (strIn == null) {
//				return ("");
//			} else {
//				return (strIn.trim().replaceAll("[\\xc2\\xb0]", ""));
//			}
//			*/
//			StringBuffer sb = new StringBuffer();
//
//			if (strIn == null) {
//				return ("");
//			} else {
//				char[] chArr = strIn.trim().toCharArray();
//				byte[] byArr = null;
//				for(int i = 0; i < chArr.length; i++) {
//					byArr = Character.toString(chArr[i]).getBytes();
//					if(byArr.length > 1) {
//						continue;
//					}
//					sb.append(chArr[i]);
//				}
//				return (sb.toString());
//			}
//		}
//	}
//
//	static public class ShutdownHook extends Thread{
//
//		public ShutdownHook(){
//			super();
//		}
//
//		public void run(){
//			System.out.println(sdfLog.format(new Date()) + "Shutting down PDAServer...");
//			try{
//				if (oSSock != null){
//					oSSock.close();
//				}
//			} catch(IOException oExcp){
//				System.out.println(sdfLog.format(new Date()) + "Exception ShutdownHook.run:");
//				oExcp.printStackTrace();
//			}
//		}
//	}
//}
