package stock5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;

import stock5.conn.ConnectionDAO;

//匯入產業名稱(industry_name.csv)
//Table Name:com_industry (視情況而定,舊資料需先刪除)
public class ImportIndustry {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("D:\\Temp\\GOGOBOX\\stock_order\\程式合併30.1\\原始\\分析資料\\import\\industry_name.csv");
		BufferedReader br = new BufferedReader(fr);
		String s = null;
		br.readLine();
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		java.sql.PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		String query;
		
		//刪除資料
		query = "delete from com_industry";
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate(query);
		
		while((s=br.readLine())!=null) {
			try{
				String[] strArr = s.split(",");
				//System.out.println(s);
				
				query = "insert into com_industry(stock_id,industry_name,create_date) values('"+strArr[0]+"','"+strArr[1]+"',now())";
				System.out.println(query);
				stmt = conn.prepareStatement(query);
				stmt.executeUpdate(query);
				
				stmt.close();
			}
			catch(Exception e1) {System.err.println(e1);}
			
		}
	}

}
