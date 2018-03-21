package stock5.option;

import stock5.ReaderURL;
import stock5.conn.ConnectionDAO;
import java.util.Calendar;

/*
 * 臺股期貨資訊(選擇權契約TXO)
 * http://www.taifex.com.tw/chinese/3/7_12_4.asp?syear=2009&smonth=9&sday=4&COMMODITY_ID=TXO
 * TYPE:1:自營商、2:投信、3:外資及陸資
 */

public class TXO_IMP {

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
			
			String s = reader.readTextFile("http://www.taifex.com.tw/chinese/3/7_12_4.asp?syear="+year+"&smonth="+month+"&sday="+d1+"&COMMODITY_ID=TXO");
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
			
			String[][] TXF = new String[3][12];
			
			//自營商
			int len1 = s.indexOf("<TDbgcolor=\"#FFFFF0\"><divalign=\"center\">自營商</div></TD>");
			int len2 = "<TDalign=rightnowrapbgcolor=\"#FFFFF0\"><divalign=\"right\">".length();
			int len3 = "<TDbgcolor=\"#FFFFF0\"><divalign=\"center\">自營商</div></TD>".length();
			int len_start = len1+len2+len3;
			int len_end = s.indexOf("</div></TD>",len_start);
			String s1 = "";
			
			
			for(int i=0; i<12; i++) {
				s1 = s.substring(len_start,len_end);
				s1 = s1.replaceAll("</div><divalign=\"right\">","");
				s1 = s1.replaceAll("gn=\"right\">","");
				s1 = s1.replaceAll("divali","");
				s1 = s1.replaceAll(",","");
				len_start = len_end+len2;
				len_end = s.indexOf("</div></TD>",len_start);
				TXF[0][i] = s1;
				
				//System.err.println(s1);
			}
			
			//投信
			len1 = s.indexOf("<TDbgcolor=\"#FFFFF0\"><divalign=\"center\">投信</div></TD>");
			len2 = "<TDalign=rightnowrapbgcolor=\"#FFFFF0\"><divalign=\"right\">".length();
			len3 = "<TDbgcolor=\"#FFFFF0\"><divalign=\"center\">投信</div></TD>".length();
			len_start = len1+len2+len3;
			len_end = s.indexOf("</div></TD>",len_start);
			s1 = "";
			
			for(int i=0; i<12; i++) {
				s1 = s.substring(len_start,len_end);
				s1 = s1.replaceAll("</div><divalign=\"right\">","");
				s1 = s1.replaceAll("gn=\"right\">","");
				s1 = s1.replaceAll("divali","");
				s1 = s1.replaceAll(",","");
				len_start = len_end+len2;
				len_end = s.indexOf("</div></TD>",len_start);
				TXF[1][i] = s1;
				
				//System.err.println(s1);
			}
			
			//外資及陸資
			len1 = s.indexOf("<TDbgcolor=\"#FFFFF0\"><divalign=\"center\">外資及陸資</div></TD>");
			len2 = "<TDalign=rightnowrapbgcolor=\"#FFFFF0\"><divalign=\"right\">".length();
			len3 = "<TDbgcolor=\"#FFFFF0\"><divalign=\"center\">外資及陸資</div></TD>".length();
			len_start = len1+len2+len3;
			len_end = s.indexOf("</div></TD>",len_start);
			s1 = "";
			
			for(int i=0; i<12; i++) {
				s1 = s.substring(len_start,len_end);
				s1 = s1.replaceAll("</div><divalign=\"right\">","");
				s1 = s1.replaceAll("gn=\"right\">","");
				s1 = s1.replaceAll("divali","");
				s1 = s1.replaceAll(",","");
				len_start = len_end+len2;
				len_end = s.indexOf("</div></TD>",len_start);
				TXF[2][i] = s1;
				
				//System.err.println(s1);
			}
			
			//Insert Data
			for(int i=1; i<=TXF.length; i++) {
				try{
					query = "insert into info_txo(DEAL_UP,DEAL_UP_MONEY,DEAL_DOWN,DEAL_DOWN_MONEY,DEAL_DIFF,DEAL_DIFF_MONEY,NOT_DEAL_UP,NOT_DEAL_UP_MONEY,NOT_DEAL_DOWN,NOT_DEAL_DOWN_MONEY,NOT_DEAL_DIFF,NOT_DEAL_DIFF_MONEY,TYPE,create_date) " +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					
					stmt = conn.prepareStatement(query);
					
					for(int j=1; j<=TXF[0].length; j++) {
						stmt.setString(j,TXF[i-1][j-1]);
					}
					stmt.setInt(13,i);
					stmt.setString(14,date);
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
