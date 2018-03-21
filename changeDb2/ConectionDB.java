package changeDb2;

import java.sql.*;
import java.util.Properties;

public class ConectionDB {
    Properties pt;
    Connection conn[] = new Connection[2];
    
    public ConectionDB() {}
    public ConectionDB(String d1,String d2,Properties p) {
        pt = p;
        String[] cgs = new String[2];
        cgs[0] = d1;    //¨Ó·½
        cgs[1] = d2;    //¥Øªº
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
    
    public Connection[] getConnection() {
        return conn;
    }
    
}
