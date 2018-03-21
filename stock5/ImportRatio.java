package stock5;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;

import stock5.conn.ConnectionDAO;

//匯入新光扣抵比率資訊
public class ImportRatio {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("D:\\Temp\\GOGOBOX\\新光資訊\\97_ratio.txt");
		BufferedReader br = new BufferedReader(fr);
		String s = null;
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		
		java.sql.PreparedStatement stmt = null, stmt2 = null;
		java.sql.ResultSet rs = null, rs2 = null;
		String query;
		
		int INS_rows = 0;
		int UPD_rows = 0;
		while((s=br.readLine())!=null) {
			try{
//				s = s.replaceAll(",,", ",-,");
//				s = s.replaceAll(",,", ",-,");
				String[] strArr = s.split(",");
				System.out.println(strArr[0]+","+strArr[4]+","+s);
				
				try {
					query = "insert into stock_finance(stock_id,year,ratio,create_date) values('"+strArr[0]+"',97,'"+strArr[4]+"',now())";
					stmt2 = conn.prepareStatement(query);
					INS_rows += stmt2.executeUpdate();
				}
				catch(Exception ex) {
					System.err.println(ex.toString());
				}
				finally{
					if(stmt2!=null) {stmt2.close();}
				}
				
				try {
					query = "update stock_finance set ratio='"+strArr[4]+"' where year=97 and stock_id='"+strArr[0]+"'";
					//System.out.println(query);
					stmt = conn.prepareStatement(query);
					UPD_rows += stmt.executeUpdate();
				}
				catch(Exception ex) {
					System.err.println(ex.toString());
				}
				finally{
					if(stmt!=null) {stmt.close();}
				}
				
			}
			catch(Exception e1) {System.err.println(e1);}
			
		}
		
		System.out.println("INS_rows:"+INS_rows);
		System.out.println("UPD_rows:"+UPD_rows);
		
	}

}
