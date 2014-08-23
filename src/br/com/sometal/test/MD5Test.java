package br.com.sometal.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import br.com.sometal.util.MD5;

public class MD5Test {

	@Test
	public void convertStringToMd5() throws NoSuchAlgorithmException {
		MD5 md5 = new MD5();
		System.out.println(md5.getHashString());
		assertNotEquals("tcarvalho", MD5.getHash("tcarvalho"));
		assertEquals("d07125f945bd79933e12149a6de3611a", MD5.getHash("tcarvalho"));
	}

}
