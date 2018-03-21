
package md5;

import java.security.*;

public class Hash_SHA {
	
	public static String getSHA1(String str, String code) throws Exception {
		MessageDigest md = MessageDigest.getInstance(code);	//SHA-1�BMD5
		md.update(str.getBytes());
		byte[] digest = md.digest();
		return Hash_SHA.toHexString(digest);
	} 
	
	public static String toHexString(byte[] b) {
		  StringBuffer hexString = new StringBuffer();
		  String plainText;

		  for (int i = 0; i < b.length; i++) {
			  plainText = Integer.toHexString(0xFF & b[i]);
			  if (plainText.length() < 2) {
				  plainText = "0" + plainText;
			  }
			  hexString.append(plainText);
		  }
		  return hexString.toString();
	}
}
