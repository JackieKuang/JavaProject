package md5;

public class TestMD5 {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String checkString = "FINISHDATE=20151231";
		checkString = Hash_SHA.getSHA1(checkString,"MD5").toUpperCase();
		
		System.err.println(checkString);
	}

}
