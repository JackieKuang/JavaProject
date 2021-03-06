package stock5;

import java.util.*;

/**
 * 主程式執行
 * 1.CatchStockMIS 
 * 2.CatchShareMoney
 * 3.StatisticsRang
 * 4.CatchExponentMIS
 * 5.PersonalValueRecord
 */
public class MasterMainMIS {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String[] arr = null;
		
		Date date = new Date();
		long secStart = date.getTime();
		Thread.sleep(1000);
		
		//最新股價
		
		System.out.println("CatchStockMIS Start");
		CatchStockMIS cs = new CatchStockMIS();
		cs.main(arr);
		System.out.println("CatchStockMIS End");
		System.out.println("===========================");
		
		/*
		System.out.println("CatchStock Start");
		CatchStock cs = new CatchStock();
		cs.main(arr);
		System.out.println("CatchStock End");
		System.out.println("===========================");
		*/
		
		//最新除權息資料
		System.out.println("CatchShareMoney Start");
		CatchShareMoney csm = new CatchShareMoney();
		csm.main(arr);
		System.out.println("CatchShareMoney End");
		System.out.println("===========================");
		
		//計算最大漲跌幅
		System.out.println("StatisticsRang Start");
		StatisticsRang sr = new StatisticsRang();
		sr.main(arr);
		System.out.println("StatisticsRang End");
		System.out.println("===========================");
		
		//上市櫃指數
		
		System.out.println("CatchExponentMIS Start");
		CatchExponentMIS ce = new CatchExponentMIS();
		ce.main(arr);
		System.out.println("CatchExponentMIS End");
		System.out.println("===========================");
		
		/*
		System.out.println("CatchExponent Start");
		CatchExponent ce = new CatchExponent();
		ce.main(arr);
		System.out.println("CatchExponent End");
		System.out.println("===========================");
		*/
		
		//更新自選股漲跌幅
		System.out.println("PersonalValueRecord Start");
		PersonalValueRecord pvr = new PersonalValueRecord();
		pvr.main(arr);
		System.out.println("PersonalValueRecord End");
		
		Date date2 = new Date();
		long secEnd = date2.getTime();
		
		System.out.println("共花秒數："+((secEnd-secStart)/1000));
	}

}
