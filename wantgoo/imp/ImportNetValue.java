package wantgoo.imp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import stock5.conn.ConnectionDAO;

//股價淨值比 = 收盤價/每股淨值。

public class ImportNetValue {

	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("C:\\a1.csv");
		BufferedReader br = new BufferedReader(fr);
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		PreparedStatement PS1 = null, PS2=null, PS3=null;
		ResultSet RS1 = null;
		
		String sqlQuery = "select stock_id from net_price_cal where stock_id=?";
		PS1 = conn.prepareStatement(sqlQuery);
		
		String sqlInsert = "insert into net_price_cal(stock_id, stock_name, value_per_share, income_per_share) values(?, ?, ?, ?)";
		PS2 = conn.prepareStatement(sqlInsert);
		
		String sqlUpdate = "update net_price_cal set stock_name=?, value_per_share=?, income_per_share=? where stock_id=?";
		PS3 = conn.prepareStatement(sqlUpdate);
		
		String s = "";
		while((s=br.readLine())!=null) {
			try{
				String[] strArr = s.split(",");
				
				//if("#N/A".equals(strArr[2])) {
				//	continue;
				//}
				
				PS1.setString(1, strArr[0]);
				RS1 = PS1.executeQuery();
				
				if(RS1.next()) {
					PS3.setString(1, strArr[1]);
					PS3.setString(2, strArr[2]);
					PS3.setString(3, strArr[3]);
					PS3.setString(4, strArr[0]);
					PS3.executeUpdate();
					System.err.println("Update:"+strArr[0]);
				}
				else {
					PS2.setString(1, strArr[0]);
					PS2.setString(2, strArr[1]);
					PS2.setString(3, strArr[2]);
					PS2.setString(4, strArr[3]);
					PS2.executeUpdate();
					System.err.println("Insert:"+strArr[0]);
				}
			}
			catch(Exception e1) {System.err.println(e1);}
			
		}
		
		if(RS1 !=null) {RS1.close();}
		if(PS1 !=null) {PS1.close();}
		if(PS2 !=null) {PS2.close();}
		if(PS3 !=null) {PS3.close();}
		if(conn !=null) {conn.close();}
		
		if(br !=null) {br.close();}
		if(fr !=null) {fr.close();}
		
		System.err.println("ImportNetValue Finish!!");
	}

}
