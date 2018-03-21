package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeZoneObject {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long nd = 1000*24*60*60*1;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
	    long nm = 1000*60;//一分钟的毫秒数
		
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		
		cal.add(Calendar.DATE,30);
		
		//cal.setTimeInMillis(1321170355933L);
		//cal2.setTimeInMillis(cal2.getTimeInMillis()+nd); 
		System.err.println("Now:"+cal.getTimeInMillis());
		
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

	}
	
	
	
}
