package testing_bank;

import com.mysql.jdbc.*;
import java.sql.*;

public class TB_TansferData2 {

	//MYSQL Connection
	java.sql.Connection connMysql = null;
	String mysql_url=GetProperty.readValue("mysql_url");
	String mysqlLoginId = GetProperty.readValue("mysqlLoginId");
	String mysqlLoginPwd = GetProperty.readValue("mysqlLoginPwd");
	
	//Oracle Connection
	java.sql.Connection connOracle = null;
	String oracle_url=GetProperty.readValue("oracle_url");
	String oracleLoginId = GetProperty.readValue("oracleLoginId");
	String oracleLoginPwd = GetProperty.readValue("oracleLoginPwd");
	
	//Log
	WriteFile wf = null;
	String logPath = GetProperty.readValue("logPath2");
	
	public static void main(String[] args) throws Exception {
		
		TB_TansferData2 tbDao = new TB_TansferData2();
		tbDao.exeStart(args);
		
	}
	
	public void exeStart(String[] args) throws Exception {
		String tableList = "tb_user";
		String[] tableName = tableList.split(",");
		
		for(int i=0; i<tableName.length; i++) {
			wf = new WriteFile(logPath + "TB-TansferData2_"+tableName[i]+"-log.txt");
			
			System.err.println("***** Start "+tableName[i]+" *****");
			transferData(tableName[i]);
			System.err.println("***** End "+tableName[i]+" *****");
			System.err.println("");
		}
		System.err.println("All Finish!!");
	}
	
	
	public void transferData(String tName) throws Exception {
		java.sql.PreparedStatement psGoal=null, ps1=null,ps2=null, ps3=null;
		java.sql.ResultSet rsGoal=null, rs1=null, rs2=null, rs3=null;
		
		int totalCount = 0;
	    int rowCount = 0;
	    int allCount = 0;
	    String userId = "";
	    
		try {
		
			//MYSQL
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connMysql = DriverManager.getConnection (mysql_url, mysqlLoginId, mysqlLoginPwd);
			
			//Oracle
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			connOracle = DriverManager.getConnection (oracle_url, oracleLoginId, oracleLoginPwd);
			
			//System.err.println("connMysql:"+connMysql);
			//System.err.println("connOracle:"+connOracle);
			
			//查欄位數量
			String sqlColumn = "select * from "+tName +" where rownum<=1";
			//System.err.println("sqlColumn:"+sqlColumn);
			psGoal = connOracle.prepareStatement(sqlColumn);
			rsGoal = psGoal.executeQuery();
			
			
			java.sql.ResultSetMetaData rsmGoal = rsGoal.getMetaData();
		    int columnTotal_Goal = rsmGoal.getColumnCount();
		    String[] columnTypeName = new String[columnTotal_Goal];
		    String[] columnName = new String[columnTotal_Goal];
		    
		    String columnNameList = "";
		    String columnValuesList = "";
		    for(int i=0; i<columnTotal_Goal; i++) {
		        columnTypeName[i] = rsmGoal.getColumnTypeName(i+1);
		        columnName[i] = rsmGoal.getColumnName(i+1);
		        
		        if("EXPLAIN".equals(columnName[i])) {
		        	columnName[i] = "EXPLAIN_DESC"; //因 EXPLAIN 在 MYSQL 為 Keyword，所以需 rename 為 EXPLAIN_DESC
		        }
		        
		        columnNameList += columnName[i] + ",";
		        columnValuesList += "?,";
		        
		        //System.out.println(tName+","+columnTypeName[i]+","+columnName[i]);
		    }
		    if(columnNameList.length()>0) {
		    	columnNameList = columnNameList.substring(0,columnNameList.length()-1);
		    	columnValuesList = columnValuesList.substring(0,columnValuesList.length()-1);
		    }
		    //System.out.println(columnNameList);
		    //System.out.println(columnValuesList);
		    
		    
		    //String sqlOracleQuery = "select * from "+tName+" where rownum<=1";
		    String sqlOracleQuery = "select * from "+tName+" where user_id>=1500000 order by USER_ID";
		    ps1 = connOracle.prepareStatement(sqlOracleQuery);
		    rs1 = ps1.executeQuery();
		    
		    String sqlOracleQuery2 = "select count(user_id) c1 from "+tName;
		    ps3 = connOracle.prepareStatement(sqlOracleQuery2);
		    rs3 = ps3.executeQuery();
		    rs3.next();
		    allCount = rs3.getInt("c1");
		    
		    String sqlMysqlInsert = "insert into "+tName+"("+columnNameList+") values("+columnValuesList+")";
		    ps2 = connMysql.prepareStatement(sqlMysqlInsert);
		    //System.out.println(sqlMysqlInsert);
		    
		    while(rs1.next()) {
		    	System.err.println("USER_ID="+rs1.getString(1)+",rowCount="+rowCount+"/"+allCount);
		    	userId = rs1.getString(1);
		    	
		    	try{
			    	for(int i=1; i<=columnName.length; i++) {
			    		ps2.setString(i, rs1.getString(i));
			    		
			    		//System.err.println(rs1.getString(i));
			    	}
			    	
		    		if(ps2.executeUpdate() >= 1) {
		        		totalCount++;
		        		//System.err.println("Table Name:"+tName+",Current:"+totalCount+"/"+rowCount+"\r\n");
		        	}
		    	}
		    	catch(Exception ex) {
		    		System.err.println("Table Name:"+tName+","+rs1.getString(1)+":"+ex.toString());
		    		wf.writeFile("Table Name:"+tName+","+rs1.getString(1)+":"+ex.toString()+"\n");
		    	}
		    	
		    	rowCount++;
		    }
			
		    
		}
		catch(Exception ex1) {
			System.err.println("Table Name:"+tName+",userId="+userId+":"+ex1.toString());
			wf.writeFile("Table Name:"+tName+",userId="+userId+":"+ex1.toString()+"\n");
		}
		finally {
			ps1.close();
		    rs1.close();
		    ps3.close();
		    rs3.close();
		    ps2.close();
			connMysql.close();
			connOracle.close();
			
			System.err.println("Table Name:"+tName+",totalCount:"+totalCount+"/"+allCount);
			wf.writeFile("Table Name:"+tName+",totalCount:"+totalCount+"/"+allCount+"\n");
			wf.closeFile();
		}
	}
}
