package br.com.sometal.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import br.com.sometal.util.MD5Hashing;

public class MD5HashingTest {

	@Test
	public void convertStringToMd5() throws NoSuchAlgorithmException {
		assertNotEquals("tcarvalho", MD5Hashing.convertStringToMd5("tcarvalho"));
		assertEquals("d07125f945bd79933e12149a6de3611a", MD5Hashing.convertStringToMd5("tcarvalho"));
	}

}
