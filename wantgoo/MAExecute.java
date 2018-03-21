package wantgoo;

import java.util.Calendar;

import stock5.conn.ConnectionDAO;

/*
 * MA5、MA10、MA30、MA60、MA72 計算
 */
public class MAExecute {

	public static void main(String[] args) throws Exception {
		Log4jUtil log = new Log4jUtil();
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		java.sql.PreparedStatement PS1 = null, PS2=null, PSMA5=null,PSMA10=null,PSMA30=null,PSMA60=null,PSMA72=null,PS_UPD=null;
		java.sql.ResultSet RS1 = null, RS2 = null, RSMA5=null,RSMA10=null,RSMA30=null,RSMA60=null,RSMA72=null;
		
		String MA5 = "0";
		String MA10 = "0";
		String MA30 = "0";
		String MA60 = "0";
		String MA72 = "0";
		
		//查詢所有股票 stock_id
		String sqlStockId = "select stock_id from wantgoo_stock group by stock_id";
		PS1 = conn.prepareStatement(sqlStockId);
		RS1 = PS1.executeQuery();
		
		String sqlStr2 = "select stock_id, stock_deal from wantgoo_stock where stock_id=? order by create_date desc limit 1";
		PS2 = conn.prepareStatement(sqlStr2);
		
		//MA5 SQL
		String sqlMA5 = "select avg(stock_deal) avg_ma5 from ("+
		" select stock_deal from wantgoo_stock where stock_id=? order by create_date desc limit 5 "+
		" ) sw";
		PSMA5 = conn.prepareStatement(sqlMA5);
		
		//MA10 SQL
		String sqlMA10 = "select avg(stock_deal) avg_ma10 from ("+
		" select stock_deal from wantgoo_stock where stock_id=? order by create_date desc limit 10 "+
		" ) sw";
		PSMA10 = conn.prepareStatement(sqlMA10);
		
		//MA30 SQL
		String sqlMA30 = "select avg(stock_deal) avg_ma30 from ("+
		" select stock_deal from wantgoo_stock where stock_id=? order by create_date desc limit 30 "+
		" ) sw";
		PSMA30 = conn.prepareStatement(sqlMA30);
		
		//MA60 SQL
		String sqlMA60 = "select avg(stock_deal) avg_ma60 from ("+
		" select stock_deal from wantgoo_stock where stock_id=? order by create_date desc limit 60 "+
		" ) sw";
		PSMA60 = conn.prepareStatement(sqlMA60);
		
		//MA72 SQL
		String sqlMA72 = "select avg(stock_deal) avg_ma72 from ("+
		" select stock_deal from wantgoo_stock where stock_id=? order by create_date desc limit 72 "+
		" ) sw";
		PSMA72 = conn.prepareStatement(sqlMA72);
		
		//Update Data
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		String sqlUpd = "update wantgoo_stock set ma5=?, ma10=?, ma30=?, ma72=?, ma60=?, mean_60_distance=?, mean_60_distance_rate=? where stock_id=? and create_date='"+dateStr+"'";
		//String sqlUpd = "update wantgoo_stock set ma5=?, ma10=?, ma30=?, ma72=? where stock_id=? and create_date='"+dateStr+"'";
		PS_UPD = conn.prepareStatement(sqlUpd);
		
		while(RS1.next()) {
			try {
				PS2.setString(1, RS1.getString("stock_id"));
				RS2 = PS2.executeQuery();
				RS2.next();
				
				//MA5
				PSMA5.setString(1, RS2.getString("stock_id"));
				RSMA5 = PSMA5.executeQuery();
				if(RSMA5.next()) {
					MA5 = (((int)(RSMA5.getFloat("avg_ma5")*100))/100.0)+"";
				}
				
				//MA10
				PSMA10.setString(1, RS2.getString("stock_id"));
				RSMA10 = PSMA10.executeQuery();
				if(RSMA10.next()) {
					MA10 = (((int)(RSMA10.getFloat("avg_ma10")*100))/100.0)+"";
				}
				
				//MA30
				PSMA30.setString(1, RS2.getString("stock_id"));
				RSMA30 = PSMA30.executeQuery();
				if(RSMA30.next()) {
					MA30 = (((int)(RSMA30.getFloat("avg_ma30")*100))/100.0)+"";
				}
				
				//MA60
				PSMA60.setString(1, RS2.getString("stock_id"));
				RSMA60 = PSMA60.executeQuery();
				if(RSMA60.next()) {
					MA60 = (((int)(RSMA60.getFloat("avg_ma60")*100))/100.0)+"";
				}
				
				//離季線
				float mean_60_distance = (RS2.getFloat("stock_deal") - Float.parseFloat(MA60)) * 100; //成交價-季線位置=離季線
				mean_60_distance = Math.round(mean_60_distance);
				mean_60_distance = mean_60_distance / 100;
				
				//離季線%
				float mean_60_distance_rate = (mean_60_distance / RS2.getFloat("stock_deal")) * 100 * 100; //離季線/股價=離季線%
				mean_60_distance_rate = Math.round(mean_60_distance_rate);
				mean_60_distance_rate = mean_60_distance_rate / 100;
				
				//System.err.println(RS2.getString("stock_id")+":"+mean_60_distance+","+mean_60_distance_rate+","+MA60);
				
				//MA72(資料不足)
				PSMA72.setString(1, RS2.getString("stock_id"));
				RSMA72 = PSMA72.executeQuery();
				if(RSMA72.next()) {
					MA72 = (((int)(RSMA72.getFloat("avg_ma72")*100))/100.0)+"";
				}
				
				//Update
				PS_UPD.setString(1, MA5);
				PS_UPD.setString(2, MA10);
				PS_UPD.setString(3, MA30);
				PS_UPD.setString(4, MA72);
				PS_UPD.setString(5, MA60);
				PS_UPD.setString(6, mean_60_distance+"");
				PS_UPD.setString(7, mean_60_distance_rate+"");
				PS_UPD.setString(8, RS2.getString("stock_id"));
				PS_UPD.executeUpdate();
				
				log.writeInfo("Update MA:"+RS1.getString("stock_id"));
				//System.out.print("Update MA:"+RS1.getString("stock_id")+"\r");
				//System.err.println(RS1.getString("stock_id")+","+MA5+","+MA10+","+MA30+","+MA60+","+MA72);
			}
			catch(Exception ex) {
				log.writeError("Update MA Err:"+RS1.getString("stock_id")+"，errMsg:"+ex.toString());
				//System.err.println("Update MA Err:"+RS1.getString("stock_id")+"，errMsg:"+ex.toString());
			}
		}
		
		if(RS1 != null) { RS1.close(); }
		if(RS2 != null) { RS2.close(); }
		if(RSMA5 != null) { RSMA5.close(); }
		if(RSMA10 != null) { RSMA10.close(); }
		if(RSMA30 != null) { RSMA30.close(); }
		if(RSMA60 != null) { RSMA60.close(); }
		if(RSMA72 != null) { RSMA72.close(); }
		if(PS1 != null) { PS1.close(); }
		if(PS2 != null) { PS2.close(); }
		if(PSMA5 != null) { PSMA5.close(); }
		if(PSMA10 != null) { PSMA10.close(); }
		if(PSMA30 != null) { PSMA30.close(); }
		if(PSMA60 != null) { PSMA60.close(); }
		if(PSMA72 != null) { PSMA72.close(); }
		if(PS_UPD != null) { PS_UPD.close(); }
		if(conn != null) { conn.close(); }
	}

}
