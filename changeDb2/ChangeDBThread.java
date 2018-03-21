package changeDb2;

import java.util.*;
import java.sql.*;
import java.io.*;
import java.awt.*;

public class ChangeDBThread extends Thread {
    Calendar cal;
    String date;
    Properties pt;
    String table_name;
    Connection[] conn;
    ResultSet rs_mysql;
    ChangeDbMain cdm2;
    StartDialog sd_dia;
    String cg1Str,cg2Str;

    ChangeDBThread() {}
    
    ChangeDBThread(Properties p, String t, ChangeDbMain cdm, StartDialog sd) {
            cal = Calendar.getInstance();
            date = cal.get(Calendar.YEAR)+"/"+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+" "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
            cdm2 = cdm;
            sd_dia = sd;
            pt = p;
            table_name = t;
            conn = new Connection[2];
    }
    
    void setValues(String c1, String c2) {
        cg1Str = c1;
        cg2Str = c2;
    }
    
    void setConnection() {
        String[] cgs = new String[2];
        cgs[0] = cg1Str;
        cgs[1] = cg2Str;
        for(int i=0;i<cgs.length;i++) {
            if(cgs[i].equals("MYSQL")) {
                setMysqlConnection(i);
            }
            else if(cgs[i].equals("Oracle")) {
                setOracleConnection(i);
            }
            else if(cgs[i].equals("MSSQL")) {
                setMssqlConnection(i);
            }
        }
    }
    
    private void setMysqlConnection(int i) {	//Mysql Connection
            try{
                    Class.forName(pt.getProperty("mysql_jdbc"));
                    conn[i] = DriverManager.getConnection("jdbc:mysql://"+ pt.getProperty("mysql_server") +":"+ pt.getProperty("mysql_port") +"/"+ pt.getProperty("mysql_db"),pt.getProperty("mysql_login"),pt.getProperty("mysql_passwd"));
            }
            catch(Exception e) {
                    e.printStackTrace();
            }
    }
    
    private void setOracleConnection(int i) {	//Oracle Connection
            try {
                    Class.forName(pt.getProperty("oracle_jdbc")).newInstance();
                    conn[i] = java.sql.DriverManager.getConnection("jdbc:oracle:thin:@"+ pt.getProperty("oracle_server") +":"+ pt.getProperty("oracle_port") +":"+ pt.getProperty("oracle_db"),pt.getProperty("oracle_login"),pt.getProperty("oracle_passwd"));
            }
            catch(Exception e) {
                    e.printStackTrace();
            }
    }
    
    private void setMssqlConnection(int i) {     //Mssql Connection
            try{
                    Class.forName(pt.getProperty("mssql_jdbc")).newInstance();
                    conn[i] = DriverManager.getConnection("jdbc:inetdae7:"+ pt.getProperty("mssql_server") +":"+ pt.getProperty("mssql_port") +"?database="+ pt.getProperty("mssql_db") +"&sql7="+ pt.getProperty("mssql_sql7"),pt.getProperty("mssql_login"),pt.getProperty("mssql_password"));
            }
            catch(Exception e) {
                    e.printStackTrace();
            }
    }
    
