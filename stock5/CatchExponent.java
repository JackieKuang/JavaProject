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

//上市櫃指數 (http://pchome.syspower.com.tw/) (PCHome)
//Table Name:stock_exponent (每天跑一次,舊資料不必刪除)
public class CatchExponent {
	
	public static void main(String[] args) throws Exception {
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		
		java.sql.PreparedStatement stmt = null,stmt2 = null,stmt3 = null;
		java.sql.ResultSet rs = null,rs2 = null,rs3 = null;
		String query,query2,query3;
		
		DecimalFormat CURRENCY_FORMAT = new DecimalFormat("0.00%");
		
		query = "SELECT max(create_date) now_date FROM stock_now";
		stmt = conn.prepareStatement(query);
		rs = stmt.executeQuery();
		rs.next();
		String now_date = rs.getString("now_date");
		
		ReaderURL ru = new ReaderURL();
		String strAll = ru.readTextFile2("http://pchome.syspower.com.tw/","UTF-8");
		
		String[] exponent = new String[6];
		
		//大盤指數
		String spStr_deal = "";
		String spStr_diff = "";
		String spStr_mon = "";
		
		String strSP = strAll.substring(strAll.indexOf("<!--上市-->"),strAll.indexOf("<!--上櫃-->"));
		//System.err.println(strSP);
		
		if(strSP.indexOf("<span class=\"idx32\">")!=-1) {
			//漲
			spStr_deal = "<span class=\"idx32\">";
			spStr_diff = "<span class=\"t15r\">";
			spStr_mon = "<span class=\"t15b\">";
		}
		else {	
			//跌
			spStr_deal = "<span class=\"idx34\">";
			spStr_diff = "<span class=\"t15g\">";
			spStr_mon = "<span class=\"t15b\">";
		}
		
		int len1 = strSP.indexOf(spStr_deal);
		int len2 = spStr_deal.length();
		int len_start = len1+len2;
		int len_end = strSP.indexOf("</span>",len_start);
		
		exponent[0] = strSP.substring(len_start,len_end);
		exponent[0] = exponent[0].replaceAll(" ","");
		
		len1 = strSP.indexOf(spStr_diff);
		len2 = spStr_diff.length();
		len_start = len1+len2;
		len_end = strSP.indexOf("</span>",len_start);
		
		exponent[1] = strSP.substring(len_start,len_end);
		exponent[1] = exponent[1].replaceAll(" ","");
		exponent[1] = exponent[1].replaceAll("▲","");
		exponent[1] = exponent[1].replaceAll("▼","");
		
		
		len1 = strSP.indexOf(spStr_mon);
		len2 = spStr_mon.length();
		len_start = len1+len2;
		len_end = strSP.indexOf("</span>",len_start);
		
		exponent[2] = strSP.substring(len_start,len_end);
		exponent[2] = exponent[2].replaceAll(" ","");
		exponent[2] = exponent[2].replaceAll(",","");
		
		System.err.println(exponent[0]+","+exponent[1]+","+exponent[2]);
		
		//上櫃指數
		spStr_deal = "";
		spStr_diff = "";
		spStr_mon = "";
		
		strSP = strAll.substring(strAll.indexOf("<!--上櫃-->"),strAll.indexOf("<!--股市即時排行 結束-->"));
		//System.err.println(strSP);
		
		if(strSP.indexOf("<span class=\"idx32\">")!=-1) {
			//漲
			spStr_deal = "<span class=\"idx32\">";
			spStr_diff = "<span class=\"t15r\">";
			spStr_mon = "<span class=\"t15b\">";
		}
		else {	
			//跌
			spStr_deal = "<span class=\"idx34\">";
			spStr_diff = "<span class=\"t15g\">";
			spStr_mon = "<span class=\"t15b\">";
		}
		
		len1 = strSP.indexOf(spStr_deal);
		len2 = spStr_deal.length();
		len_start = len1+len2;
		len_end = strSP.indexOf("</span>",len_start);
		
		exponent[3] = strSP.substring(len_start,len_end);
		exponent[3] = exponent[3].replaceAll(" ","");
		
		len1 = strSP.indexOf(spStr_diff);
		len2 = spStr_diff.length();
		len_start = len1+len2;
		len_end = strSP.indexOf("</span>",len_start);
		
		exponent[4] = strSP.substring(len_start,len_end);
		exponent[4] = exponent[4].replaceAll(" ","");
		exponent[4] = exponent[4].replaceAll("▲","");
		exponent[4] = exponent[4].replaceAll("▼","");
		
		len1 = strSP.indexOf(spStr_mon);
		len2 = spStr_mon.length();
		len_start = len1+len2;
		len_end = strSP.indexOf("</span>",len_start);
		
		exponent[5] = strSP.substring(len_start,len_end);
		exponent[5] = exponent[5].replaceAll(" ","");
		exponent[5] = exponent[5].replaceAll(",","");
		
		System.err.println(exponent[3]+","+exponent[4]+","+exponent[5]);
		
		//刪除資料
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		
		query = "delete from stock_exponent where create_date='"+dateStr+"'";
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate();
		
		//Insert Data
		query = "insert into stock_exponent(market_exp,market_diff,market_deal,otc_exp,otc_diff,otc_deal,create_date)"+
				" values('"+exponent[0]+"','"+exponent[1]+"','"+exponent[2]+"','"+exponent[3]+"','"+exponent[4]+"','"+exponent[5]+"',now())";
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate();
		
		
		//Update stock_diff + to ""
		query = "update stock_exponent set market_diff=replace(market_diff,'+',''), otc_diff=replace(otc_diff,'+','')";
		stmt2 = conn.prepareStatement(query);
		stmt2.executeUpdate();
		
		
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
		
		float market_exp = 0;
		float otc_exp = 0;
		
		if(rs2.next()) {
			market_exp = (Float.parseFloat(exponent[0]) - rs2.getFloat("market_exp")) / rs2.getFloat("market_exp");
			otc_exp = (Float.parseFloat(exponent[3]) - rs2.getFloat("otc_exp")) / rs2.getFloat("otc_exp");
		}
		
		//Update 大盤漲跌%、OTC漲跌%
		query = "update stock_exponent set market_rate='"+CURRENCY_FORMAT.format(market_exp)+"',otc_rate='"+CURRENCY_FORMAT.format(otc_exp)+"' where create_date='"+dateStr+"'";
		stmt = conn.prepareStatement(query);
		stmt.executeUpdate();
		
		System.out.println("OK");
		
	}
	
}
