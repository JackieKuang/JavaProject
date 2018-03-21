package changeDb2;

import java.sql.*;
import java.util.Properties;

public class Skema {
    String db1,db2,tableName;
    boolean flag = false;
    Connection conn[];
    
    public Skema() {}
    public Skema(String d1, String d2,String t) {
        tableName = t;
        db1 = d1;
        db2 = d2;
        buildingStart();
    }
    
    private void buildingStart() {
        GetProperty gp = new GetProperty();
        Properties pt = gp.setProperty();
        ConectionDB cdb = new ConectionDB(db1,db2,pt);
        conn = cdb.getConnection();
        String strSql = "";
        if(db1.equals("MYSQL"))
            strSql = "select * from "+tableName+" LIMIT 0, 1";
        else if(db1.equals("Oracle")) 
            strSql = "select * from "+tableName+" where rownum<=1";
        else if(db1.equals("MSSQL"))
            strSql = "select top 1 * from "+tableName;
        
        
        try{
            Statement stmtGoal = conn[0].createStatement();
            ResultSet rsGoal = stmtGoal.executeQuery(strSql);
            ResultSetMetaData rsmGoal = rsGoal.getMetaData();
            int columnTotal_Goal = rsmGoal.getColumnCount();
            String[] columnTypeName = new String[columnTotal_Goal];
            String[] columnName = new String[columnTotal_Goal];
            int[] columnNull = new int[columnTotal_Goal];
            int[] columnDisplaySize = new int[columnTotal_Goal];
            int[] columnScale = new int[columnTotal_Goal];
            for(int i=0; i<columnTotal_Goal; i++) {
                columnTypeName[i] = rsmGoal.getColumnTypeName(i+1);
                columnName[i] = rsmGoal.getColumnName(i+1);
                columnNull[i] = rsmGoal.isNullable(i+1);
                columnDisplaySize[i] = rsmGoal.getColumnDisplaySize(i+1);
                columnScale[i] = rsmGoal.getScale(i+1);
                //tableName = rsmGoal.getTableName(i+1);
                //System.out.println(tableName+","+rsmGoal.getColumnTypeName(i+1)+","+rsmGoal.getColumnDisplaySize(i+1)+","+rsmGoal.getScale(i+1));
            }
            
            String strSchema = "CREATE TABLE "+tableName+"(";
            for(int i=0; i<columnTypeName.length; i++) {
                //System.out.println(columnName[i]);
                String isNull = "";
                if(columnNull[i]==1) isNull = "";
                else isNull = "NOT NULL";
                if(columnTypeName[i].toUpperCase().equals("LONG")) {
                    if(db2.equals("MSSQL"))  strSchema = strSchema + columnName[i] + " INT DEFAULT 0 " + isNull + ",";    //MYSQL -> MSSQL
                    else strSchema = strSchema + columnName[i] + " NUMBER(10) DEFAULT 0 " + isNull + ",";    //MYSQL -> ORACLE
                }
                else if(columnTypeName[i].toUpperCase().equals("VARCHAR")) {
                    if(db2.equals("MSSQL") || db2.equals("MYSQL"))  strSchema = strSchema + columnName[i] + " VARCHAR("+columnDisplaySize[i]+") " + isNull + ",";    //MYSQL -> MSSQL or MSSQL -> MYSQL
                    else strSchema = strSchema + columnName[i] + " VARCHAR2("+columnDisplaySize[i]+" byte) " + isNull + ",";    //MYSQL -> ORACLE
                }
                else if(columnTypeName[i].toUpperCase().equals("TINY")) {
                    if(db2.equals("MSSQL"))  strSchema = strSchema + columnName[i] + " TINYINT DEFAULT 0 " + isNull + ",";    //MYSQL -> MSSQL
                    else strSchema = strSchema + columnName[i] + " NUMBER(2) DEFAULT 0 " + isNull + ",";  //MYSQL -> ORACLE
                }
                else if(columnTypeName[i].toUpperCase().equals("DATETIME")) {
                    if(db2.equals("MSSQL") || db2.equals("MYSQL"))  strSchema = strSchema + columnName[i] + " DATETIME,";    //MYSQL -> MSSQL or MSSQL -> MYSQL
                    else strSchema = strSchema + columnName[i] + " DATE,";   //Date Type Default先設為NULL     //MYSQL -> ORACLE
                }
                else if(columnTypeName[i].toUpperCase().equals("FLOAT") || columnTypeName[i].toUpperCase().equals("DOUBLE")) {
                    if(db2.equals("MSSQL"))  strSchema = strSchema + columnName[i] + " FLOAT DEFAULT 0 " + isNull + ",";    //MYSQL -> MSSQL
                    else if(db2.equals("MYSQL"))  strSchema = strSchema + columnName[i] + " DOUBLE DEFAULT 0 " + isNull + ",";    //MSSQL -> MYSQL
                    else strSchema = strSchema + columnName[i] + " NUMBER(25,20) DEFAULT 0 " + isNull + ",";    //MYSQL -> ORACLE or MSSQL -> ORACLE
                }
                else if(columnTypeName[i].toUpperCase().equals("NUMBER")) { //Oracle
                    if(db2.equals("MSSQL")) {   //ORACLE -> MSSQL
                        if(columnScale[i]>0) 
                            strSchema = strSchema + columnName[i] + " FLOAT DEFAULT 0 " + isNull + ",";   
                        else
                            strSchema = strSchema + columnName[i] + " INT DEFAULT 0 " + isNull + ",";   
                    }
                    else if(db2.equals("MYSQL")) { //ORACLE -> MYSQL
                        if(columnScale[i]>5) 
                            strSchema = strSchema + columnName[i] + " DOUBLE DEFAULT 0 " + isNull + ",";    
                        else if(columnScale[i]>0) 
                            strSchema = strSchema + columnName[i] + " FLOAT DEFAULT 0 " + isNull + ",";    
                        else 
                            strSchema = strSchema + columnName[i] + " INT(4) DEFAULT 0 " + isNull + ",";
                    }
		}
                else if(columnTypeName[i].toUpperCase().equals("DATE")) {   //Oracle
                    if(db2.equals("Oracle")) strSchema = strSchema + columnName[i] + " DATE,";   //DATETIME Type Default先設為NULL
                    else strSchema = strSchema + columnName[i] + " DATETIME,";   //DATETIME Type Default先設為NULL
		}
                else if(columnTypeName[i].toUpperCase().equals("VARCHAR2")) {   //Oracle
                    if(db2.equals("MSSQL")) {
                        strSchema = strSchema + columnName[i] + " VARCHAR("+columnDisplaySize[i]+") " + isNull + ",";   //ORACLE -> MSSQL
                    }
                    else {
                        if(columnDisplaySize[i]>255) strSchema = strSchema + columnName[i] + " TEXT " + isNull + ",";
                        else strSchema = strSchema + columnName[i] + " VARCHAR("+columnDisplaySize[i]+") " + isNull + ",";  //ORACLE -> MYSQL
                    }
                }
                else if(columnTypeName[i].toUpperCase().equals("INT")) {
                    if(db2.equals("MYSQL"))  strSchema = strSchema + columnName[i] + " INT(4) DEFAULT 0 " + isNull + ",";    //MSSQL -> MYSQL
                    else strSchema = strSchema + columnName[i] + " NUMBER(10) DEFAULT 0 " + isNull + ",";    //MSSQL -> ORACLE
                }
            }
            strSchema = strSchema.substring(0,strSchema.length()-1);
            strSchema = strSchema + ")";
            //System.out.println(strSchema);
            Statement stmtUpdate = conn[1].createStatement();
            stmtUpdate.executeUpdate(strSchema);
            flag = true;
        }
        catch(Exception e) {
            System.out.println(e);
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
