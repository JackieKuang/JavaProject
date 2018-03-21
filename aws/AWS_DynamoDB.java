package aws;

import com.mysql.jdbc.*;
import java.sql.*;
import org.json.*;

public class AWS_DynamoDB {

	//MYSQL Connection
	java.sql.Connection connMysql = null;
	String mysql_url=GetProperty.readValue("mysql_url");
	String mysqlLoginId = GetProperty.readValue("mysqlLoginId");
	String mysqlLoginPwd = GetProperty.readValue("mysqlLoginPwd");
	
	//Log
	WriteFile wf = null;
	
	public static void main(String[] args) throws Exception {
		
		AWS_DynamoDB tbDao = new AWS_DynamoDB();
		//tbDao.asmModuleCpiB();
		//tbDao.asmModuleCpiPst();
		//tbDao.asmModuleCpiMdl();
		//tbDao.asmModuleCpiMdlBatch();
		tbDao.asmModuleCompetency();
	}
	
	//職職行為常模
	public void asmModuleCompetency() throws Exception {
		try{
			//MYSQL
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connMysql = DriverManager.getConnection (mysql_url, mysqlLoginId, mysqlLoginPwd);
			
			//write file
			WriteFile wf = new WriteFile("D:/aws/export/asm_module_competency/asm_module_competency.txt");
			
			//Random Number
			java.util.Random rand = new java.util.Random();
			
			java.sql.PreparedStatement psCpiMdl=null;
			java.sql.ResultSet rsCpiMdl=null;
			
			String sql_cpi_mdl = "select t1.*, t2.MEAN,t2.SD from tb_module_competency_mdl t1, tb_module_competency t2 where t1.DMS_ID=t2.DMS_ID and t1.LNG_ID=t2.LNG_ID and t1.LNG_ID=1 and length(t1.DMS_ID)=3 and t1.CUSTNO=1";
			psCpiMdl = connMysql.prepareStatement(sql_cpi_mdl);
			rsCpiMdl = psCpiMdl.executeQuery();
			
			int row = 0;
			while(rsCpiMdl.next()) {
				int  n = rand.nextInt(98) + 1;
				String randNum = n + "";
				if(n < 10) {
					randNum  = "0" + n;
				}
				
				JSONObject sJon = new JSONObject();
				sJon.put("mdl_key", getEmptyValue("s", rsCpiMdl.getString("CUSTNO")+","+rsCpiMdl.getString("PST_ID")));
				sJon.put("cid", getEmptyValue("n", rsCpiMdl.getString("CUSTNO")));
				sJon.put("mdl_id", getEmptyValue("n", new java.util.Date().getTime()+randNum));
				sJon.put("dms_id", getEmptyValue("s", rsCpiMdl.getString("DMS_ID")));
				sJon.put("pst_id", getEmptyValue("n", rsCpiMdl.getString("PST_ID")));
				sJon.put("pst_name", getEmptyValue("s", rsCpiMdl.getString("PST_NAME")));
				sJon.put("lng_id", getEmptyValue("n", rsCpiMdl.getString("LNG_ID")));
				sJon.put("pr_score", getEmptyValue("n", rsCpiMdl.getString("PR_SCORE")));
				sJon.put("is_radar", getEmptyValue("n", "0"));
				sJon.put("scope_high", getEmptyValue("n", rsCpiMdl.getString("SCOPE_HIGH")));
				sJon.put("scope_low", getEmptyValue("n", rsCpiMdl.getString("SCOPE_LOW")));
				sJon.put("mean", getEmptyValue("n", rsCpiMdl.getString("MEAN")));
				sJon.put("sd", getEmptyValue("n", rsCpiMdl.getString("SD")));
				
				if(rsCpiMdl.isLast()) {
					wf.writeLine(sJon.toString());
				}
				else {
					wf.writeLine(sJon.toString()+"@@@@@");
				}
				
				row++;
				System.err.println(row + ",sJon:"+sJon.toString());
				Thread.sleep(1);
			}
			
			System.err.println("asmModuleCompetency Finish");
		}
		catch(Exception ex){
			System.err.println(ex);
			
		}
		finally {
			if(connMysql != null) {connMysql.close();}
			if(wf != null) {wf.closeFile();}
		}
	}
	
	
	/*
	 * PST 新舊常模 0:舊 1:新 (第一代常模不分新舊, 第二代職能常模只有新常模, 第二代性格常模有分新舊)
	 * MDL 新舊常模 0:舊 1:新 (第一代和第二代常模都有分新舊, 第二代舊常模沒有職能)
	 */
	public void asmModuleCpiPst() throws Exception {
		try{
			//MYSQL
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connMysql = DriverManager.getConnection (mysql_url, mysqlLoginId, mysqlLoginPwd);
			
			//write file
			WriteFile wf = new WriteFile("D:/aws/export/asm_module_cpi_pst/asm_module_cpi_pst.txt");
			
			//Random Number
			java.util.Random rand = new java.util.Random();
			
			java.sql.PreparedStatement psCpiPst=null;
			java.sql.ResultSet rsCpiPst=null;
			
			//String sql_cpi_pst = "select * from tb_module_cpi_pst_v1 where custno=1 and lng_id=1";
			String sql_cpi_pst = "select * from tb_module_cpi_pst_v1 where lng_id=1 and ((MODULE_VER=1 and custno=1) or (MODULE_VER=2 and is_new=1))";
			psCpiPst = connMysql.prepareStatement(sql_cpi_pst);
			rsCpiPst = psCpiPst.executeQuery();
			
			while(rsCpiPst.next()) {
				int  n = rand.nextInt(98) + 1;
				String randNum = n + "";
				if(n < 10) {
					randNum  = "0" + n;
				}
				
				JSONObject sJon = new JSONObject();
				sJon.put("mdl_key", getEmptyValue("s", rsCpiPst.getString("CUSTNO")+","+rsCpiPst.getString("INDUSTRY_ID")+","+rsCpiPst.getString("POSITION_ID")));
				sJon.put("mdl_id", getEmptyValue("n", new java.util.Date().getTime()+randNum));
				sJon.put("pst_id", getEmptyValue("n", rsCpiPst.getString("PST_ID")));
				sJon.put("industry_id", getEmptyValue("n", rsCpiPst.getString("INDUSTRY_ID")));
				sJon.put("position_id", getEmptyValue("n", rsCpiPst.getString("POSITION_ID")));
				sJon.put("type", getEmptyValue("n", rsCpiPst.getString("TYPE")));
				sJon.put("lng_id", getEmptyValue("n", rsCpiPst.getString("LNG_ID")));
				sJon.put("pst_name", getEmptyValue("s", rsCpiPst.getString("PST_NAME")));
				sJon.put("custno", getEmptyValue("n", rsCpiPst.getString("CUSTNO")));
				sJon.put("zrm", getEmptyValue("n", rsCpiPst.getString("ZRM")));
				sJon.put("zrsd", getEmptyValue("n", rsCpiPst.getString("ZRSD")));
				sJon.put("is_open", getEmptyValue("n", rsCpiPst.getString("IS_OPEN")));
				sJon.put("module_ver", getEmptyValue("n", rsCpiPst.getString("MODULE_VER")));
				sJon.put("sample_count", getEmptyValue("n", rsCpiPst.getString("SAMPLE_COUNT")));
				sJon.put("sampling_date", getEmptyValue("n", new java.util.Date().getTime()+""));
				//sJon.put("is_new", getEmptyValue("n", rsCpiPst.getString("IS_NEW")));
				
				if(rsCpiPst.isLast()) {
					wf.writeLine(sJon.toString());
				}
				else {
					wf.writeLine(sJon.toString()+"@@@@@");
				}
				
				System.err.println("sJon:"+sJon.toString());
				Thread.sleep(1);
			}
			
			System.err.println("asmModuleCpiPst Finish");
		}
		catch(Exception ex){
			System.err.println(ex);
			
		}
		finally {
			if(connMysql != null) {connMysql.close();}
			if(wf != null) {wf.closeFile();}
		}
	}
	
