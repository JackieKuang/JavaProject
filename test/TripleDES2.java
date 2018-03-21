package test;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.io.*;
import java.util.*;
import com.sun.crypto.provider.*;



public class TripleDES2 {

	  Cipher ecipher;
	  Cipher dcipher;
	  IvParameterSpec iv;
	  SecureRandom sr;
	  static final String TripDesKey = "vrkDN+vEgh25AbZuN2T2Bo/o";
	  static final String TripDesIV = "yhygdHE/";	//8 bytes
	  
	  public TripleDES2(SecretKey key) throws Exception {
		System.err.println(TripDesKey.toCharArray().length);
		System.err.println(TripDesKey.getBytes().length);
		 
		System.err.println(TripDesIV.toCharArray().length);
		System.err.println(TripDesIV.getBytes().length);
		
		sr = new SecureRandom();
		iv = new IvParameterSpec(TripDesIV.getBytes("UTF8"));
	    ecipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");	//TripleDES DES DESede
	    dcipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
	    ecipher.init(Cipher.ENCRYPT_MODE, key,iv,sr);
	    dcipher.init(Cipher.DECRYPT_MODE, key,iv,sr);
	  }

	  public String encrypt(String str) throws Exception {
	    // Encode the string into bytes using utf-8
	    byte[] utf8 = str.getBytes("UTF8");

	    // Encrypt
	    byte[] enc = ecipher.doFinal(utf8);

	    // Encode bytes to base64 to get a string
	    return new sun.misc.BASE64Encoder().encode(enc);
	  }

	  public String decrypt(String str) throws Exception {
	    // Decode base64 to get bytes
	    byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

	    byte[] utf8 = dcipher.doFinal(dec);

	    // Decode using utf-8
	    return new String(utf8, "UTF8");
	  }

	  public static void main(String[] args) throws Exception {
		  //SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(), "DES");
		  
		  KeySpec keySpec = new DESedeKeySpec(TripDesKey.getBytes("UTF8"));
          SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
          SecretKey key = keyFactory.generateSecret(keySpec);
		  
          TripleDES2 encrypter = new TripleDES2(key);
		    String encrypted = encrypter.encrypt("A1234567890");
		    String decrypted = encrypter.decrypt(encrypted);
		    
		    System.err.println("encrypted:"+encrypted);
		    System.err.println("decrypted:"+decrypted);
	}
}
