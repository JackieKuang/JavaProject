package stock5;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;

import stock5.conn.ConnectionDAO;

import com.mysql.jdbc.*;
import java.sql.*;
import java.util.*;
import java.text.*;

//上市櫃指數 (http://mis.tse.com.tw/data/TSEIndex.csv、http://mis.tse.com.tw/data/OTCIndex.csv) (MIS)
// http://mis.tse.com.tw/data/
//Table Name:stock_exponent (每天跑一次,舊資料不必刪除)
public class CatchExponentMIS {
	
	public static void main(String[] args) throws Exception {
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		java.sql.PreparedStatement stmt = null,stmt2 = null,stmt3 = null;
		java.sql.ResultSet rs = null,rs2 = null,rs3 = null;
		String query,query2,query3;
		
		DecimalFormat CURRENCY_FORMAT = new DecimalFormat("0.00%");
		DecimalFormat CURRENCY_FORMAT2 = new DecimalFormat("#####.##");
		
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		
		query = "SELECT max(create_date) now_date FROM stock_now";
		stmt = conn.prepareStatement(query);
		rs = stmt.executeQuery();
		rs.next();
		String now_date = rs.getString("now_date");
		
		//大盤指數
		ReaderURL ru = new ReaderURL();
		String strAll = ru.readTextFile("http://mis.tse.com.tw/data/TSEIndex.csv?id="+dateStr);
		strAll = strAll.replaceAll("\",\"","@");
		strAll = strAll.replaceAll("\"","");
		//System.out.println(strAll);
		
		String[] stock_data = strAll.split("\r\n");
		String market_exp = "";
		String market_deal = "";
		String market_diff = "";
		
		for(int j=0; j<stock_data.length; j++) {
			//System.out.println(stock_data[j]);
			if(j == 1) {
				String[] temp = stock_data[j].split("@");
				market_exp = temp[2];
				market_diff = temp[3];
			}
			if(j == 49) {
				String[] temp = stock_data[j].split("@");
				market_deal = CURRENCY_FORMAT2.format(Float.parseFloat(temp[2].replaceAll(",",""))/100000000);
			}
		}
		
		//上櫃指數
		ru = new ReaderURL();
		strAll = ru.readTextFile("http://mis.tse.com.tw/data/OTCIndex.csv?id="+dateStr);
		strAll = strAll.replaceAll("\",\"","@");
		strAll = strAll.replaceAll("\"","");
		//System.out.println(strAll);
		
		stock_data = strAll.split("\r\n");
		String otc_exp = "";
		String otc_deal = "";
		String otc_diff = "";
		
		for(int j=0; j<stock_data.length; j++) {
			//System.out.println(stock_data[j]);
			if(j == 1) {
				String[] temp = stock_data[j].split("@");
				otc_exp = temp[2];
				otc_diff = temp[3];
			}
			if(j == 26) {
				String[] temp = stock_data[j].split("@");
				otc_deal = CURRENCY_FORMAT2.format(Float.parseFloat(temp[2].replaceAll(",",""))/100000000);
			}
		}
		
		//刪除資料
		query = "delete from stock_exponent where create_date='"+dateStr+"'";
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate();
		
		//Insert Data
		query = "insert into stock_exponent(market_exp,market_diff,market_deal,otc_exp,otc_diff,otc_deal,create_date)"+
				" values('"+market_exp+"','"+market_diff+"','"+market_deal+"','"+otc_exp+"','"+otc_diff+"','"+otc_deal+"',now())";
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate();
		
		//找出前一日資料
		//查詢最新的上一次股價日期
		String now_date_bef = now_date;	//預設為當日
		query3 = "SELECT create_date FROM stock_exponent group by create_date";
		stmt3 = conn.prepareStatement(query3,java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
		rs3 = stmt3.executeQuery();
		if(rs3.next()) {
			rs3.last();
			rs3.absolute(rs3.getRow()-1);
			now_date_bef = rs3.getString("create_date");
		}
		
		//cal.set(Calendar.DATE,cal.get(Calendar.DATE)-1);
		//String dateStr2 = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		query2 = "select * from stock_exponent where create_date='"+now_date_bef+"'";
		stmt2 = conn.prepareStatement(query2);
		rs2 = stmt2.executeQuery();
		
		float market_exp_f = 0;
		float otc_exp_f = 0;
		
		if(rs2.next()) {
			market_exp_f = (Float.parseFloat(market_exp) - rs2.getFloat("market_exp")) / rs2.getFloat("market_exp");
			otc_exp_f = (Float.parseFloat(otc_exp) - rs2.getFloat("otc_exp")) / rs2.getFloat("otc_exp");
		}
		
		//Update 大盤漲跌%、OTC漲跌%
		query = "update stock_exponent set market_rate='"+CURRENCY_FORMAT.format(market_exp_f)+"',otc_rate='"+CURRENCY_FORMAT.format(otc_exp_f)+"' where create_date='"+dateStr+"'";
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate();
		
		System.out.print(market_exp_f+","+otc_exp_f+"\r");
		System.out.println("");
		
	}
	
}
