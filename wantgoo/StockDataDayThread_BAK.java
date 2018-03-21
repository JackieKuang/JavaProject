package wantgoo;

import java.util.Calendar;

import stock5.conn.ConnectionDAO;

//即時股價處理
//股票資料(http://www.wantgoo.com/CorpInfom.aspx?StockNo=6244) (玩股網)
public class StockDataDayThread_BAK extends Thread {
	String stockNum = "";
	
	public StockDataDayThread_BAK(String s) {
		stockNum = s;
	}
	
	public String getDateTime() {
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		String timeStr = cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
		
		return dateStr+"_"+timeStr;
	}
	
	public String getDate() {
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		
		return dateStr;
	}
	
	public int getHOUR_OF_DAY() {
		Calendar cal = Calendar.getInstance();
		
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	public int getMINUTE() {
		Calendar cal = Calendar.getInstance();
		
		return cal.get(Calendar.MINUTE);
	}
	
	public void run() {
		/***** 參數設定 *****/
		int sleepTime = Integer.parseInt(GetProperty.readValue("runThread_StockSleepTime")); //單位：秒
		/***** 參數設定 *****/
		
		java.sql.Connection conn = null;
		java.sql.PreparedStatement PS1 = null, PS2=null, PS_SEL=null, PS_UPD=null;
		java.sql.ResultSet RS1 = null, RS_SEL=null;
		int errCount = 0;
		
		try {
			
			String[] dataArr = new String[19];
			while(true) {
				try {
					if(errCount>Integer.parseInt(GetProperty.readValue("runThread_errCount"))) {
						System.err.println("[errCount]錯誤股碼:"+stockNum+"，errCount:"+errCount+"，Date:"+getDateTime());
						break;
					}
					
					//尚未開盤時間 continue
					if(getHOUR_OF_DAY()<Integer.parseInt(GetProperty.readValue("runThread_StartTimeHour")) ||
							(getHOUR_OF_DAY()==Integer.parseInt(GetProperty.readValue("runThread_StartTimeHour")) && getMINUTE()<Integer.parseInt(GetProperty.readValue("runThread_StartTimeMinute")))) {
						System.err.println("[尚未開盤]股碼:"+stockNum+"，Date:"+getDateTime());
						Thread.sleep(sleepTime*1000);
						continue;
					}
					
					//收盤時間結束 break
					if(getHOUR_OF_DAY()>Integer.parseInt(GetProperty.readValue("runThread_EndTimeHour")) ||
							getHOUR_OF_DAY()==Integer.parseInt(GetProperty.readValue("runThread_EndTimeHour")) && getMINUTE()>Integer.parseInt(GetProperty.readValue("runThread_EndTimeMinute"))) {
						System.err.println("[收盤]股碼:"+stockNum+"，Date:"+getDateTime());
						break;
					}
					
					conn = ConnectionDAO.getConnection();
					
					String query = "insert into wantgoo_stock(stock_id,stock_deal,deal_change,deal_open,deal_percentage," +
					"deal_buy,deal_high,deal_sell,deal_low,total_volume,deal_last," +
					"mean_60_distance,mean_60_distance_rate,EPS,price_volume,no_new_high_low_dates," +
					"rank_duration,create_date,stock_name,ma60,stock_sum) "+
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,now(),?,?,?)";
					PS1 = conn.prepareStatement(query);
					
					//***** 檢查當日資料 *****
					String sqlSel = "select stock_id from wantgoo_stock where create_date='"+getDate()+"' and stock_id=?";
					PS_SEL = conn.prepareStatement(sqlSel);
					//***** 檢查當日資料 *****
					
					//***** 更新當日資料 *****
					String sqlUpd = "update wantgoo_stock set stock_deal=?,deal_change=?,deal_open=?," +
							"deal_percentage=?,deal_buy=?,deal_high=?,deal_sell=?,deal_low=?,total_volume=?,deal_last=?," +
							"mean_60_distance=?,mean_60_distance_rate=?,EPS=?,price_volume=?,no_new_high_low_dates=?," +
							"rank_duration=?,stock_name=?,ma60=?,stock_sum=? where stock_id=? and create_date='"+getDate()+"'";
					PS_UPD = conn.prepareStatement(sqlUpd);
					//***** 更新當日資料 *****
					
					//Get URL Content
					ReaderURL ru = new ReaderURL();
					String strAll = ru.readTextFile("http://www.wantgoo.com/CorpInfom.aspx?StockNo="+stockNum);
					strAll = strAll.replaceAll("Red","");
					strAll = strAll.replaceAll("Black","");
					strAll = strAll.replaceAll("Green","");
					strAll = strAll.replaceAll("background-color:#FFC0C0;","");
					strAll = strAll.replaceAll("background-color:#C0FFC0;","");
					
					
					//成交價
					String replaceStrS = "<span id=\"ctl00_service_FormView1_lblDeal\" style=\"color:;\">";
					String replaceStrE = "</span></td>";
					String strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[0] = strSP;
					
					//漲跌
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblChange\" style=\"color:;\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[1] = strSP;
					
					//開盤價
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblOpen\" style=\"color:;\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[2] = strSP;
					
					//漲幅
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblPercentage\" style=\"color:;\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					strSP = strSP.replaceAll("%","");
					dataArr[3] = strSP;
					
					//買進價
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblBuy\" style=\"color:;\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[4] = strSP;
					
					//最高價
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblHigh\" style=\"color:;\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[5] = strSP;
					
					//賣出價
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblSell\" style=\"color:;\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[6] = strSP;
					
					//最低價
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblLow\" style=\"color:;\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[7] = strSP;
					
					//成交量
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblTotalVolume\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					strSP = strSP.replaceAll(",","");
					dataArr[8] = strSP;
					
					//昨收價
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblLast\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[9] = strSP;
					
					//離季線
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblMean60Distance\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[10] = strSP;
					
					//離季線%
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblMean60DistanceRate\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					strSP = strSP.replaceAll("%","");
					dataArr[11] = strSP;
					
					//本益比
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblEPS\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[12] = strSP;
					
					//價量分數
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblPriceVolume\">";
					replaceStrE = "</span></td>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[13] = strSP;
					
					//幾日未創新高/低
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblNoNewHighLowDates\">";
					replaceStrE = "</span>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[14] = strSP;
					
					//連續多頭排列天數
					replaceStrS = "<span id=\"ctl00_service_FormView1_lblRankDuration\">";
					replaceStrE = "</span>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[15] = strSP;
					
					//股名
					replaceStrS = "<span id=\"ctl00_service_FormView1_Name\">";
					replaceStrE = "</span>";
					strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
					strSP = strSP.replaceAll(replaceStrS,"");
					dataArr[16] = strSP;
					
					//MA60
					dataArr[17] = (Float.parseFloat(dataArr[0]) - Float.parseFloat(dataArr[10])) + "";
					
					//三大法人買賣(單位：張) - 抓取當天
					try {
						replaceStrS = "<span id=\"ctl00_service_gvStock_ctl02_lblSum\" class=\"number\" style=\"color:;\">";
						replaceStrE = "</span></td>";
						strSP = strAll.substring(strAll.indexOf(replaceStrS),strAll.indexOf(replaceStrE,strAll.indexOf(replaceStrS)));
						strSP = strSP.replaceAll(replaceStrS,"");
						strSP = strSP.replaceAll(",","");
						dataArr[18] = strSP;
					}
					catch(Exception ex) {
						dataArr[18] = "0";
					}
					
					PS_SEL.setString(1,stockNum);
					RS_SEL = PS_SEL.executeQuery();
					
					if(RS_SEL.next()) {
						//Update Data
						
						PS_UPD.setString(1,dataArr[0]);
						PS_UPD.setString(2,dataArr[1]);
						PS_UPD.setString(3,dataArr[2]);
						PS_UPD.setString(4,dataArr[3]);
						PS_UPD.setString(5,dataArr[4]);
						PS_UPD.setString(6,dataArr[5]);
						PS_UPD.setString(7,dataArr[6]);
						PS_UPD.setString(8,dataArr[7]);
						PS_UPD.setString(9,dataArr[8]);
						PS_UPD.setString(10,dataArr[9]);
						PS_UPD.setString(11,dataArr[10]);
						PS_UPD.setString(12,dataArr[11]);
						PS_UPD.setString(13,dataArr[12]);
						PS_UPD.setString(14,dataArr[13]);
						PS_UPD.setString(15,dataArr[14]);
						PS_UPD.setString(16,dataArr[15]);
						PS_UPD.setString(17,dataArr[16]);
						PS_UPD.setString(18,dataArr[17]);
						PS_UPD.setString(19,dataArr[18]);
						PS_UPD.setString(20,stockNum);
						
						PS_UPD.executeUpdate();
					}
					else {
						//Insert Data
						PS1.setString(1,stockNum);
						PS1.setString(2,dataArr[0]);
						PS1.setString(3,dataArr[1]);
						PS1.setString(4,dataArr[2]);
						PS1.setString(5,dataArr[3]);
						PS1.setString(6,dataArr[4]);
						PS1.setString(7,dataArr[5]);
						PS1.setString(8,dataArr[6]);
						PS1.setString(9,dataArr[7]);
						PS1.setString(10,dataArr[8]);
						PS1.setString(11,dataArr[9]);
						PS1.setString(12,dataArr[10]);
						PS1.setString(13,dataArr[11]);
						PS1.setString(14,dataArr[12]);
						PS1.setString(15,dataArr[13]);
						PS1.setString(16,dataArr[14]);
						PS1.setString(17,dataArr[15]);
						PS1.setString(18,dataArr[16]);
						PS1.setString(19,dataArr[17]);
						PS1.setString(20,dataArr[18]);
						
						PS1.executeUpdate();
					}
					
				}
				catch(Exception ex) {
					
					System.err.println("[Thread]錯誤股碼:"+stockNum+"["+errCount+"]，ErrMsg:"+ex.toString()+"，Date:"+getDateTime());
					String errMsg = ex.toString();
					if(errMsg.indexOf("Connection reset")>=0) {
						Thread.sleep(60*1000);
						continue;
					}
					errCount++;
				}
				finally {
					if(PS_SEL != null) { PS_SEL.close(); }
					if(PS_UPD != null) { PS_UPD.close(); }
					if(RS_SEL != null) { RS_SEL.close(); }
					if(RS1 != null) { RS1.close(); }
					if(PS1 != null) { PS1.close(); }
					if(PS2 != null) { PS2.close(); }
					if(conn != null) { conn.close(); }
				}
				
				Thread.sleep(sleepTime*1000);
			}
			
		}
		catch(Exception e1) {
			System.err.println("[PG Break]錯誤股碼:"+stockNum+"，ErrMsg:"+e1.toString()+"，Date:"+getDateTime());
		}
	}
}
