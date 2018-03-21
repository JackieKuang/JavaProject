package test;

import java.util.Calendar;

public class TestC {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Calendar cal1 = Calendar.getInstance();
		long a1 = cal1.getTimeInMillis();
		
		Thread.sleep(2000);
		
		Calendar cal2 = Calendar.getInstance();
		long a2 = cal2.getTimeInMillis();
		
		long b1 = a2-a1;
		
		System.err.println(b1/1000);
	}

}
