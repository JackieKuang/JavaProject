package wantgoo;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Hashtable;
import stock5.conn.ConnectionDAO;

/*
 * 選股程式
 * ********** 參數設定 **********
 *    查詢天數資料
 * (V)離季線
 *    離季線%區間
 * (△)突破季線(需和"離季線"一同使用)
 *    股價區間
 *    本益比(EPS)區間
 *    價量分數區間
 * (V)爆量(五日均量(張))
 * (△)三大法人買賣張數區間
 *    營收上月比較增減
 * (V)營收去年同月增減
 *    營收前期比較增減
 *    五年平均殖利率設定
 *    估算股價高低點設定(股價低估)
 * ********** 參數設定 **********
 * 
 * ********** 設定技巧 **********
 * 
 * 1.找出季線下爆量個股
 * mean_60_distance_H = 0 (低於離季線)
 * volume5Flag = true (爆量(五日均量(張)))
 * revenue_year_diffFlag = true (營收去年同月增減)
 * 
 * ********** 設定技巧 **********
 * 
 */
public class SmartSearch {

	public static void main(String[] args) throws Exception {
		Log4jUtil log = new Log4jUtil();
		NumberFormat formatter = new DecimalFormat("00");
		
		Calendar cal = Calendar.getInstance();
		String dateStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		
		String filePath = GetProperty.readValue("filePath");
		String fileName = "jnl-stock_"+dateStr+"-"+formatter.format(cal.get(Calendar.HOUR_OF_DAY))+"."+formatter.format(cal.get(Calendar.MINUTE))+"."+formatter.format(cal.get(Calendar.SECOND))+".csv";
		if(args.length != 0 && "1".equals(args[0])) {
			fileName = "jnl-stock_"+dateStr+"-"+formatter.format(cal.get(Calendar.HOUR_OF_DAY))+"."+formatter.format(cal.get(Calendar.MINUTE))+"."+formatter.format(cal.get(Calendar.SECOND))+"_Final.csv";
		}
		
		String allPath = filePath+fileName;
		WriteFile wf = new WriteFile(allPath,"BIG5");
		
		/********** 參數設定 **********/
		
			/********* 設定資料 *******/
			int year = Integer.parseInt(GetProperty.readValue("year")); //營收年設定 (預設:2011)
			int month = Integer.parseInt(GetProperty.readValue("month")); //營收月設定 (預設:4)
			String yieldsYearSetup = GetProperty.readValue("yieldsYearSetup"); //殖利率年度設定(五均) (預設:99,98,97,96,95)
			/********* 設定資料 *******/
		
		//查詢天數資料
		int bDate = Integer.parseInt(GetProperty.readValue("bDate")); //0:當天、-1:前1天...以此類推 (預設:0)
		
		
		//離季線(-1:不啟用)
		float mean_60_distance_L = Float.parseFloat(GetProperty.readValue("mean_60_distance_L")); //(預設:-1)
		float mean_60_distance_H = Float.parseFloat(GetProperty.readValue("mean_60_distance_H")); //(預設:-1)
		
		//突破季線(需和"離季線"一同使用)
		boolean mean_60Flag = Boolean.parseBoolean(GetProperty.readValue("mean_60Flag")); //false:不啟用、true:啟用 (預設:false)
		
		//離季線%區間(-1:不啟用)
		float mean_60_distance_rate_L = Float.parseFloat(GetProperty.readValue("mean_60_distance_rate_L")); //(預設:-1)
		float mean_60_distance_rate_H = Float.parseFloat(GetProperty.readValue("mean_60_distance_rate_H")); //(預設:-1)
		
		//股價區間(-1:不啟用)
		float stock_deal_L = Float.parseFloat(GetProperty.readValue("stock_deal_L")); //(預設:-1)
		float stock_deal_H = Float.parseFloat(GetProperty.readValue("stock_deal_H")); //(預設:-1)
		
		//本益比(EPS)區間(-1:不啟用)
		float EPS_L = Float.parseFloat(GetProperty.readValue("EPS_L")); //(預設:-1)
		float EPS_H = Float.parseFloat(GetProperty.readValue("EPS_H")); //(預設:-1)
		
		//價量分數區間(-40~40)，(-1:不啟用)
		float price_volume_L = Float.parseFloat(GetProperty.readValue("price_volume_L")); //(預設:-1)
		float price_volume_H = Float.parseFloat(GetProperty.readValue("price_volume_H")); //(預設:-1)
		
		//爆量(五日均量(張))開關
		boolean volume5Flag = Boolean.parseBoolean(GetProperty.readValue("volume5Flag")); //false:不啟用、true:啟用(含以下設定) (預設:true)
		float volume5Mu = Float.parseFloat(GetProperty.readValue("volume5Mu")); //爆量倍數(建議2倍) (預設:2)
		int volumeLimit = Integer.parseInt(GetProperty.readValue("volumeLimit")); //爆量張數(建議1000張以上) (預設:1000)
		float volumeUp = Float.parseFloat(GetProperty.readValue("volumeUp"));	//爆量股價上漲%數(建議上漲3%以上) (預設:3)
		int volumeAvgDay = Integer.parseInt(GetProperty.readValue("volumeAvgDay")); //均日天數(建議5日) (預設:5)
		int volume5LimitL = Integer.parseInt(GetProperty.readValue("volume5LimitL")); //volumeAvgDay均量(張)下限(建議1000張以上)，(-1:不啟用) (預設:1000)
		int volume5LimitH = Integer.parseInt(GetProperty.readValue("volume5LimitH")); //volumeAvgDay均量(張)上限，(-1:不啟用) (預設:-1)
		
		//三大法人買賣張數區間(-1:不啟用)
		float stock_sum_L = Float.parseFloat(GetProperty.readValue("stock_sum_L")); //(預設:-1)
		float stock_sum_H = Float.parseFloat(GetProperty.readValue("stock_sum_H")); //(預設:-1)
		
		//三大法人買超天數(-1:不啟用)
		int stock_sum_day = Integer.parseInt(GetProperty.readValue("stock_sum_day")); //(預設:-1)
		
		//營收上月比較增減
		boolean revenue_diffFlag = Boolean.parseBoolean(GetProperty.readValue("revenue_diffFlag")); //false:不啟用、true:啟用(含以下設定) (預設:false)
		float revenue_diff_per = Float.parseFloat(GetProperty.readValue("revenue_diff_per")); //(單位:%) (預設:0)
		
		//營收去年同月增減
		boolean revenue_year_diffFlag = Boolean.parseBoolean(GetProperty.readValue("revenue_year_diffFlag")); //false:不啟用、true:啟用(含以下設定) (預設:true)
		float revenue_year_diff_per = Float.parseFloat(GetProperty.readValue("revenue_year_diff_per")); //(單位:%) (預設:0)
		
		//營收前期比較增減
		boolean total_revenue_diffFlag = Boolean.parseBoolean(GetProperty.readValue("total_revenue_diffFlag")); //false:不啟用、true:啟用(含以下設定) (預設:false)
		float total_revenue_diff_per = Float.parseFloat(GetProperty.readValue("total_revenue_diff_per")); //(單位:%) (預設:0)
		
		//五年平均殖利率設定
		float yields_L = Float.parseFloat(GetProperty.readValue("yields_L")); //(-1:不啟用，單位:%) (預設:0)
		
		//估算股價高低點設定(股價低估)
		boolean lowHighFlag = Boolean.parseBoolean(GetProperty.readValue("lowHighFlag")); //false:不啟用、true:啟用 (預設:false)
		
		/********** 參數設定 **********/
		
		
		java.sql.Connection conn = ConnectionDAO.getConnection();
		java.sql.PreparedStatement PS1 = null, PS2 = null, PS3 = null, PS4 = null, PS5 = null, PS6 = null, PS7=null;
		java.sql.ResultSet RS1 = null, RS2 = null, RS3 = null, RS4 = null, RS5 = null, RS6 = null, RS7=null;
		
		//***** 組 SQL 條件字串 *****
		cal.set(Calendar.DATE, cal.get(Calendar.DATE)+bDate);
		dateStr = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH)+1) + "-" + cal.get(Calendar.DATE);
		
		String searchDesc = "篩選條件: "+dateStr+"\n";
		String sqlAdd = "";
		if(mean_60Flag==true) { sqlAdd += " and stock_deal-deal_change<=ma60"; searchDesc += "突破季線 \n"; }
		
		if(mean_60_distance_L != -1) { sqlAdd += " and mean_60_distance>="+mean_60_distance_L; searchDesc += "離季線>="+mean_60_distance_L+"\n"; }
		if(mean_60_distance_H != -1) { sqlAdd += " and mean_60_distance<="+mean_60_distance_H; searchDesc += "離季線<="+mean_60_distance_H+"\n"; }
		
		if(stock_deal_L != -1) { sqlAdd += " and stock_deal>="+stock_deal_L; searchDesc += "股價區間>="+stock_deal_L+"\n"; }
		if(stock_deal_H != -1) { sqlAdd += " and stock_deal<="+stock_deal_H; searchDesc += "股價區間<="+stock_deal_H+"\n"; }
		
		if(EPS_L != -1) { sqlAdd += " and EPS>="+EPS_L; searchDesc += "本益比(EPS)區間>="+EPS_L+"\n"; }
		if(EPS_H != -1) { sqlAdd += " and EPS<="+EPS_H; searchDesc += "本益比(EPS)區間<="+EPS_H+"\n"; }
		
		if(price_volume_L != -1) { sqlAdd += " and price_volume>="+price_volume_L; searchDesc += "價量分數區間(-40~40)>="+price_volume_L+"\n"; }
		if(price_volume_H != -1) { sqlAdd += " and price_volume<="+price_volume_H; searchDesc += "價量分數區間(-40~40)<="+price_volume_H+"\n"; }
		
		if(mean_60_distance_rate_L != -1) { sqlAdd += " and mean_60_distance_rate>="+mean_60_distance_rate_L; searchDesc += "離季線%區間>="+mean_60_distance_rate_L+"\n"; }
		if(mean_60_distance_rate_H != -1) { sqlAdd += " and mean_60_distance_rate<="+mean_60_distance_rate_H; searchDesc += "離季線%區間<="+mean_60_distance_rate_H+"\n"; }
		
		if(stock_sum_L != -1) { sqlAdd += " and stock_sum>"+stock_sum_L; searchDesc += "三大法人買賣張數>"+stock_sum_L+"\n"; }
		if(stock_sum_H != -1) { sqlAdd += " and stock_sum<="+stock_sum_H; searchDesc += "三大法人買賣張數<="+stock_sum_H+"\n"; }
		if(stock_sum_day != -1) { searchDesc += "三大法人買超天數>="+stock_sum_day+"\n"; }
		
		if(yields_L != -1) { searchDesc += "五年平均殖利率%>"+yields_L+"\n"; }
		
		if(lowHighFlag == true) { searchDesc += "高低點設定開啟\n"; }
		
		
		String sqlAdd2 = "";
		if(revenue_diffFlag == true) {
			sqlAdd2 += " and revenue_diff>="+revenue_diff_per;
			searchDesc += year+"/"+month+" 營收上月比較增減>="+revenue_diff_per+"% \n";
		}
		if(revenue_year_diffFlag == true) {
			sqlAdd2 += " and revenue_year_diff>="+revenue_year_diff_per;
			searchDesc += year+"/"+month+" 營收去年同月增減>="+revenue_year_diff_per+"% \n";
		}
		if(total_revenue_diffFlag == true) {
			sqlAdd2 += " and total_revenue_diff>="+total_revenue_diff_per;
			searchDesc += year+"/"+month+" 營收前期比較增減>="+total_revenue_diff_per+"% \n";
		}
		//***** 組 SQL 條件字串 *****
		
		//***** 爆量長紅(五日均量(張)) SQL *****
		String sql_5volume = " select avg(total_volume) volume5, count(stock_id) avg_count from ( "+
							 " select * from stock.wantgoo_stock where stock_id=? and create_date<=date(ADDDATE(now(),0+"+bDate+")) order by create_date desc limit "+volumeAvgDay+" "+
							 " ) w1 "+
							 " group by stock_id ";
		//System.err.println(sql_5volume);
		PS2 = conn.prepareStatement(sql_5volume);
		
		if(volume5Flag == true) {
			searchDesc += "當日成交量>="+volumeLimit+"張以上，且當日成交量>="+volumeAvgDay+"日均量"+volume5Mu+"倍以上，且股價上漲"+volumeUp+"%以上  \n";
			if(volume5LimitL != -1) {
				searchDesc += volumeAvgDay+"日均量(張)>="+volume5LimitL+"張以上 \n";
			}
			if(volume5LimitH != -1) {
				searchDesc += volumeAvgDay+"日均量(張)<="+volume5LimitH+"張 \n";
			}
			
		}
		//***** 爆量長紅(五日均量(張)) SQL *****
		
		//***** Main SQL *****
		String sqlStr1 = "select w1.*,w2.* from wantgoo_stock w1, wantgoo_stock_month w2 where w1.create_date=date(ADDDATE(now(),0+"+bDate+")) " + sqlAdd + " and w2.year='"+year+"' and w2.month='"+month+"' and w1.stock_id=w2.stock_id ";
		if(revenue_diffFlag == true || revenue_year_diffFlag==true || total_revenue_diffFlag==true) {
			sqlStr1 += sqlAdd2;
		}
		//System.err.println(sqlStr1);
		
		PS1 = conn.prepareStatement(sqlStr1);
		RS1 = PS1.executeQuery();
		//***** Main SQL *****
		
		//***** 計算上漲家數 SQL *****
		String[] comUpDateArr = new String[5];
		String comUpDateSql = "select distinct create_date from stock.wantgoo_stock where create_date<=date(ADDDATE(now(),0+"+bDate+")) order by create_date desc limit "+comUpDateArr.length;
		PS3 = conn.prepareStatement(comUpDateSql);
		RS3 = PS3.executeQuery();
		for(int i=0; RS3.next(); i++) {
			comUpDateArr[i] = RS3.getString("create_date");
		}
		
		String comUpCountSql = "select count(num) com_count from stock.wantgoo_stock where create_date=? and deal_change>?";
		PS4 = conn.prepareStatement(comUpCountSql);
		
		int comCountDiff = 0;
		int comTotalCount = 0;
		int[] comUpCountArr = new int[comUpDateArr.length];
		for(int i=0; i<comUpCountArr.length; i++) {
			PS4.setString(1, comUpDateArr[i]);
			PS4.setInt(2, 0);
			RS4 = PS4.executeQuery();
			if(RS4.next()) {
				comUpCountArr[i] = RS4.getInt("com_count");
			}
		}
		comCountDiff = comUpCountArr[0] - comUpCountArr[1];
		
		String comTotalCountSql = "select count(num) com_totalcount from stock.wantgoo_stock where create_date=?";
		PS5 = conn.prepareStatement(comTotalCountSql);
		PS5.setString(1, comUpDateArr[0]);
		RS5 = PS5.executeQuery();
		if(RS5.next()) { 
			comTotalCount = RS5.getInt("com_totalcount");
		}
		
		String exportComCountDesc = "\n\n";
		exportComCountDesc += "上漲家數: "+comUpCountArr[0]+" / "+comTotalCount+"【"+comUpCountArr[0]/(float)comTotalCount*100+"%】\n";
		exportComCountDesc += "與前一日上漲家數差: "+comCountDiff+"【"+comCountDiff/(float)comTotalCount*100+"%】";
		
		//***** 計算上漲家數 SQL *****
		
		//***** 計算五年平均殖利率 SQL *****
		String yieldsStockValueList = "";
		String yieldsStockIdList = "";
		String lowDealList = "";
		String highDealList = "";
		String yieldsSql = "";
			yieldsSql += "select s2.stock_id,s2.stock_name,s2.stock_deal+0 stock_deal,s1.money_total,s1.money_total_avg,s1.low, s1.high, s1.year_cnt,round((s1.money_total_avg/s2.stock_deal)*100,2) yields "+
			" from stock.wantgoo_stock s2, ( "+
			"	 select s1.stock_id, round(sum(s1.money_total),2) money_total,round(sum(s1.money_total)/count(s1.stock_id),2) money_total_avg, round(sum(s1.money_total)/count(s1.stock_id)*16,2) low, round(sum(s1.money_total)/count(s1.stock_id)*32,2) high, count(s1.stock_id) year_cnt "+
			"	 from stock.stock_share_money s1 "+
			"	 where s1.year1 in("+yieldsYearSetup+") "+
			"	 group by s1.stock_id "+
			"	 ) s1 "+
			" where s2.create_date=date(ADDDATE(now(),0+"+bDate+")) and s2.stock_deal<>0 and s1.stock_id=s2.stock_id ";
		PS6 = conn.prepareStatement(yieldsSql);
		RS6 = PS6.executeQuery();
		
		while(RS6.next()) {
			yieldsStockIdList += RS6.getString("stock_id")+",";
			yieldsStockValueList += RS6.getString("yields")+",";
			lowDealList += RS6.getString("low")+",";
			highDealList += RS6.getString("high")+",";
		}
		
		String[] yieldsStockIdArr = yieldsStockIdList.split(",");
		String[] yieldsStockValueArr = yieldsStockValueList.split(",");
		String[] lowDealArr = lowDealList.split(",");
		String[] highDealArr = highDealList.split(",");
		//***** 計算五年平均殖利率 SQL *****
		
		//***** 三大法人買超天數 SQL *****
		if(stock_sum_day == -1) { stock_sum_day = 0; }
		String sqlStr7 = "SELECT stock_id,stock_sum FROM wantgoo_stock where stock_id=? and stock_sum is not null order by create_date desc ";
		PS7 = conn.prepareStatement(sqlStr7);
		//***** 三大法人買超天數 SQL *****
		
		
		String exportNameList = "股碼,股名,成交價,漲跌,漲幅%,成交量,離季線,離季線%,EPS,價量分數,季線(60日),三大法人(張),法人連續買超(天),"+volumeAvgDay+"日均量(張),"+volumeAvgDay+"均計算天數,五年平均殖利率%,預估低點,預估高點,營收上月比較增減%,營收去年同月增減%,營收前期比較增減%"+"\n";
		String exportDataList = "";
		
		while(RS1.next()) {
			PS2.setString(1,RS1.getString("stock_id"));
			RS2 = PS2.executeQuery();
			
			/***** 三大法人買超天數 *****/
			PS7.setString(1,RS1.getString("stock_id"));
			RS7 = PS7.executeQuery();
			
			int stockSumDayCount = 0;
			for(int i=0; RS7.next(); i++) {
				if(RS7.getInt("stock_sum")>0) {
					stockSumDayCount++;
				}
				else {
					break;
				}
			}
			if(stock_sum_day != -1 && stockSumDayCount<stock_sum_day) {
				continue;
			}
			/***** 三大法人買超天數 *****/
			
			/***** 檢查殖利率設定條件 *****/
			String yValue = "";
			for(int i=0; i<yieldsStockIdArr.length; i++) {
				if(yieldsStockIdArr[i].equals(RS1.getString("stock_id"))) {
					yValue = yieldsStockValueArr[i];
					break;
				}
			}
			
			if(yields_L != -1 && !"".equals(yValue) && Float.parseFloat(yValue)<=yields_L) { 
				continue; 
			}
			/***** 檢查殖利率設定條件 *****/
			
			/***** 估算股價高低點條件 *****/
			String lowValue = "";
			String highValue = "";
			for(int i=0; i<lowDealArr.length; i++) {
				if(yieldsStockIdArr[i].equals(RS1.getString("stock_id"))) {
					lowValue = lowDealArr[i];
					highValue = highDealArr[i];
					break;
				}
			}
			if(lowHighFlag == true && !"".equals(lowValue) && RS1.getFloat("stock_deal")>Float.parseFloat(lowValue)) {
				continue;
			}
			/***** 估算股價高低點條件 *****/
			
			/***** 爆量(五日均量(張))開關 *****/
			float volume5 = 0;
			int avg_count = 0;
			if(RS2.next()) {
				volume5 = RS2.getFloat("volume5");
				avg_count = RS2.getInt("avg_count");
			}
			
			if(volume5Flag == true) {
					//判斷五日均量(張)上、下限條件
					if((volume5LimitL!=-1 && volume5<=volume5LimitL) || (volume5LimitH!=-1 && volume5>=volume5LimitH)) {
						continue;
					}
					
					if(RS1.getFloat("total_volume") < volumeLimit || RS1.getFloat("deal_percentage")<volumeUp || 
						RS1.getFloat("total_volume")/volume5<volume5Mu) {
						continue;
					}
			}
			/***** 爆量(五日均量(張))開關 *****/
			
			/***** Export Data *****/
			exportDataList += RS1.getString("stock_id") + ",";
			exportDataList += RS1.getString("stock_name") + ",";
			exportDataList += RS1.getString("stock_deal") + ",";
			exportDataList += RS1.getString("deal_change") + ",";
			exportDataList += RS1.getString("deal_percentage") + ",";
			exportDataList += RS1.getString("total_volume") + ",";
			exportDataList += RS1.getString("mean_60_distance") + ",";
			exportDataList += RS1.getString("mean_60_distance_rate") + ",";
			exportDataList += RS1.getString("EPS") + ",";
			exportDataList += RS1.getString("price_volume") + ",";
			exportDataList += RS1.getString("ma60") + ",";
			exportDataList += (RS1.getString("stock_sum")==null?"":RS1.getString("stock_sum")) + ",";
			exportDataList += stockSumDayCount + ",";
			exportDataList += volume5 + ",";
			exportDataList += avg_count + ",";
			exportDataList += yValue + ",";
			exportDataList += lowValue + ",";
			exportDataList += highValue + ",";
			exportDataList += RS1.getString("revenue_diff") + ",";
			exportDataList += RS1.getString("revenue_year_diff") + ",";
			exportDataList += RS1.getString("total_revenue_diff") + ",";
			exportDataList += "\n";
			/***** Export Data *****/
		}
		
		log.writeInfo(searchDesc);
		log.writeInfo(exportNameList);
		log.writeInfo(exportDataList);
		log.writeInfo(exportComCountDesc);
		log.writeInfo("檔案輸出路徑:"+allPath);
		
