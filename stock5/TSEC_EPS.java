package stock5;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;

import stock5.conn.ConnectionDAO;

import com.mysql.jdbc.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.*;


//個股日本益比、殖利率及股價淨值比(TSEC_EPS.csv)
//http://www.tse.com.tw/ch/trading/exchange/BWIBBU/BWIBBU_d.php
//Table Name:stock_tsec (每天跑一次,視情況而定,舊資料不必刪除,需人工處理)
public class TSEC_EPS {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("D:\\Temp\\GOGOBOX\\stock_order\\程式合併30.58\\TSEC_EPS.csv");
		BufferedReader br = new BufferedReader(fr);
		String s = null;
		br.readLine();
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		
		java.sql.PreparedStatement stmt = null;
		java.sql.ResultSet rs = null;
		String query;
		
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR) + "-1-1";
		
		//刪除資料
		query = "delete from stock_tsec where create_date>='"+dateStr+"'";
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate(query);
		
		while((s=br.readLine())!=null) {
			try{
				s = s.replaceAll("－", "0");
				s = s.replaceAll(",,", ",0,");
				String[] strArr = s.split(",");
				//System.out.println(s);
				
				query = "insert into stock_tsec(stock_id,pe,rate_value,net_value,create_date) values('"+strArr[0]+"','"+strArr[2]+"','"+strArr[3]+"','"+strArr[4]+"',now())";
				System.out.println(query);
				stmt = conn.prepareStatement(query);
				stmt.executeUpdate(query);
				
				stmt.close();
			}
			catch(Exception e1) {System.err.println(e1);}
			
		}
		
	}

}
