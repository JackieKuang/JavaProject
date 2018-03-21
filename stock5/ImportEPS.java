package stock5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;

import stock5.conn.ConnectionDAO;

//匯入EPS(eps_all.csv)-->資料來源TSEC_EPS.csv
//Table Name:calculate_eps (每年跑一次,視情況而定,舊資料不需刪除update即可,需人工處理)
public class ImportEPS {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("D:\\Temp\\GOGOBOX\\stock_order\\程式合併30.2\\原始\\分析資料\\計算個股EPS(證交所)\\eps_all.csv");
		BufferedReader br = new BufferedReader(fr);
		String s = null;
		br.readLine();
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		
		java.sql.PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		String query;
		
		while((s=br.readLine())!=null) {
			try{
				s = s.replaceAll(",,", ",-,");
				s = s.replaceAll(",,", ",-,");
				String[] strArr = s.split(",");
				//System.out.println(s);
				
				query = "update calculate_eps set eps_tsec='"+strArr[2].replaceAll("#N/A", "")+"',eps_skis='"+strArr[1]+"',create_date=now() where stock_id='"+strArr[0]+"'";
				System.out.println(query);
				stmt = conn.prepareStatement(query);
				stmt.executeUpdate(query);
				
				stmt.close();
			}
			catch(Exception e1) {System.err.println(e1);}
			
		}
	}

}