    public void run() {     //執行開始
            try{
                    DbColumnType dct = new DbColumnType();

                    //Oracle 處理
                    String str_oracle = "select * from "+ table_name;
                    Statement stmt_oracle = conn[1].createStatement();
                    ResultSet rs_oracle = stmt_oracle.executeQuery(str_oracle);
                    ResultSetMetaData rsm_oracle = rs_oracle.getMetaData();
                    int columnCount_oracle = rsm_oracle.getColumnCount();

                    StringBuffer sb = new StringBuffer();
                    String orderStr = "";
                    for(int i=1; i<=columnCount_oracle; i++) {
                            if(i != columnCount_oracle) {
                                    sb = sb.append(rsm_oracle.getColumnName(i)+",");
                                    if(i==1) {
                                            orderStr = rsm_oracle.getColumnName(i);
                                    }
                            }
                            else {
                                    sb = sb.append(rsm_oracle.getColumnName(i));
                            }	
                    }

                    //Mysql 處理
                    String str_mysql = "select "+ sb.toString() +" from " + table_name + " order by "+orderStr;	//全不部資料轉
                    //String str_mysql = "select "+ sb.toString() +" from " + table_name + " limit 0,10";	//Test

                    Statement stmt_mysql = conn[0].createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                    rs_mysql = stmt_mysql.executeQuery(str_mysql);
                    rs_mysql.last();
                    int rowCount = rs_mysql.getRow();
                    int k=1;
                    int scale=0;
                    rs_mysql.beforeFirst();

                    Statement so = conn[1].createStatement();
                    while(rs_mysql.next()) {
                            String value_list = "";
                            String str_temp = "";
                            StringBuffer sb2 = null;
                            for(int j=1; j<=columnCount_oracle; j++) {
                               //System.out.println(rsm_oracle.getColumnName(j)+","+rsm_oracle.getColumnType(j)+","+rs_mysql.getString(j));
                                    value_list += dct.getTypeString(rs_mysql.getString(j),rsm_oracle.getColumnType(j),cg2Str,cg1Str);
                            }
                            //System.out.println("=======================================");
                            sb2 = new StringBuffer(value_list);
                            sb2 = sb2.deleteCharAt(sb2.length()-1);

                            str_temp = "insert into " + table_name + " values("+ sb2.toString() +")";
                            //System.out.println(str_temp);
                            so.executeUpdate(str_temp);
                            
                            //計算完成比例
                            scale = Math.round((float)k/rowCount*100);
                            String msgTemp = cg1Str+"--->"+cg2Str+"，\""+table_name.toUpperCase()+"\" 轉換資料中!";
                            sd_dia.setMessage(msgTemp,"完成 "+scale+" %，"+k+"/"+rowCount,scale);
                            k++;
                             
                    }
                    //System.out.println(columnCount_oracle);	//Show Oracle Table 欄位總數

                    conn[0].close();
                    conn[1].close();

                    //Message Dialog
                    String message = "This Table \""+ table_name.toUpperCase() +"\" Was Changed Success~~";
                    MessageDialog md = new MessageDialog(cdm2,"Message Dialog",true,message,sd_dia);
                    md.show();
                             
            }
            catch(SQLException e) {
                    //Writer Log
                    String str_row = "";
                    try{
                            str_row = "，The Table " + table_name.toUpperCase() +"("+ rs_mysql.getRow()+") ";
                    }
                    catch(Exception ex) {
                            str_row = "，The Table " + table_name.toUpperCase() +"(無資料起始) ";
                    }

                    String err = "Error Message：" + e.toString() + str_row + " ("+ date +")\r\n";
                    WriterLog wlog = new WriterLog(pt.getProperty("log_path"));
                    wlog.writeLog(err);

                    //Message Dialog
                    String message = "This Table \""+ table_name.toUpperCase() +"\" Was Changed Fail，Please See The "+ pt.getProperty("log_path") +" File ~~";
                    MessageDialog md = new MessageDialog(cdm2,"Message Dialog",true,message,sd_dia);
                    md.show();
                    //e.printStackTrace();
            }
            catch(Exception e) {
                    //Writer Log
                    String str_row = "";
                    try{
                            str_row = "，The Table " + table_name.toUpperCase() +"("+ rs_mysql.getRow()+") ";
                    }
                    catch(Exception ex) {
                            str_row = "，The Table " + table_name.toUpperCase() +"(無資料起始) ";
                    }

                    String err = "Error Message：" + e.toString() + str_row + " ("+ date +")\r\n";
                    WriterLog wlog = new WriterLog(pt.getProperty("log_path"));
                    wlog.writeLog(err);

                    //Message Dialog
                    String message = "This Table \""+ table_name.toUpperCase() +"\" Was Changed Fail，Please See The "+ pt.getProperty("log_path") +" File ~~";
                    MessageDialog md = new MessageDialog(cdm2,"Message Dialog",true,message,sd_dia);
                    md.show();
            }
            finally {
                for(int d=0; d<conn.length; d++) {
                    if(conn[d] != null) {
                        try {
                            conn[d].close();
                            conn[d] = null;
                        }
                        catch(Exception e) {System.out.println(conn[d]+" Connection Close Error!!");}
                    }
                }
            }
    }
    
}
