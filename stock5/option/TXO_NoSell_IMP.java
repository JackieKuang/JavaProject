package stock5.option;

import stock5.ReaderURL;
import stock5.conn.ConnectionDAO;
import java.util.Calendar;

/*
 * 臺股期貨資訊(選擇權每日交易行情簡表資料查詢)
 * http://www.taifex.com.tw/chinese/3/3_2_1.asp?DATA_DATE_Y=2009&DATA_DATE_M=9&DATA_DATE_D=4&COMMODITY_ID=TXO
 * TYPE:1:自營商、2:投信、3:外資及陸資
 */

public class TXO_NoSell_IMP {

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
			
			String s = reader.readTextFile("http://www.taifex.com.tw/chinese/3/3_2_1.asp?DATA_DATE_Y="+year+"&DATA_DATE_M="+month+"&DATA_DATE_D="+d1+"&COMMODITY_ID=TXO");
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
			
			String[][] TXO_C = new String[60][10];
			int ti = 0;
			String typeTmp = "CALL";
			String monthTmp = "";
			int monthCount = 0;
			
			for(int i=0; i<TXO_C.length; i++) {
				//1
				int len1 = s.indexOf("<TDwidth=\"48\"align=middle>",ti);
				int len2 = "<TDwidth=\"48\"align=middle>".length();
				int len3 = "".length();
				int len_start = len1+len2+len3;
				int len_end = s.indexOf("</TD>",len_start);
				String s1 = "";
				s1 = s.substring(len_start,len_end);
				
				//檢查是否歸回原點
				if(s1.length()>10) {break;}
				
				TXO_C[i][0] = s1;
				//System.err.println(s1);
				
				
				//2
				len1 = s.indexOf("<TDwidth=\"54\"align=right>",ti);
				len2 = "<TDwidth=\"54\"align=right>".length();
				len3 = "".length();
				len_start = len1+len2+len3;
				len_end = s.indexOf("</TD>",len_start);
				s1 = "";
				
				s1 = s.substring(len_start,len_end);
				//System.err.println(s1);
				TXO_C[i][1] = s1;
				
				//3
				len1 = s.indexOf("<TDwidth=\"49\"align=right>",ti);
				len2 = "<TDwidth=\"49\"align=right>".length();
				len3 = "".length();
				len_start = len1+len2+len3;
				len_end = s.indexOf("</TD>",len_start);
				s1 = "";
				
				s1 = s.substring(len_start,len_end);
				//System.err.println(s1);
				TXO_C[i][2] = s1;
				
				ti = len_end;
				
				//4
				len1 = s.indexOf("<TDwidth=\"49\"align=right>",ti);
				len2 = "<TDwidth=\"49\"align=right>".length();
				len3 = "".length();
				len_start = len1+len2+len3;
				len_end = s.indexOf("</TD>",len_start);
				s1 = "";
				
				s1 = s.substring(len_start,len_end);
				//System.err.println(s1);
				TXO_C[i][3] = s1;
				
				//5
				len1 = s.indexOf("<TDwidth=\"60\"align=right>",ti);
				len2 = "<TDwidth=\"60\"align=right>".length();
				len3 = "".length();
				len_start = len1+len2+len3;
				len_end = s.indexOf("</TD>",len_start);
				s1 = "";
				
				s1 = s.substring(len_start,len_end);
				//System.err.println(s1);
				TXO_C[i][4] = s1;
				
				//6
				len1 = s.indexOf("<TDwidth=\"48\"align=right>",ti);
				len2 = "<TDwidth=\"48\"align=right>".length();
				len3 = "".length();
				len_start = len1+len2+len3;
				len_end = s.indexOf("</TD>",len_start);
				s1 = "";
				
				s1 = s.substring(len_start,len_end);
				//System.err.println(s1);
				TXO_C[i][5] = s1;
				
				//7
				len1 = s.indexOf("<TDwidth=\"61\"align=middleclass=\"12red\">",ti);
				len2 = "<TDwidth=\"61\"align=middleclass=\"12red\">".length();
				len3 = "".length();
				len_start = len1+len2+len3;
				len_end = s.indexOf("</TD>",len_start);
				s1 = "";
				
				s1 = s.substring(len_start,len_end);
				s1 = s1.replaceAll("	","");
				s1 = s1.replaceAll("<fontcolor=\"red\">","");
				s1 = s1.replaceAll("<fontcolor=\"green\">","");
				//System.err.println(s1);
				TXO_C[i][6] = s1;
				
				//8
				len1 = s.indexOf("<TDwidth=\"56\"align=right>",ti);
				len2 = "<TDwidth=\"56\"align=right>".length();
				len3 = "".length();
				len_start = len1+len2+len3;
				len_end = s.indexOf("</TD>",len_start);
				s1 = "";
				
				s1 = s.substring(len_start,len_end);
				//System.err.println(s1);
				TXO_C[i][7] = s1;
				
				//9
				len1 = s.indexOf("<TDwidth=\"66\"align=right>",ti);
				len2 = "<TDwidth=\"66\"align=right>".length();
				len3 = "".length();
				len_start = len1+len2+len3;
				len_end = s.indexOf("</TD>",len_start);
				s1 = "";
				
				s1 = s.substring(len_start,len_end);
				ti = len_end;
				TXO_C[i][8] = s1;
				//System.err.println(s1);
				//System.err.println("===============");
				
				
				//10
				//檢查 CALL、PUT
				if(!monthTmp.equals(TXO_C[i][0])) {
					monthTmp = TXO_C[i][0];
					monthCount++;
					if(monthCount>2) {
						typeTmp = "PUT";
					}
				}
				TXO_C[i][9] = typeTmp;
				
			}
			
			//show data
//			for(int i=0; i<TXO_C.length; i++) {
//				for(int j=0; j<TXO_C[0].length; j++) {
//					System.err.println(TXO_C[i][j]);
//				}
//				System.err.println("========");
//			}
			
			//Insert Data
			for(int i=0; i<TXO_C.length; i++) {
				try{
					if(TXO_C[i][0]==null) {
						break;
					}
					
					query = "insert into info_txo_nosell(month,strike_price,high,low,bid,settlement_price,change1,volume,outstanding,type,create_date) " +
					" values(?,?,?,?,?,?,?,?,?,?,?)";
					
					stmt = conn.prepareStatement(query);
					
					for(int j=1; j<=TXO_C[i].length; j++) {
						stmt.setString(j,TXO_C[i][j-1]);
					}
					
					stmt.setString(11,date);
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
