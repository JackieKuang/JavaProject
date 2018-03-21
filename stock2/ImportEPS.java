package stock2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;

//匯入EPS(eps_all.csv)
//檔名:stock_tsec (每年跑一次,視情況而定,舊資料不必刪除)
public class ImportEPS {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("C:\\Documents and Settings\\jackie\\桌面\\股票資訊\\程式合併21\\原始\\分析資料\\計算個股EPS(證交所)\\eps_all.csv");
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
				String[] strArr = s.split(",");
				//System.out.println(s);
				
				query = "insert into calculate_eps(stock_id,year,eps_skis,eps_tsec,create_date) values('"+strArr[0]+"','2008','"+strArr[1]+"','"+strArr[2].replaceAll("#N/A", "")+"',now())";
				System.out.println(query);
				stmt = conn.prepareStatement(query);
				stmt.executeUpdate(query);
				
				stmt.close();
			}
			catch(Exception e1) {System.err.println(e1);}
			
		}
		
		
		
	}

}
