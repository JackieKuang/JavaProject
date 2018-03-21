package wantgoo;

import java.io.File;
import java.util.Calendar;

import stock5.CatchShareMoney;

/*
 * 執行步驟:
 * 1.09:05~13:45	StartRunStockThread (StockDataDay(一次)、StockDataDayYahooAPI(多次)、MAExecute(多次)、SmartSearch(多次))
 * 2.16:30			StartRunSearchThread (StockDataDay(一次)、MAExecute(一次)、SmartSearch(一次))
 * 
 */
public class StartRunStockThread {

	public static void main(String[] args) throws Exception {
		Log4jUtil log = new Log4jUtil();
		
		/***** 參數設定 *****/
		int sleepTime = Integer.parseInt(GetProperty.readValue("runThread_StockSleepTime")); //單位：秒
		/***** 參數設定 *****/
		
		//尚未開盤時間 continue
		Calendar cal3 = Calendar.getInstance();
		int hour3 = cal3.get(Calendar.HOUR_OF_DAY);
		int min3 = cal3.get(Calendar.MINUTE);
		
		if(hour3<Integer.parseInt(GetProperty.readValue("runThread_StartTimeHour")) ||
				(hour3==Integer.parseInt(GetProperty.readValue("runThread_StartTimeHour")) && min3<Integer.parseInt(GetProperty.readValue("runThread_StartTimeMinute")))) {
			log.writeInfo("[尚未開盤時間]Stock End");
			throw new Exception("[尚未開盤時間]Stock End");
		}
		
		/***** 每日資訊 Start *****/
		if("2".equals(args[0])) {
			log.writeInfo("每日資訊");
			StockDataDay s2 = new StockDataDay();
			s2.main(args);
		}
		else if("3".equals(args[0])) {
			log.writeInfo("每日資訊 StockDataInit");
			StockDataInit s3 = new StockDataInit();
			s3.main(args);
		}
		
		/***** 每日資訊 End *****/
		
		while(true) {
			try {
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int min = cal.get(Calendar.MINUTE);
				
				//尚未開盤時間 continue
				if(hour<Integer.parseInt(GetProperty.readValue("runThread_StartTimeHour")) ||
						(hour==Integer.parseInt(GetProperty.readValue("runThread_StartTimeHour")) && min<Integer.parseInt(GetProperty.readValue("runThread_StartTimeMinute")))) {
					log.writeInfo("[尚未開盤時間]Stock End");
					Thread.sleep(sleepTime*1000);
					continue;
				}
				
				//收盤時間結束 break
				if(hour>Integer.parseInt(GetProperty.readValue("runThread_EndTimeHour")) ||
						hour==Integer.parseInt(GetProperty.readValue("runThread_EndTimeHour")) && min>Integer.parseInt(GetProperty.readValue("runThread_EndTimeMinute"))) {
					log.writeInfo("[收盤]Stock End");
					break;
				}
				
				/***** Yahoo API 即時股價 Start *****/
				log.writeInfo("Yahoo API 即時股價 Start");
				StockDataDayYahooAPI yahooAPI = new StockDataDayYahooAPI();
				yahooAPI.main(args);
				/***** Yahoo API 即時股價 End *****/
				
				/***** MA均線計算 Start *****/
				log.writeInfo("每日MA");
				MAExecute maDao = new MAExecute();
				maDao.main(args);
				/***** MA均線計算 End *****/
				
				/***** 股價淨值比計算 Start *****/
				log.writeInfo("股價淨值比計算");
				NetValueExecute netValueDao = new NetValueExecute();
				netValueDao.main(args);
				/***** 股價淨值比計算 End *****/
				
				/***** 選股 Start *****/
				log.writeInfo("選股");
				SmartSearch s1 = new SmartSearch();
				s1.main(args);
				/***** 選股 End *****/
				
				Calendar cal2 = Calendar.getInstance();
				int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
				int min2 = cal2.get(Calendar.MINUTE);
				//System.err.println("StartRunStockThread Finish!!"+"，Date:"+hour2+":"+min2);
				log.writeInfo("StartRunStockThread Finish!!"+"，Date:"+hour2+":"+min2);
				
				Thread.sleep(sleepTime*1000);
			}
			catch(Exception ex) {
				log.writeError("StartRunStockThread Err!!"+ex.toString());
			}
		}
		
	}

}