	public void asmModuleCpiMdl() throws Exception {
		try{
			//MYSQL
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connMysql = DriverManager.getConnection (mysql_url, mysqlLoginId, mysqlLoginPwd);
			
			//write file
			WriteFile wf = new WriteFile("D:/aws/export/asm_module_cpi_mdl/asm_module_cpi_mdl.txt");
			
			//Random Number
			java.util.Random rand = new java.util.Random();
			
			java.sql.PreparedStatement psCpiMdl=null;
			java.sql.ResultSet rsCpiMdl=null;
			
			String sql_cpi_mdl = "select * from tb_module_cpi_mdl_v1 where is_new=1 and lng_id=1";
			psCpiMdl = connMysql.prepareStatement(sql_cpi_mdl);
			rsCpiMdl = psCpiMdl.executeQuery();
			
			int row = 0;
			while(rsCpiMdl.next()) {
				int  n = rand.nextInt(98) + 1;
				String randNum = n + "";
				if(n < 10) {
					randNum  = "0" + n;
				}
				
				
				String is_important = "0";
				String index_important = "0";
				
				if(rsCpiMdl.getString("IS_IMPORTANT") != null) {
					is_important = rsCpiMdl.getString("IS_IMPORTANT");
				}
				if(rsCpiMdl.getString("INDEX_IMPORTANT") != null) {
					index_important = rsCpiMdl.getString("INDEX_IMPORTANT");
				}
				
				JSONObject sJon = new JSONObject();
				sJon.put("mdd_id", getEmptyValue("n", new java.util.Date().getTime()+randNum));
				sJon.put("pst_id", getEmptyValue("n", rsCpiMdl.getString("PST_ID")));
				sJon.put("type", getEmptyValue("n", rsCpiMdl.getString("TYPE")));
				sJon.put("dms_id", getEmptyValue("s", rsCpiMdl.getString("DMS_ID")));
				sJon.put("lng_id", getEmptyValue("n", rsCpiMdl.getString("LNG_ID")));
				sJon.put("mean", getEmptyValue("n", rsCpiMdl.getString("MEAN")));
				sJon.put("sd", getEmptyValue("n", rsCpiMdl.getString("SD")));
				sJon.put("z", getEmptyValue("n", rsCpiMdl.getString("Z")));
				//sJon.put("sample_count", getEmptyValue("n", new java.util.Date().getTime()+""));
				sJon.put("is_important", getEmptyValue("n", is_important));
				sJon.put("index_important", getEmptyValue("n", index_important));
				//sJon.put("is_new", getEmptyValue("n", rsCpiMdl.getString("IS_NEW")));				
				
				if(rsCpiMdl.isLast()) {
					wf.writeLine(sJon.toString());
				}
				else {
					wf.writeLine(sJon.toString()+"@@@@@");
				}
				
				row++;
				System.err.println(row + ",sJon:"+sJon.toString());
				Thread.sleep(1);
			}
			
			System.err.println("asmModuleCpiMdl Finish");
		}
		catch(Exception ex){
			System.err.println(ex);
			
		}
		finally {
			if(connMysql != null) {connMysql.close();}
			if(wf != null) {wf.closeFile();}
		}
	}
	
	
	public void asmModuleCpiMdlBatch() throws Exception {
		try{
			//MYSQL
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connMysql = DriverManager.getConnection (mysql_url, mysqlLoginId, mysqlLoginPwd);
			
			//write file
			WriteFile wf = new WriteFile("D:/aws/export/asm_module_cpi_mdl/asm_module_cpi_mdl_batch.txt");
			
			//Random Number
			java.util.Random rand = new java.util.Random();
			
			java.sql.PreparedStatement psCpiMdl=null;
			java.sql.ResultSet rsCpiMdl=null;
			
			String sql_cpi_mdl = "select * from tb_module_cpi_mdl_v1 where is_new=1 and lng_id=1 limit 26";
			psCpiMdl = connMysql.prepareStatement(sql_cpi_mdl);
			rsCpiMdl = psCpiMdl.executeQuery();
			
			JSONArray sJonArr = new JSONArray();
			int row = 0;
			while(rsCpiMdl.next()) {
				int  n = rand.nextInt(98) + 1;
				String randNum = n + "";
				if(n < 10) {
					randNum  = "0" + n;
				}
				
				JSONObject sJon = new JSONObject();
				sJon.put("mcm_id", getValue("n", new java.util.Date().getTime()+randNum));
				sJon.put("pst_id", getValue("n", rsCpiMdl.getString("PST_ID")));
				sJon.put("type", getValue("n", rsCpiMdl.getString("TYPE")));
				sJon.put("dms_id", getValue("s", rsCpiMdl.getString("DMS_ID")));
				sJon.put("lng_id", getValue("n", rsCpiMdl.getString("LNG_ID")));
				sJon.put("mean", getValue("n", rsCpiMdl.getString("MEAN")));
				sJon.put("sd", getValue("n", rsCpiMdl.getString("SD")));
				sJon.put("z", getValue("n", rsCpiMdl.getString("Z")));
				//sJon.put("sample_count", getValue("n", new java.util.Date().getTime()+""));
				sJon.put("is_important", getValue("n", rsCpiMdl.getString("IS_IMPORTANT")));
				sJon.put("index_important", getValue("n", rsCpiMdl.getString("INDEX_IMPORTANT")));
				//sJon.put("is_new", getValue("n", rsCpiMdl.getString("IS_NEW")));				
				
				
				JSONObject sJonItem = new JSONObject();
				sJonItem.put("Item", sJon);
				
				JSONObject sJonPutRequest = new JSONObject();
				sJonPutRequest.put("PutRequest", sJonItem);
				
				sJonArr.put(sJonPutRequest);
				
				row++;
				System.err.println(row + ",sJon:"+sJon.toString());
				Thread.sleep(1);
			}
			
			
			JSONObject sJonTable = new JSONObject();
			sJonTable.put("asm_module_cpi_mdl_batch", sJonArr);
			
			JSONObject sJonDb = new JSONObject();
			sJonDb.put("RequestItems", sJonTable);
			
			wf.writeLine(sJonDb.toString());
			System.err.println(sJonDb);
			System.err.println("asmModuleCpiMdl Finish");
		}
		catch(Exception ex){
			System.err.println(ex);
			
		}
		finally {
			if(connMysql != null) {connMysql.close();}
			if(wf != null) {wf.closeFile();}
		}
	}
	
