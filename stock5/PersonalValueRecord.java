package stock5;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Calendar;
import stock5.conn.ConnectionDAO;

//更新自選股漲跌幅
public class PersonalValueRecord {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		DecimalFormat CURRENCY_FORMAT = new DecimalFormat("0.00%");
		
		ConnectionDAO connDAO = new ConnectionDAO();

		Connection conn = null;
		java.sql.PreparedStatement stmt = null,stmt2 = null,stmt3 = null;
		java.sql.ResultSet rs = null,rs2 = null,rs3 = null;
		String query,query2,query3;
		
		conn = connDAO.getConnection();
		
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		
		query = "SELECT create_date FROM stock_now group by create_date order by create_date desc limit 0,2";
		stmt = conn.prepareStatement(query,java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
		rs = stmt.executeQuery();
		rs.next();
		String now_date = rs.getString("create_date");//第1筆為當日日期
		
		//找出前一日資料
		rs.next();
		String dateStr2 = rs.getString("create_date"); //第2筆為前一日日期
		
		query = "SELECT m_id,f_num FROM personal_stock_save group by m_id,f_num";
		stmt = conn.prepareStatement(query);
		rs = stmt.executeQuery();
		
		while(rs.next()) {
			query2 = "select * from personal_value_record where m_id='"+rs.getString("m_id")+"' and f_num='"+rs.getString("f_num")+"' and create_date='"+dateStr+"'";
			stmt2 = conn.prepareStatement(query2);
			rs2 = stmt2.executeQuery();
			
			//先刪除舊資料
			if(rs2.next()) {
				query3 = "delete from personal_value_record where m_id='"+rs.getString("m_id")+"' and f_num='"+rs.getString("f_num")+"' and create_date='"+dateStr+"'";
				stmt3 = conn.prepareStatement(query3);
				stmt3.executeUpdate();
			}
			
//			//計算股票總市值
//			query2 = "select p1.*,s1.stock_name,s1.stock_deal,s1.stock_diff from personal_stock_save p1,stock_now s1 where p1.f_num="+rs.getString("f_num")+" and s1.stock_id=p1.stock_id and p1.m_id='"+rs.getString("m_id")+"' and s1.create_date='"+now_date+"'";
//			stmt2 = conn.prepareStatement(query2,java.sql.ResultSet.TYPE_FORWARD_ONLY, java.sql.ResultSet.CONCUR_READ_ONLY);
//			rs2 = stmt2.executeQuery();
//			
//			float overall_money = 0;
//			float stock_deal = 0;
//			while(rs2.next()) {
//				//股票總市值累計
//				if(!"－－".equals(rs2.getString("stock_deal"))) {
//					stock_deal = rs2.getFloat("stock_deal");
//				}
//				overall_money += stock_deal*rs2.getFloat("buy_number");
//			}
			
			//找出今日購買之股票市值
			query3 = "select * from personal_deal_details_item where m_id='"+rs.getString("m_id")+"' and f_num='"+rs.getString("f_num")+"' and buy_date_p='"+dateStr+"'";
			stmt3 = conn.prepareStatement(query3);
			rs3 = stmt3.executeQuery();
			int value_bef = 0;
			int buy_money_today = 0;
			while(rs3.next()) {
				
				query2 = "select stock_deal from stock_now where stock_id="+rs3.getString("stock_id")+" and create_date='"+dateStr+"'";
				stmt2 = conn.prepareStatement(query2);
				rs2 = stmt2.executeQuery();
				float stock_deal_2 = 0;
				if(rs2.next()) {
					stock_deal_2 = rs2.getFloat("stock_deal");
				}
				
				if("1".equals(rs3.getString("type"))) {
					value_bef += stock_deal_2*rs3.getFloat("buy_number_p");
					buy_money_today += rs3.getFloat("buy_price_p")*rs3.getFloat("buy_number_p");
				}
				else if("2".equals(rs3.getString("type"))){
					value_bef -= stock_deal_2*rs3.getFloat("buy_number_p");
					buy_money_today -= rs3.getFloat("buy_price_p")*rs3.getFloat("buy_number_p");
				}
				
			}
			
			//第一次計算獲利%
			query3 = "select p1.*,s1.stock_name,s1.stock_deal,s1.stock_diff from personal_stock_save p1,stock_now s1 where p1.f_num="+rs.getString("f_num")+" and s1.stock_id=p1.stock_id and p1.m_id='"+rs.getString("m_id")+"' and s1.create_date='"+now_date+"'";
			stmt3 = conn.prepareStatement(query3);
			rs3 = stmt3.executeQuery();
			
			float total_profits = 0;
			float total_profits_money = 0;
			int overall_money = 0;
			float p_rate_tod_temp = 0;
			int p_rate_tod_temp_count = 0;
			for(int i=0; rs3.next(); i++) {
				
				float stock_deal_f = 1;
				if(rs3.getString("stock_deal").indexOf("－－")==-1) {
					stock_deal_f = rs3.getFloat("stock_deal");
				}
				
				float stock_diff_f = 0;
				if(rs3.getString("stock_diff").indexOf("－－")==-1) {
					stock_diff_f = rs3.getFloat("stock_diff");
				}
				
				//總股票投資額
				total_profits_money += rs3.getFloat("buy_price")*rs3.getFloat("buy_number");
				
				//損益($)
				total_profits += stock_deal_f*rs3.getFloat("buy_number")-rs3.getFloat("buy_price")*rs3.getFloat("buy_number");
				
				//股票總市值
				overall_money += stock_deal_f*rs3.getFloat("buy_number");
				
			}
			
			query2 = "select * from personal_value_record where m_id='"+rs.getString("m_id")+"' and f_num='"+rs.getString("f_num")+"' and create_date='"+dateStr2+"'";
			stmt2 = conn.prepareStatement(query2);
			rs2 = stmt2.executeQuery();
			
			float p_rate = 0;
			if(total_profits_money!=0) {
				p_rate = total_profits/total_profits_money;
			}
			int p_int = ((int)overall_money);
			int p_int_today = ((int)overall_money);
			int p_rem = (int)total_profits;
			if(rs2.next()) {
				p_rem = p_int-value_bef-rs2.getInt("p_value");
				p_rate = p_rem/rs2.getFloat("p_value");
				p_int_today = p_int-value_bef+buy_money_today;
			}
			
			if(p_int > 0) {
				query3 = "insert into personal_value_record(m_id,f_num,p_value,p_rate,p_rem,create_date)"+
				" values('"+rs.getString("m_id")+"','"+rs.getString("f_num")+"','"+p_int_today+"','"+CURRENCY_FORMAT.format(p_rate)+"','"+p_rem+"',now())";
				stmt3 = conn.prepareStatement(query3);
				stmt3.executeUpdate();
			}
			
			//System.out.println(p_int+","+CURRENCY_FORMAT.format(p_rate));
			
			System.out.print(rs.getString("m_id")+":"+rs.getString("f_num")+":"+p_int_today+","+CURRENCY_FORMAT.format(p_rate)+"\r");
		}
		
		System.out.println("");
	}

}
