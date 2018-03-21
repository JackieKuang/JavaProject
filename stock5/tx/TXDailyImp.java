package stock5.tx;

import java.io.*;
import java.util.*;

import stock5.conn.ConnectionDAO;

public class TXDailyImp {

	public static void main(String[] args) throws Exception {
		
		String path = "D:\\Daily_2010_04_27.rpt";
		BufferedReader br_temp = new BufferedReader(new InputStreamReader(new FileInputStream(path), "BIG5"));
		
		System.err.println(path+" Import Start!!");
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		java.sql.PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		
		//先清除當天資料
		//String sqlStr = "delete from tx_daily_imp where create_date=DATE(now())";
		String sqlStr = "delete from tx_daily_imp";
		stmt = conn.prepareStatement(sqlStr);
		stmt.executeUpdate();
		
		String sqlStr2 = "insert into tx_daily_imp(deal_date,p_name,deal_y_m,finish_datetime,finish_price,finish_number,near_price,far_price,create_date) " +
				" values(?,?,?,?,?,?,?,?,now())";
		stmt = conn.prepareStatement(sqlStr2);
		
		String str = "";
		while( (str=br_temp.readLine())!=null ) {
			
			try {
				str = str.replaceAll(" ","");
				String[] arr = str.split(",");
				if(arr.length!=8) {
					continue;
				}
				
				if(!"MTX".equals(arr[1]) && !"TX".equals(arr[1])) {
					continue;
				}
				
				if(!"201005".equals(arr[2])) {
					continue;
				}
				//System.err.println(arr[1]+","+arr[2]);
				
				stmt.setString(1,arr[0]);
				stmt.setString(2,arr[1]);
				stmt.setString(3,arr[2]);
				stmt.setString(4,arr[3]);
				stmt.setString(5,arr[4]);
				stmt.setString(6,arr[5]);
				stmt.setString(7,arr[6]);
				stmt.setString(8,arr[7]);
				stmt.executeUpdate();
			}
			catch(Exception ex) {
				System.err.println("str:"+str+",err:"+ex.toString());
			}
			
			
			//System.err.println(str);
		}
		
		System.err.println(path+" Import End!!");
		
	}

}
