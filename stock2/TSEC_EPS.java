package stock2;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;
import com.mysql.jdbc.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.*;


//個股日本益比、殖利率及股價淨值比(TSEC_EPS.csv)
//檔名:stock_tsec (每天跑一次,舊資料不必刪除)
public class TSEC_EPS {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("D:\\Temp\\GOGOBOX\\stock_order\\程式合併21\\原始\\分析資料\\計算個股EPS(證交所)\\TSEC_EPS.csv");
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
				s = s.replaceAll(",,", ",-,");
				s = s.replaceAll(",,", ",-,");
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
