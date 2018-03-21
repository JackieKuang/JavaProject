package wantgoo;

import java.io.File;
import java.util.Calendar;

import stock5.CatchShareMoney;


public class StartRunSearchThread {

	public static void main(String[] args) throws Exception {
		Log4jUtil log = new Log4jUtil();
		
			try {
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				int min = cal.get(Calendar.MINUTE);
				
				/***** 每日資訊 Start *****/
				log.writeInfo("每日資訊");
				StockDataDay s2 = new StockDataDay();
				s2.main(args);
				
				/***** 每日資訊 End *****/
				
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
				
				
				/***** 法人持股資訊 Start *****/
				//log.writeInfo("法人持股資訊");
				//StockDataDayEmegaLegal emegaLegalDao = new StockDataDayEmegaLegal();
				//emegaLegalDao.main(args);
				/***** 法人持股資訊 End *****/
				
				/***** 選股 Start *****/
				args  = new String[1];
				args[0] = "1";
				log.writeInfo("選股");
				SmartSearch s1 = new SmartSearch();
				s1.main(args);
				/***** 選股 End *****/
				
				Calendar cal2 = Calendar.getInstance();
				int hour2 = cal2.get(Calendar.HOUR_OF_DAY);
				int min2 = cal2.get(Calendar.MINUTE);
				//System.err.println("StartRunStockThread Finish!!"+"，Date:"+hour2+":"+min2);
				log.writeInfo("StartRunStockThread Finish!!"+"，Date:"+hour2+":"+min2);
			}
			catch(Exception ex) {
				log.writeError("[錯誤]StartRunSearchThread Error:"+ex.toString());
			}
		}
	
}
