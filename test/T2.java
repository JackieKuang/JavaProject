package test;

import java.text.DecimalFormat;

public class T2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DecimalFormat df=new DecimalFormat("#");
		df.setMinimumIntegerDigits(2);
		
		String s = "C"+df.format(201);
		System.err.print(s);
	}

}
