package test;

import javax.crypto.Mac;  
import javax.crypto.SecretKey;  
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;

public class HMACSHA1 {
	private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";


	/**
	* Computes RFC 2104-compliant HMAC signature.
	* * @param data
	* The data to be signed.
	* @param key
	* The signing key.
	* @return
	* The Base64-encoded RFC 2104-compliant HMAC signature.
	* @throws
	* java.security.SignatureException when signature generation fails
	*/
	public static String calculateRFC2104HMAC(String data, String key)
	throws Exception
	{
	String result;
	try {

	// get an hmac_sha1 key from the raw key bytes
	SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

	// get an hmac_sha1 Mac instance and initialize with the signing key
	Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
	mac.init(signingKey);

	// compute the hmac on input data bytes
	byte[] rawHmac = mac.doFinal(data.getBytes());

	// base64-encode the hmac
	BASE64Encoder encoder = new BASE64Encoder();
	result = encoder.encode(rawHmac);

	} catch (Exception e) {
		throw new Exception("Failed to generate HMAC : " + e.getMessage());
	}
		return result;
	}
}