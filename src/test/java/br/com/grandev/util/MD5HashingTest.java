package br.com.grandev.util;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import br.com.grandev.util.MD5Hashing;

public class MD5HashingTest {

	@Test
	public void convertStringToMd5() throws NoSuchAlgorithmException {
		assertNotEquals("tcarvalho", MD5Hashing.convertStringToMd5("tcarvalho"));
		assertEquals("d07125f945bd79933e12149a6de3611a", MD5Hashing.convertStringToMd5("tcarvalho"));
	}

}
