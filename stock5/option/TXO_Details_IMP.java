package stock5.option;

import stock5.ReaderURL;
import stock5.conn.ConnectionDAO;
import java.util.Calendar;

/*
 * 臺股期貨資訊(選擇權買賣權分計)
 * http://www.taifex.com.tw/chinese/3/7_12_5.asp?syear=2009&smonth=9&sday=4&COMMODITY_ID=TXO
 * TYPE:1:自營商、2:投信、3:外資及陸資
 */

public class TXO_Details_IMP {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//自動當日
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int d1 = cal.get(Calendar.DATE);
		int d2 = cal.get(Calendar.DATE);
		
		//自訂日期
//		int year = 2009;
//		int month = 10;
//		int d1 = 30;
//		int d2 = 30;
		
		String date = "";
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		java.sql.PreparedStatement stmt = null, stmt2 = null;
		java.sql.ResultSet rs2 = null;
		String query = "";
		
		ReaderURL reader = new ReaderURL();
		
		while(d1<=d2) {
			Thread.sleep(1000);
			
			date = year+"-"+month+"-"+d1;
			
			String s = reader.readTextFile("http://www.taifex.com.tw/chinese/3/7_12_5.asp?syear="+year+"&smonth="+month+"&sday="+d1+"&COMMODITY_ID=TXO");
			s = s.replaceAll(" ","");
			s = s.replaceAll("height=\"15\"","");
			s = s.replaceAll("height=15","");
			s = s.replaceAll("\r\n","");
			//System.err.println(s);
			
			//查無此股資料
			if(s.indexOf("查無")!=-1) {
				d1++;
				continue;
			}
			
			if(s.indexOf("系統忙碌中")!=-1) {
				d1++;
				continue;
			}
			
			if(s.indexOf("尚未完成")!=-1) {
				d1++;
				continue;
			}
			
			String[][] TXO_C = new String[6][15];
			int ti = 0;
			
			for(int i=0; i<TXO_C.length; i++) {
				//1
				for(int d=3; d<15; d++) {
					int len1 = s.indexOf("</div><divalign=\"right\">",ti);
					int len2 = "</div><divalign=\"right\">".length();
					int len3 = "".length();
					int len_start = len1+len2+len3;
					int len_end = s.indexOf("</div></TD>",len_start);
					String s1 = "";
					
					s1 = s.substring(len_start,len_end);
					
					TXO_C[i][0] = "TXO";
					
					if(i==0 || i==3) {	//自營商
						TXO_C[i][1] = "1";
					}
					else if(i==1 || i==4) {	//投信
						TXO_C[i][1] = "2";
					}
					else {
						TXO_C[i][1] = "3";
					}
					
					if(i<=2) {
						TXO_C[i][2] = "CALL";
					}
					else {
						TXO_C[i][2] = "PUT";
					}
					
					
					TXO_C[i][d] = s1;
					//System.err.println(s1);
					ti = len_end;
				}
				
			}
			
//			for(int i=0; i<TXO_C.length; i++) {
//				for(int j=0; j<TXO_C[0].length; j++) {
//					System.err.println(TXO_C[i][j]);
//				}
//				System.err.println("========");
//			}
			
			//Insert Data
			for(int i=0; i<TXO_C.length; i++) {
				try{
					query = "insert into info_txo_details(product,type,status,d_buy_num,d_buy_money,d_sell_num,d_sell_money,d_diff_num,d_diff_money,n_buy_num,n_buy_money,n_sell_num,n_sell_money,n_diff_num,n_diff_money,create_date) " +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
					stmt = conn.prepareStatement(query);
					
					for(int j=1; j<=TXO_C[i].length; j++) {
						stmt.setString(j,TXO_C[i][j-1]);
					}
					
					stmt.setString(16,date);
					stmt.executeUpdate();
					
					stmt.close();
				}
				catch(Exception ex1) {
					System.out.println(date+":"+ex1.toString());
					continue;
				}
			}
			
			d1++;
			System.out.println(date);
		}
		
	}

}