	public void asmModuleCpiB() throws Exception {
		try{
			//MYSQL
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connMysql = DriverManager.getConnection (mysql_url, mysqlLoginId, mysqlLoginPwd);
			
			//write file
			WriteFile wf = new WriteFile("D:/aws/export/asm_module_cpi_b/asm_module_cpi_b.txt");
			
			
			java.sql.PreparedStatement psCpiB=null;
			java.sql.ResultSet rsCpiB=null;
			
			String sql_cpi_b = "select * from tb_module_cpi_b_v1";
			psCpiB = connMysql.prepareStatement(sql_cpi_b);
			rsCpiB = psCpiB.executeQuery();
			while(rsCpiB.next()) {
				JSONObject sJon = new JSONObject();
				JSONObject sJonSub = new JSONObject();
				sJon.put("dms_id", sJonSub.put("s", rsCpiB.getString("DMS_ID")));
				for(int i=1; i<=28; i++) {
					JSONObject sJonSub2 = new JSONObject();
					String num = i+"";
					if(i<10) {
						num = "0" + i;
					}
					sJon.put("dev_d"+num, sJonSub2.put("n", rsCpiB.getString("DEV_D"+num)));
					
				}
				
				JSONObject sJonSub3 = new JSONObject();
				sJon.put("cons", sJonSub3.put("n", rsCpiB.getString("CONS")));
				
				JSONObject sJonSub4 = new JSONObject();
				sJon.put("title_status", sJonSub4.put("n", rsCpiB.getString("STATUS")));
				
				if(rsCpiB.isLast()) {
					wf.writeLine(sJon.toString());
				}
				else {
					wf.writeLine(sJon.toString()+"@@@@@");
				}
				
				System.err.println("sJon:"+sJon.toString());
			}
			
			System.err.println("asmModuleCpiB Finish");
		}
		catch(Exception ex){
			System.err.println(ex);
			
		}
		finally {
			if(connMysql != null) {connMysql.close();}
			if(wf != null) {wf.closeFile();}
		}
	}
	
	public JSONObject getValue(String s, String d) throws Exception {
		JSONObject sJonSub = new JSONObject();
		
		if("s".equals(s)) {
			sJonSub.put("S", d);
		}
		else if("n".equals(s)) {
			if(d == null) {
				sJonSub.put("N", "0");
			}
			else {
				sJonSub.put("N", d);
			}
		}
		
		return sJonSub;
	}
	
	public String getEmptyValue(String s, String d) throws Exception {
		return d;
	}
	
}
