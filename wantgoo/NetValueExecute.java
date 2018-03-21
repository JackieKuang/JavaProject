package wantgoo;

import stock5.conn.ConnectionDAO;

/*
 * 股價淨值比、本益比 計算
 */
public class NetValueExecute {

	public static void main(String[] args) throws Exception {
		Log4jUtil log = new Log4jUtil();
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		java.sql.PreparedStatement PS1 = null, PS2=null;
		java.sql.ResultSet RS1 = null;
		
		//查詢所有Net Value股票 stock_id
		String sqlStockId = "SELECT t2.stock_id, t2.stock_name, round(t2.stock_deal/t1.value_per_share,2) net_value, round(t2.stock_deal/t1.income_per_share,2) per_value FROM net_price_cal t1, wantgoo_stock t2 "+
							" where t2.create_date=date(now()) and t1.stock_id=t2.stock_id ";
		PS1 = conn.prepareStatement(sqlStockId);
		RS1 = PS1.executeQuery();
		
		//Update Net Value and PER
		String sqlUpd = "update wantgoo_stock set net_value=?, eps=? where stock_id=? and create_date=date(now())";
		PS2 = conn.prepareStatement(sqlUpd);
		
		while(RS1.next()) {
			String netValue = "0";
			String perValue = "0";
			if(RS1.getString("net_value") != null) {
				netValue = RS1.getString("net_value");
			}
			if(RS1.getString("per_value") != null) {
				perValue = RS1.getString("per_value");
			}
			
			PS2.setString(1, netValue);
			PS2.setString(2, perValue);
			PS2.setString(3, RS1.getString("stock_id"));
			PS2.executeUpdate();
		}
		
		
		if(RS1 != null) { RS1.close(); }
		if(PS1 != null) { PS1.close(); }
		if(PS2 != null) { PS2.close(); }
		if(conn != null) { conn.close(); }
		
		System.err.println("NetValueExecute Finish!!");
	}

}
