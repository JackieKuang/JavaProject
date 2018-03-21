package test;

import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class TripleDES {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
	    Security.addProvider(new com.sun.crypto.provider.SunJCE());        
	    byte[] input = "SHVuZ2hzaUNoZW4=".getBytes();

	    byte[] keyBytes = "1qazxsw23edcvfr45tgbnhy6".getBytes();
	    
	    SecretKeySpec key = new SecretKeySpec(keyBytes, "TripleDES");
	    Cipher cipher = Cipher.getInstance("TripleDES/ECB/NoPadding");
	    System.out.println("input : " + new String(input));
	    System.out.println("size : " + "abcdefghijklmnopqrstuvwx".length());
	    System.out.println("size : " + "SHVuZ2hzaUNoZW4=".length());
	    
	    // encryption pass
	    cipher.init(Cipher.ENCRYPT_MODE, key);
	    byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
	    int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
	    ctLength += cipher.doFinal(cipherText, ctLength);
	    System.out.println("cipher: " + new String(cipherText) + " bytes: " + ctLength);

	    // decryption pass

	    cipher.init(Cipher.DECRYPT_MODE, key);
	    byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
	    int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
	    ptLength += cipher.doFinal(plainText, ptLength);
	    System.out.println("plain : " + new String(plainText) + " bytes: " + ptLength);
	 }


}
