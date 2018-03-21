package desEncrypter;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import java.io.*;
import java.util.*;
import com.sun.crypto.provider.*;



public class TripleDES3 {

	  Cipher ecipher;
	  Cipher dcipher;
	  
	  //樹德加解密Key
//	  static final String TripDesKey = "vrkDN+vEgh25AbZuN2T2Bo/o";
//	  static final String TripDesIV = "yhygdHE/";	//8 bytes
	  
	  //永慶加解密Key
	  static final String TripDesKey = "FkVmw+vZgh20AbBrN2T2Z0/o";
	  static final String TripDesIV = "yHygDHe/";	//8 bytes
	  
	  public TripleDES3() throws Exception {
//		System.err.println(TripDesKey.toCharArray().length);
//		System.err.println(TripDesKey.getBytes().length);
//		System.err.println(TripDesIV.toCharArray().length);
//		System.err.println(TripDesIV.getBytes().length);
		
		KeySpec keySpec = new DESedeKeySpec(TripDesKey.getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey key = keyFactory.generateSecret(keySpec);
        
        SecureRandom sr = new SecureRandom();
		IvParameterSpec iv = new IvParameterSpec(TripDesIV.getBytes("UTF8"));
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
}
