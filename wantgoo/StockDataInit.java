package wantgoo;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

import stock5.conn.ConnectionDAO;

public class StockDataInit {

	public static void main(String[] args) throws Exception {

		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		java.sql.PreparedStatement PS1 = null;
		
		String sqlStrInit = "insert into stock.wantgoo_stock(stock_id, stock_deal, deal_change, deal_open, deal_percentage, deal_buy, deal_high, deal_sell, deal_low, total_volume, deal_last, mean_60_distance, mean_60_distance_rate, EPS, price_volume, no_new_high_low_dates, rank_duration, create_date, stock_name, ma60, stock_sum, ma5, ma10, ma30, ma72, net_value, stock_sum_flag) "+ 
				" select stock_id, 0, 0, 0, 0, 0, 0, 0, 0, 0, stock_deal, mean_60_distance, mean_60_distance_rate, EPS, 0, 0, 0, now(), stock_name, ma60, 0, ma5, ma10, ma30, ma72, net_value, 0 from stock.wantgoo_stock "+
				" where create_date=(select max(create_date) create_date from stock.wantgoo_stock)";
		PS1 = conn.prepareStatement(sqlStrInit);
		PS1.executeUpdate();
		
		System.err.println("StockDataInit OK!!");
		
		if(PS1 != null) { PS1.close(); }
		if(conn != null) { conn.close(); }
	}
	
	
}
