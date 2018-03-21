package desEncrypter;

public class RunStart {

	public static void main(String[] args) throws Exception {
		
		//TripleDES3
		TripleDES3 encrypter = new TripleDES3();
		String encrypted = encrypter.encrypt("ZQB2AGUAcgB0AHIAdQBzAHQA");
	    String decrypted = encrypter.decrypt(encrypted);
	    
	    //TripleDES
//	    DesEncrypter encrypter2 = new DesEncrypter();
//	    String encrypted2 = encrypter2.encrypt("A123456789");
//	    String decrypted2 = encrypter2.decrypt(encrypted2);
	    
	    
	    System.err.println("TripleDES3 encrypted:"+encrypted);
	    System.err.println("TripleDES3 decrypted:"+decrypted);
	    System.err.println("");
//	    System.err.println("TripleDES encrypted2:"+encrypted2);
//	    System.err.println("TripleDES decrypted2:"+decrypted2);
	    
	}

}
