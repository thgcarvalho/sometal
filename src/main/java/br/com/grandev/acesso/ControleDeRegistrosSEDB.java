//package br.com.grandev.acesso;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.text.SimpleDateFormat;
//import java.util.List;
//
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.JTextField;
//import javax.swing.SwingUtilities;
//
//import br.com.grandev.acesso.dao.BilheteDao;
//import br.com.grandev.acesso.model.Bilhete;
//import br.com.grandev.acesso.model.Status;
//import br.com.grandev.acesso.service.Service;
//
//public class ControleDeRegistrosSEDB implements Runnable {
//   // Connect status constants
//   public final static int NULL = 0;
//   public final static int DISCONNECTED = 1;
//   public final static int DISCONNECTING = 2;
//   public final static int BEGIN_CONNECT = 3;
//   public final static int CONNECTED = 4;
//
//	// Other constants
//	public final static String statusMessages[] = {
//			" Erro! Não foi possível se conectar!", 
//			" Desconectado", 
//			" Desconectando...",
//			" Conectando...", 
//			" Conectado" };
//	
//   public final static ControleDeRegistrosSEDB tcpObj = new ControleDeRegistrosSEDB();
//   public final static String END_CHAT_SESSION =
//      new Character((char)0).toString(); // Indicates the end of a session
//
//   // Connection atate info
//   public static String hostIP = Service.IP;
//   public static int port = Service.PORT;
//   public static int connectionStatus = DISCONNECTED;
//   public static boolean isHost = true;
//   public static String statusString = statusMessages[connectionStatus];
//   public static StringBuffer toAppend = new StringBuffer("");
//   public static StringBuffer toSend = new StringBuffer("");
//
//   // Various GUI components and info
//   public static JFrame mainFrame = null;
//   public static JTextArea chatText = null;
//   public static JPanel statusBar = null;
//   public static JLabel statusField = null;
//   public static JTextField statusColor = null;
//   public static JButton connectButton = null;
//   public static JButton sendButton = null;
//   public static JButton disconnectButton = null;
//
//   // TCP Components
//   public static ServerSocket hostServer = null;
//   public static Socket socket = null;
//   public static BufferedReader in = null;
//   public static PrintWriter out = null;
//   
//   private static boolean blDebug = true;
//   
//   public static boolean helo() {
//	   boolean helo_200 = false;
//	   String heloResp;
//		try {
//			//sendString("HELO " + "Empresa" + " " + InetAddress.getLocalHost().getHostName() + "\n");
//			out.print("HELO " + "Empresa" + " " + InetAddress.getLocalHost().getHostName() + "\n");
//			out.flush();
//
//			while ((heloResp = in.readLine()) != null) {
//				if (blDebug) {
//					appendToChatBox("DEBUG: " + "HeloResp " + heloResp + "\n");
//				}
//				if (heloResp.substring(0, 3).equals("200")) {
//					helo_200 = true;
//					break;
//				}
//				if (heloResp.substring(0, 3).equals("500")) {
//					break;
//				}
//				if (heloResp.substring(0, 3).equals("501")) {
//					break;
//				}
//				if (heloResp.substring(0, 3).equals("502")) {
//					break;
//				}
//			}
//		} catch (UnknownHostException exp) {
//			appendToChatBox("EXP: " + exp.getMessage() + "\n");
//			exp.printStackTrace();
//		} catch (IOException exp) {
//			appendToChatBox("EXP: " + exp.getMessage() + "\n");
//			exp.printStackTrace();
//		}
//		changeStatusTS(NULL, true);
//		return helo_200;
//	}
//   
//   public static void send() {
//		String origem;
//		String tipo;
//		int data;
//		int hora;
//		int codigo;
//		BilheteDao innerDao = new BilheteDao();
//        String sendResp;
//        Status status = Status.NAOENVIADO;
//		SimpleDateFormat YMD = new SimpleDateFormat("yyyyMMdd");
//		SimpleDateFormat HMS = new SimpleDateFormat("HHmmss");
//		
//		ControleDeRegistrosDB cdr = new ControleDeRegistrosDB(new BilheteDao());
//		List<Bilhete> bilhetesPendentes = cdr.getBilhetesPendentes();
//		
//		if (helo()) {
//			for (Bilhete bilhete : bilhetesPendentes) {
//				origem = bilhete.getOrigem();
//				tipo = bilhete.getTipo();
//				data = Integer.parseInt(YMD.format(bilhete.getData()));
//				hora = Integer.parseInt(HMS.format(bilhete.getData()));;
//				codigo = bilhete.getCodigo();
//
//				// Send the string
//				out.print("SAVEDATA " + origem + " " + tipo + " " + data + " " + hora + " " + codigo + "\n");
//				out.flush();
//				appendToChatBox("OUT: " + "SAVEDATA " + origem + " " + tipo + " " + data + " " + hora + " " + codigo + "\n");
//
//				try {
//					while ((sendResp = in.readLine()) != null) {
//						if (blDebug) {
//							appendToChatBox("DEBUG: " + "SendResp " + sendResp + "\n");
//						}
//						if (sendResp.substring(0, 3).equals("200")) {
//							if (blDebug) {
//								appendToChatBox("DEBUG: " + sendResp + "\n");
//							}
//							status = Status.ENVIADO;
//							break;
//						}
//						if (sendResp.substring(0, 3).equals("500")) {
//							if (blDebug) {
//								appendToChatBox("DEBUG: " + sendResp+ "\n");
//							}
//							status = Status.FALHA;
//							break;
//						}
//						if (sendResp.substring(0, 3).equals("501")) {
//							if (blDebug) {
//								appendToChatBox("DEBUG: " + sendResp + "\n");
//							}
//							status = Status.FALHA;
//							break;
//						}
//						if (sendResp.substring(0, 3).equals("502")) {
//							if (blDebug) {
//								appendToChatBox("DEBUG: " + sendResp + "\n");
//							}
//							status = Status.FALHA;
//							break;
//						}
//					}
//					bilhete.setStatus(status.getCodigo());
//					innerDao.update(bilhete);
//					appendToChatBox("INFO: " + "UPDATE " + status + "\n");
//				} catch (IOException exp) {
//					appendToChatBox("EXP: " + exp.getMessage() + "\n");
//					exp.printStackTrace();
//				}
//			}
//		} else {
//			appendToChatBox("OUT: " + "HELO NOT 200" + "\n");
//		}
//		changeStatusTS(NULL, true);
//	}
//   
//
//   /////////////////////////////////////////////////////////////////
//
//   private static JPanel initOptionsPane() {
//      JPanel panel = null;
//      ActionAdapter buttonListener = null;
//
//      // Create an options pane
//      JPanel optionsPane = new JPanel(new GridLayout(4, 1));
//      
////      JLabel label = new JLabel(); 
////      ImageIcon icon = new ImageIcon("resources/images/logo_empresa.png"); 
////      panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
////      label.setIcon(icon); 
////      panel.add(label); 
////      optionsPane.add(panel);
//
//      // Line 1
//      panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//      panel.add(new JLabel("SOMETAL"));
//      optionsPane.add(panel);
//
//      // Line 2
//      panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//      panel.add(new JLabel(""));
//      optionsPane.add(panel);
//
//      panel = new JPanel(new GridLayout(1, 2));
//      optionsPane.add(panel);
//
//      // Connect/disconnect buttons
//      JPanel buttonPane = new JPanel(new GridLayout(1, 2));
//		buttonListener = new ActionAdapter() {
//			public void actionPerformed(ActionEvent e) {
//				// Request a connection initiation
//				if (e.getActionCommand().equals("connect")) {
//					changeStatusNTS(BEGIN_CONNECT, true);
//				} else if (e.getActionCommand().equals("send")) {
//					send();
//				} else {
//					// Disconnect
//					changeStatusNTS(DISCONNECTING, true);
//				}
//			}
//		};
//      connectButton = new JButton("Conectar");
//      connectButton.setMnemonic(KeyEvent.VK_C);
//      connectButton.setActionCommand("connect");
//      connectButton.addActionListener(buttonListener);
//      connectButton.setEnabled(true);
//      
//      sendButton = new JButton("Enviar");
//      sendButton.setMnemonic(KeyEvent.VK_E);
//      sendButton.setActionCommand("send");
//      sendButton.addActionListener(buttonListener);
//      sendButton.setEnabled(false);
//      
//      disconnectButton = new JButton("Desconectar");
//      disconnectButton.setMnemonic(KeyEvent.VK_D);
//      disconnectButton.setActionCommand("disconnect");
//      disconnectButton.addActionListener(buttonListener);
//      disconnectButton.setEnabled(false);
//      
//      buttonPane.add(connectButton);
//      buttonPane.add(sendButton);
//      buttonPane.add(disconnectButton);
//      optionsPane.add(buttonPane);
//
//      return optionsPane;
//   }
//
//   /////////////////////////////////////////////////////////////////
//
//   // Initialize all the GUI components and display the frame
//   private static void initGUI() {
//      // Set up the status bar
//      statusField = new JLabel();
//      statusField.setText(statusMessages[DISCONNECTED]);
//      statusColor = new JTextField(1);
//      statusColor.setBackground(Color.red);
//      statusColor.setEditable(false);
//      statusBar = new JPanel(new BorderLayout());
//      statusBar.add(statusColor, BorderLayout.WEST);
//      statusBar.add(statusField, BorderLayout.CENTER);
//
//      // Set up the options pane
//      JPanel optionsPane = initOptionsPane();
//
//      // Set up the chat pane
//      JPanel chatPane = new JPanel(new BorderLayout());
//      chatText = new JTextArea(10, 20);
//      chatText.setLineWrap(true);
//      chatText.setEditable(false);
//      chatText.setForeground(Color.blue);
//      JScrollPane chatTextPane = new JScrollPane(chatText,
//         JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
//         JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//      
//      chatPane.add(chatTextPane, BorderLayout.CENTER);
//      chatPane.setPreferredSize(new Dimension(800, 300));
//
//      // Set up the main pane
//      JPanel mainPane = new JPanel(new BorderLayout());
//      mainPane.add(statusBar, BorderLayout.SOUTH);
//      mainPane.add(optionsPane, BorderLayout.WEST);
//      mainPane.add(chatPane, BorderLayout.CENTER);
//
//      // Set up the main frame
//      mainFrame = new JFrame("Controle de Registros");
//      mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      mainFrame.setContentPane(mainPane);
//      mainFrame.setSize(mainFrame.getPreferredSize());
//      mainFrame.setLocation(200, 200);
//      mainFrame.pack();
//      mainFrame.setVisible(true);
//   }
//
//   /////////////////////////////////////////////////////////////////
//
//   // The thread-safe way to change the GUI components while
//   // changing state
//   private static void changeStatusTS(int newConnectStatus, boolean noError) {
//      // Change state if valid state
//      if (newConnectStatus != NULL) {
//         connectionStatus = newConnectStatus;
//      }
//
//      // If there is no error, display the appropriate status message
//      if (noError) {
//         statusString = statusMessages[connectionStatus];
//      }
//      // Otherwise, display error message
//      else {
//         statusString = statusMessages[NULL];
//      }
//
//      // Call the run() routine (Runnable interface) on the
//      // error-handling and GUI-update thread
//      SwingUtilities.invokeLater(tcpObj);
//   }
//
//   /////////////////////////////////////////////////////////////////
//
//   // The non-thread-safe way to change the GUI components while
//   // changing state
//   private static void changeStatusNTS(int newConnectStatus, boolean noError) {
//      // Change state if valid state
//      if (newConnectStatus != NULL) {
//         connectionStatus = newConnectStatus;
//      }
//
//      // If there is no error, display the appropriate status message
//      if (noError) {
//         statusString = statusMessages[connectionStatus];
//      }
//      // Otherwise, display error message
//      else {
//         statusString = statusMessages[NULL];
//      }
//
//      // Call the run() routine (Runnable interface) on the
//      // current thread
//      tcpObj.run();
//   }
//
//   /////////////////////////////////////////////////////////////////
//
//   // Thread-safe way to append to the chat box
//   private static void appendToChatBox(String s) {
//      synchronized (toAppend) {
//         toAppend.append(s);
//      }
//   }
//
//   /////////////////////////////////////////////////////////////////
//
//   // Add text to send-buffer
//   @SuppressWarnings("unused")
//   private static void sendString(String s) {
//      synchronized (toSend) {
//         toSend.append(s + "\n");
//      }
//   }
//
//   /////////////////////////////////////////////////////////////////
//
//   // Cleanup for disconnect
//   private static void cleanUp() {
//      try {
//         if (hostServer != null) {
//            hostServer.close();
//            hostServer = null;
//         }
//      }
//      catch (IOException e) { hostServer = null; }
//
//      try {
//         if (socket != null) {
//            socket.close();
//            socket = null;
//         }
//      }
//      catch (IOException e) { socket = null; }
//
//      try {
//         if (in != null) {
//            in.close();
//            in = null;
//         }
//      }
//      catch (IOException e) { in = null; }
//
//      if (out != null) {
//         out.close();
//         out = null;
//      }
//   }
//
//   /////////////////////////////////////////////////////////////////
//
//   // Checks the current state and sets the enables/disables
//   // accordingly
//   public void run() {
//      switch (connectionStatus) {
//      case DISCONNECTED:
//         connectButton.setEnabled(true);
//         sendButton.setEnabled(false);
//         disconnectButton.setEnabled(false);
//         statusColor.setBackground(Color.red);
//         break;
//
//      case DISCONNECTING:
//         connectButton.setEnabled(false);
//         sendButton.setEnabled(false);
//         disconnectButton.setEnabled(false);
//         statusColor.setBackground(Color.orange);
//         break;
//
//      case CONNECTED:
//         connectButton.setEnabled(false);
//         sendButton.setEnabled(true);
//         disconnectButton.setEnabled(true);
//         statusColor.setBackground(Color.green);
//         break;
//
//      case BEGIN_CONNECT:
//         connectButton.setEnabled(false);
//         sendButton.setEnabled(false);
//         disconnectButton.setEnabled(false);
//         statusColor.setBackground(Color.orange);
//         break;
//      }
//
//      // Make sure that the button/text field states are consistent
//      // with the internal states
//      statusField.setText(statusString);
//      chatText.append(toAppend.toString());
//      toAppend.setLength(0);
//
//      mainFrame.repaint();
//   }
//
//   /////////////////////////////////////////////////////////////////
//
//   // The main procedure
//   public static void main(String args[]) {
//      //String s;
//
//      initGUI();
//      
//      while (true) {
//         try { // Poll every ~10 ms
//            Thread.sleep(10);
//         }
//         catch (InterruptedException e) {}
//
//         switch (connectionStatus) {
//         case BEGIN_CONNECT:
//            try {
//               // Try to set up a server if host
//               if (isHost) {
//            	  socket = new Socket(hostIP, port);
//               } else {
//            	  // If guest, try to connect to the server
//                  socket = new Socket(hostIP, port);
//               }
//
//               in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//               out = new PrintWriter(socket.getOutputStream(), true);
//               
//               changeStatusTS(CONNECTED, true);
//            }
//            // If error, clean up and output an error message
//            catch (IOException e) {
//               cleanUp();
//               changeStatusTS(DISCONNECTED, false);
//            }
//            break;
//
//         case CONNECTED:
//            try {
////               // Send data
////               if (toSend.length() != 0) {
////                  out.print(toSend); 
////                  out.flush();
////                  toSend.setLength(0);
////                  changeStatusTS(NULL, true);
////               }
////
////               // Receive data
////               if (in.ready()) {
////                  s = in.readLine();
////                  if ((s != null) &&  (s.length() != 0)) {
////                     // Check if it is the end of a trasmission
////                     if (s.equals(END_CHAT_SESSION)) {
////                        changeStatusTS(DISCONNECTING, true);
////                     } else {
////                    	// Otherwise, receive what text
////                        appendToChatBox("IN: " + s + "\n");
////                        changeStatusTS(NULL, true);
////                     }
////                  }
////               }
//            } catch (Exception e) { //IOException
//               cleanUp();
//               changeStatusTS(DISCONNECTED, false);
//            }
//            break;
//
//         case DISCONNECTING:
//            // Tell other chatter to disconnect as well
//            out.print(END_CHAT_SESSION); out.flush();
//
//            // Clean up (close all streams/sockets)
//            cleanUp();
//            changeStatusTS(DISCONNECTED, true);
//            break;
//
//         default: break; // do nothing
//         }
//      }
//   }
//}
//
//////////////////////////////////////////////////////////////////////
//
//// Action adapter for easy event-listener coding
//class ActionAdapter implements ActionListener {
//   public void actionPerformed(ActionEvent e) {}
//}
//
//////////////////////////////////////////////////////////////////////