//		System.err.println(searchDesc);
//		System.err.println(exportNameList);
//		System.err.println(exportDataList);
//		System.err.println(exportComCountDesc);
//		System.err.println("檔案輸出路徑:"+allPath);
		
		wf.writeFile(searchDesc+"\n");
		wf.writeFile(exportNameList);
		wf.writeFile(exportDataList);
		wf.writeFile(exportComCountDesc);
		wf.closeFile();
		
		/***** 轉檔 CVS to XLS *****/
		XlsProcessor processor = XlsProcessor.getInstance();
		String csv = allPath;
		File f1 = processor.convert(new File(csv),filePath);
		/***** 轉檔 CVS to XLS *****/
		
		/***** Copy File *****/
		String dPath1 = GetProperty.readValue("dPath1");
		String dPath2 = GetProperty.readValue("dPath2");
		
		if((Integer.parseInt(GetProperty.readValue("run_write_file"))&1)==1) {
			FileAccess.Copy(filePath+f1.getName(),dPath1+f1.getName());
		}
		if((Integer.parseInt(GetProperty.readValue("run_write_file"))&2)==2) {
			FileAccess.Copy(filePath+f1.getName(),dPath2+f1.getName());
		}
		/***** Copy File *****/
		
		
		if(RS1 != null) { RS1.close(); }
		if(RS2 != null) { RS2.close(); }
		if(RS3 != null) { RS3.close(); }
		if(RS4 != null) { RS4.close(); }
		if(RS5 != null) { RS5.close(); }
		if(RS6 != null) { RS6.close(); }
		if(PS1 != null) { PS1.close(); }
		if(PS2 != null) { PS2.close(); }
		if(PS3 != null) { PS3.close(); }
		if(PS4 != null) { PS4.close(); }
		if(PS5 != null) { PS5.close(); }
		if(PS6 != null) { PS6.close(); }
		if(conn != null) { conn.close(); }
	}
}
