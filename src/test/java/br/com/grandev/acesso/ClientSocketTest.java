package br.com.grandev.acesso;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClientSocketTest {

	@Test
	public void deveIdentificarRegistroNovo() {
		String response;
		ClientSocket clientSocket = new ClientSocket();
		response = clientSocket.sendData("20141125", "1234", 1001);
		
		assertEquals(ClientSocket.NETWORK_UNREACHABLE, response);
	}
	
}
