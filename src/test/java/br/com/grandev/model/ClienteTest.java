package br.com.grandev.model;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.grandev.model.Cliente;

public class ClienteTest {

	@Test
	public void isPF() {
		Cliente cliente1 = new Cliente();
		cliente1.setCadastro("111.111.111-11");
		assertTrue(cliente1.getIsPF());
		
		Cliente cliente2 = new Cliente();
		cliente2.setCadastro("222.222.222");
		assertFalse(cliente2.getIsPF());
		
		Cliente cliente3 = new Cliente();
		cliente3.setCadastro("");
		assertFalse(cliente3.getIsPF());
		
		Cliente cliente4 = new Cliente();
		cliente4.setCadastro(null);
		assertFalse(cliente4.getIsPF());
	}
	
	@Test
	public void isPJ() {
		Cliente cliente1 = new Cliente();
		cliente1.setCadastro("19.162.013/0001-95");
		assertTrue(cliente1.getIsPJ());
		
		Cliente cliente2 = new Cliente();
		cliente2.setCadastro("19.162.013");
		assertFalse(cliente2.getIsPJ());
		
		Cliente cliente3 = new Cliente();
		cliente3.setCadastro("");
		assertFalse(cliente3.getIsPJ());
		
		Cliente cliente4 = new Cliente();
		cliente4.setCadastro(null);
		assertFalse(cliente4.getIsPJ());
	}
	
	@Test
	public void getCadastro() {
		Cliente cliente1 = new Cliente();
		cliente1.setCadastro("111.111.111-11");
		assertNotNull(cliente1.getCadastro());
		
		Cliente cliente2 = new Cliente();
		cliente2.setCadastro("19.162.013/0001-95");
		assertNotNull(cliente2.getCadastro());
		
		Cliente cliente3 = new Cliente();
		cliente3.setCadastro("");
		assertNotNull(cliente3.getCadastro());
		
		Cliente cliente4 = new Cliente();
		cliente4.setCadastro(null);
		assertNull(cliente4.getCadastro());
	}
	
	@Test
	public void getCpf() {
		Cliente cliente1 = new Cliente();
		cliente1.setCadastro("111.111.111-11");
		assertEquals("111.111.111-11", cliente1.getCpf());
		
		Cliente cliente2 = new Cliente();
		cliente2.setCadastro("222.222.222");
		assertEquals("", cliente2.getCpf());
		
		Cliente cliente3 = new Cliente();
		cliente3.setCadastro("");
		assertEquals("", cliente3.getCpf());
		
		Cliente cliente4 = new Cliente();
		cliente4.setCadastro(null);
		assertEquals("", cliente4.getCpf());
	}
	
	@Test
	public void getCnpj() {
		Cliente cliente1 = new Cliente();
		cliente1.setCadastro("19.162.013/0001-95");
		assertEquals("19.162.013/0001-95", cliente1.getCnpj());
		
		Cliente cliente2 = new Cliente();
		cliente2.setCadastro("19.162.013");
		assertEquals("", cliente2.getCnpj());
		
		Cliente cliente3 = new Cliente();
		cliente3.setCadastro("");
		assertEquals("", cliente3.getCnpj());
		
		Cliente cliente4 = new Cliente();
		cliente4.setCadastro(null);
		assertEquals("", cliente4.getCnpj());
	}

}
