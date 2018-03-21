package id_test;

public class CodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Code code = new Code();
		String s1 = code.encode("ask027");
		String s2 = code.decode(s1);
		System.out.println(s1);
		System.out.println(s2);

	}

}
