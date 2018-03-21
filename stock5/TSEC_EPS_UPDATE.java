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

//Table Name:calculate_eps (須先更新完stock_tsec-->TSEC_EPS.java)
public class TSEC_EPS_UPDATE {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("D:\\Temp\\GOGOBOX\\stock_order\\程式合併30.58\\TSEC_EPS.csv");
		BufferedReader br = new BufferedReader(fr);
		String s = null;
		br.readLine();
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		
		java.sql.PreparedStatement stmt = null, stmt2 = null, stmt3 = null;
		java.sql.ResultSet rs2 = null;
		String query, query2, query3;
		
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR)+"";
		String dateStr2 = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE);
		
		query2 = "select s2.stock_id,ROUND(s2.stock_deal/s1.pe, 2) eps from stock_tsec s1,stock_now s2 where s2.create_date>='"+dateStr2+"' and s2.stock_id=s1.stock_id";
		stmt2 = conn.prepareStatement(query2);
		//System.out.println(query2);
		rs2 = stmt2.executeQuery();
		
		while(rs2.next()) {
			try{
				String eps = "0";
				if(rs2.getString("eps")!=null) {
					eps = rs2.getString("eps");
				}
				
				//Insert
				try{
					query = "insert into calculate_eps(year,eps_tsec,eps_skis,eps_e_stock,stock_id,create_date) values("+dateStr+",'"+eps+"','"+eps+"','"+eps+"','"+rs2.getString("stock_id")+"',now())";
					System.out.println(query);
					stmt = conn.prepareStatement(query);
					stmt.executeUpdate();
				}
				catch(Exception e1) {System.err.println(e1);}
				finally{if(stmt!=null) {stmt.close();}}
				
				//Update
				try{
					//query3 = "update calculate_eps set eps_tsec='"+eps+"' where stock_id='"+rs2.getString("stock_id")+"' and year="+dateStr;
					//System.out.println(query3);
					//stmt3 = conn.prepareStatement(query3);
					//stmt3.executeUpdate();
				}
				catch(Exception e1) {System.err.println(e1);}
				finally{if(stmt3!=null) {stmt3.close();}}
			}
			catch(Exception e1) {System.err.println(e1);}
			
		}
		System.err.println("FINISH!!");
		
	}

}
