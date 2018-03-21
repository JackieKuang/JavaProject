package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class T1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal.setTimeInMillis(1317742599909L); //2011-10-05 16:58:44
		cal2.setTimeInMillis(1317742599909L); 
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		
		TimeZone timeZoneTaipei = TimeZone.getTimeZone("GMT+8");
		cal.setTimeZone(timeZoneTaipei);
		sdf.setTimeZone(timeZoneTaipei); 
		System.out.println(sdf.format(cal.getTime())+",TimeZone:"+timeZoneTaipei);
		System.out.println(cal.getTimeInMillis());
		System.out.println(cal.get(Calendar.HOUR));
		
		TimeZone timeZoneBNE = TimeZone.getTimeZone("GMT"); 
		cal2.setTimeZone(timeZoneBNE);
		sdf.setTimeZone(timeZoneBNE); 
		System.out.println(sdf.format(cal2.getTime())+",TimeZone:"+timeZoneBNE);
		System.out.println(cal2.getTimeInMillis());
		System.out.println(cal2.get(Calendar.HOUR));
		
//		java.util.Calendar cal5 = java.util.Calendar.getInstance();
//		cal5.add(Calendar.MINUTE, 10);
//		long aaa = cal5.getTimeInMillis();
		
		int s1 = (4&32);
		System.err.println("s1="+s1);
	}

}
