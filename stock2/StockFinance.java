package stock2;

import java.io.*;
import com.mysql.jdbc.*;
import java.sql.*;

//各股扣抵比率(01_2各股財務報表資料.csv)
//檔名:stock_finance (每年跑一次,舊資料不必刪除)
public class StockFinance {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("C:\\Documents and Settings\\jackie\\桌面\\股票資訊\\程式合併21\\原始\\分析資料\\01_2各股財務報表資料.csv");
		BufferedReader br = new BufferedReader(fr);
		String s = null;
		br.readLine();
		
		String mysql_url="jdbc:mysql://localhost:3306/stock";
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		java.sql.Connection conn = DriverManager.getConnection (mysql_url, "root", "");
		
		
		java.sql.PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		String query;
		
		while((s=br.readLine())!=null) {
			try{
				s = s.replaceAll(",,", ",0,");
				s = s.replaceAll(",,", ",0,");
				String[] strArr = s.split(",");
				
				query = "insert into stock_finance(stock_id,year,ratio,create_date) values('"+strArr[0]+"','"+strArr[1]+"','"+strArr[2]+"',now())";
				System.out.println(query);
				stmt = conn.prepareStatement(query);
				stmt.executeUpdate(query);
				
				query = "insert into stock_finance(stock_id,year,ratio,create_date) values('"+strArr[0]+"','"+strArr[3]+"','"+strArr[4]+"',now())";
				System.out.println(query);
				stmt = conn.prepareStatement(query);
				stmt.executeUpdate(query);
				
				stmt.close();
			}
			catch(Exception e1) {System.err.println(e1);}
			
		}
	}

}
