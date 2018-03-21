package test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CompareDate {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Calendar cal = Calendar.getInstance();
		String nowStr = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONDAY)+1)+"-"+cal.get(Calendar.DATE)+" 00:00:00";
		//System.err.println(nowStr);
		
		CompareDate obj = new CompareDate();
		boolean flag = obj.isDatePeriod("2014-12-30 00:00:00", nowStr, "2015-12-31 00:00:00");
		
		System.err.println(flag);
	}
	
	//判斷該時間是否在時間區間
	public boolean isDatePeriod(String startDateStr, String nowDateStr, String endDateStr) throws Exception {
		boolean flag = false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		//Start Date
		Date startDate = sdf.parse(startDateStr);
		long startTime = startDate.getTime();
		
		//Now Date
		Date nowDate = sdf.parse(nowDateStr);
		long nowTime = nowDate.getTime();
		
		//End Date
		Date endDate = sdf.parse(endDateStr);
		long endTime = endDate.getTime();
		
		if(nowTime >= startTime && nowTime <= endTime) {
			flag = true;
		}
		
		System.err.println("startDate="+startDateStr+",startTime="+startTime);
		System.err.println("nowDate="+nowDateStr+",nowTime="+nowTime);
		System.err.println("endDate="+endDateStr+",endTime="+endTime);
		
		return flag;
	}
	
}
