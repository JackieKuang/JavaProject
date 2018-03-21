package wantgoo;

import java.io.File;

public class StartRun {

	public static void main(String[] args) throws Exception {
		Log4jUtil log = new Log4jUtil();
		
		/***** 每日資訊 Start *****/
		if("1".equals(GetProperty.readValue("run_data_day"))) {
			log.writeInfo("每日資訊");
			StockDataDay s2 = new StockDataDay();
			s2.main(args);
			
			log.writeInfo("每日MA");
			MAExecute maDao = new MAExecute();
			maDao.main(args);
		}
		/***** 每日資訊 End *****/
		
		/***** 每月資訊 Start *****/
		if("1".equals(GetProperty.readValue("run_data_month"))) {
			log.writeInfo("每月資訊");
			StockDataMonthEmega s3 = new StockDataMonthEmega();
			s3.main(args);
		}
		/***** 每月資訊 End *****/
		
		/***** 每年除權息資訊 Start *****/
		if("1".equals(GetProperty.readValue("run_share_money_year"))) {
			log.writeInfo("每年除權息資訊");
			CatchShareMoney s4 = new CatchShareMoney();
			s4.main(args);
		}
		/***** 每年除權息資訊 End *****/
		
		/***** 選股 Start *****/
		if("1".equals(GetProperty.readValue("run_search"))) {
			log.writeInfo("選股");
			SmartSearch s1 = new SmartSearch();
			s1.main(args);
		}
		/***** 選股 End *****/
		
		log.writeInfo("Finish!!");
	}

}
