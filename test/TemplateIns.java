package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class TemplateIns {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String path = "C:/與人資學院職務職能對應.txt";
		BufferedReader br_temp = new BufferedReader(new InputStreamReader(new FileInputStream(path), "BIG5"));
		
		String positionId  = "";
		String positionName = "";
		String str = br_temp.readLine();
		String strList = "";
		while(str!=null) {
			String[] strSplit = str.split("\t");
			
			if(!positionId.equals(strSplit[0])) {
				int limit_time = 0;
				if(strList.length()>0) {
					strList = strList.substring(0, strList.length()-1);
					limit_time = strList.split(",").length*2;
					
					//32:管理者PK
					String sqlStr = "UUID_SHORT(),'"+positionName+"','288947050000',4,'"+strList+"',now(),100,32,"+limit_time;
					System.err.println(sqlStr);
					
					
					strList = "";
				}
				
				
				positionName = strSplit[1];
				if(!"".equals(strSplit[5])) {
					strList += strSplit[5]+",";
				}
			}
			else {
				if(!"".equals(strSplit[5])) {
					strList += strSplit[5]+",";
				}
				
			}
			
//			for(int i=0; i<strSplit.length; i++) {
//				System.err.println("str:"+strSplit[i]);
//				
//				if("".equals(strSplit[i])) {
//					System.err.println("Space");
//				}
//			}
//			System.err.println("=====");
			
			positionId = strSplit[0];
			str = br_temp.readLine();
			
			//Thread.sleep(500);
		}
		
		
	}

}
