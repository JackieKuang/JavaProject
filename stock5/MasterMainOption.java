package stock5;

import java.util.*;

import stock5.option.*;

/**
 * 主程式執行
 * 1.stock5.option.TXF_IMP
 * 2.stock5.option.TXO_IMP
 */

public class MasterMainOption {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String[] arr = null;
		
		Date date = new Date();
		long secStart = date.getTime();
		
		
		//臺股期貨資訊(期貨契約TXF)
		Thread.sleep(1000);
		System.out.println("TXF_IMP Start");
		TXF_IMP txf = new TXF_IMP();
		txf.main(args);
		System.out.println("TXF_IMP END");
		System.out.println("===========================");
		
		//臺股期貨資訊(選擇權契約TXO)
		Thread.sleep(1000);
		System.out.println("TXO_IMP Start");
		TXO_IMP txo = new TXO_IMP();
		txo.main(args);
		System.out.println("TXO_IMP END");
		System.out.println("===========================");
		
		//臺股期貨資訊(選擇權每日交易行情簡表資料查詢)
		Thread.sleep(1000);
		System.out.println("TXO_NoSell_IMP Start");
		TXO_NoSell_IMP txo_sell = new TXO_NoSell_IMP();
		txo_sell.main(args);
		System.out.println("TXO_NoSell_IMP END");
		System.out.println("===========================");
		
		//臺股期貨資訊(選擇權買賣權分計)
		Thread.sleep(1000);
		System.out.println("TXO_Details_IMP Start");
		TXO_Details_IMP txo_det = new TXO_Details_IMP();
		txo_det.main(args);
		System.out.println("TXO_Details_IMP END");
		System.out.println("===========================");
		
		
		Date date2 = new Date();
		long secEnd = date2.getTime();
		
		System.out.println("共花秒數："+((secEnd-secStart)/1000));

	}

}
