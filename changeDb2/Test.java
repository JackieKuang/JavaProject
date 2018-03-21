/*
 * Test.java
 *
 * Created on 2002年11月1日, 下午 3:36
 */

package changeDb2;

import java.sql.*;
/**
 *
 * @author  Administrator
 */
public class Test {
    
    /** Creates a new instance of Test */
    public Test() {
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //Class.forName("com.inet.tds.TdsDriver").newInstance();
        //Connection conn = DriverManager.getConnection("jdbc:inetdae7:winnt93:1433?database=resume2000&sql7=true","b2b","b2b!)$");
        try {
             Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
             Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@172.16.7.167:1521:labora01", "testing", "111111"); 
             Statement stmt = conn.createStatement();
             String str = "Create Table AAAA(BANNER_ID NUMBER(10) DEFAULT 0 NOT NULL, SUBJECT VARCHAR2(200 byte) NOT NULL)";
             //stmt.executeQuery(str);
             
             System.out.println("It's OK!!");
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}
