/*
dbMode:目的端
 sourceDb:來源端
 */

package changeDb2;

import tools.StringReplace;
import java.util.StringTokenizer;

public class DbColumnType {
    String value_list = "";
    StringReplace replace;
    StringTokenizer st = null;
    public DbColumnType(){
        replace = new StringReplace();	
    }
    
    public String getTypeString(String s, int i, String dbMode, String sourceDb) {
            //Varchar2 Type(12),Number Type(2,4,-6,8,6)
            if(i == 12 || i == 2 || i==4 || i==-6 || i==8 || i==6) {
                    if(s == null || s.trim().equals("")) {
                            if(i==12) value_list = "' ',";
                            else value_list = "'0',";
                    }
                    else {
                        try {
                            if(dbMode.equals("MYSQL") && sourceDb.equals("Oracle"))
                                value_list = replace.string_Replace(new String(s.getBytes(),"ISO8859_1"),"'","''","all");
                            else if(dbMode.equals("Oracle") && sourceDb.equals("MSSQL"))
                                value_list = replace.string_Replace(new String(s.getBytes("iso8859-1"),"big5"),"'","''","all");
                            else
                                value_list = replace.string_Replace(s,"'","''","all");
                                
                            value_list = "'" + replace.string_Replace(value_list,"‧","、","all") + "',";
                        }
                        catch(Exception e) {
                            System.out.println(e);
                        }
                    }
            }

            //Date Type(91)
            if(i == 91) {
                int flag = 0;
                String Str_t = "";
                if((dbMode.equals("Oracle") && sourceDb.equals("MYSQL")) || (dbMode.equals("Oracle") && sourceDb.equals("MSSQL"))){
                    if(s == null || s.equals("0000-00-00 00:00:00") || s.equals("0000-00-00") || s.equals("0000-00-00 00:00:00.0")) {
                            //value_list = "TO_DATE('1900-01-01','YYYY-MM-DD HH24:MI:SS')" + ",";	//SYSDATE 為Oracle系統時間
                            value_list = "NULL,";
                    }
                    else {
                            flag = s.indexOf(".");
                            if(flag != -1)
                                Str_t = s.substring(0,flag);
                            else
                                Str_t = s;
                            
                            value_list = "TO_DATE('"+ Str_t +"','YYYY-MM-DD HH24:MI:SS')" + ",";
                    }
                }
            }
            //Oracle Date Type(91)
            if(i == 93) {
                    if(s == null) {
                            //value_list = "TO_DATE('1900-01-01','YYYY-MM-DD HH24:MI:SS')" + ",";	//SYSDATE 為Oracle系統時間
                            value_list = "NULL,";
                    }
                    else if((dbMode.equals("MSSQL") && sourceDb.equals("MYSQL")) || (dbMode.equals("MYSQL") && sourceDb.equals("MSSQL"))) {
                        if(s == null || s.equals("0000-00-00 00:00:00") || s.equals("0000-00-00") || s.equals("0000-00-00 00:00:00.0"))
                            value_list = "NULL,";
                        else
                            value_list = "'"+ s+"',";
                    }
                    else if((dbMode.equals("MYSQL") && sourceDb.equals("Oracle")) || (dbMode.equals("MSSQL") && sourceDb.equals("Oracle"))){
                            String timeTemp = s.substring(s.indexOf(" "),s.length());
                            st = new StringTokenizer(s.substring(0,s.indexOf(" ")),"/");
                            String s1[] = new String[3];
                            for(int j=0; st.hasMoreTokens(); j++) {
                                s1[j] = st.nextToken();
                                //System.out.println("s:"+s1[j]);
                            }
                            String s2 = s1[2] + "/" + s1[0] + "/" + s1[1] + timeTemp;
                            value_list = "'"+ s2+"',";
                            //System.out.println(timeTemp);
                            //value_list = "DATE_FORMAT('"+s2+"','%Y %m %d %T'),";
                    }
            }
            return value_list;
    }
}
