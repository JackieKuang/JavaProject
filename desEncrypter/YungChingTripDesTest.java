package desEncrypter;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class YungChingTripDesTest {

	  Cipher ecipher;
	  Cipher dcipher;
	  
	  //永慶加解密Key(有2組Key)
	  static final String TripDesKey = "ZQB2AGUAcgB0AHIAdQBzAHQA";
	  static final String TripDesIV = "MQAwADQA";	//8 bytes
	  
	  public YungChingTripDesTest() throws Exception {
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
	  
	  public static void main(String[] args) throws Exception {
		  
		    //產生加解密元件
		  YungChingTripDesTest encrypter = new YungChingTripDesTest();
		  	
		  	//加密
		  	String encrypted = encrypter.encrypt("永慶房屋");
		  	//System.err.println("xxxEN1:"+encrypted);
		  	//String encrypted = "XH+kgtYcD2A=";
		  	//encrypted = java.net.URLEncoder.encode(encrypted,"utf-8");
		  	//System.err.println("xxxEN2:"+encrypted);
		  	
		  	
		  	//解密
		  	//String decrypted = java.net.URLDecoder.decode(encrypted,"utf-8");
		  	//System.err.println("xxxDE1:"+encrypted);
		  	String decrypted = encrypter.decrypt(encrypted);
		  	//System.err.println("xxxDE2:"+encrypted);
		  	
		    //顯示加解密結果
		    System.err.println("TripleDES encrypted:"+encrypted);
		    System.err.println("TripleDES decrypted:"+decrypted);
	  }
}
