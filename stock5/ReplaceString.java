package stock5;

import java.util.*;

public class ReplaceString {
	
	public static String getStr(String s1) {
		String str = s1;
		str = str.replaceAll("&nbsp;","");
		str = str.replaceAll(" ","");
		
		return str;
	}
	
	public static String getSubString(String s1, int startNum, int endNum) {
		String[] st = s1.split(",");
		String s1Temp = "";
		String stStr = "";
		
		for(int i=0; i<st.length; i++) {
			stStr = st[i];
			if(i>=startNum && i<=endNum) {
				s1Temp += stStr+",";
				//System.out.print("s2:"+stStr+",");
			}
		}
		
		return s1Temp;
	}
	
	public static String getSubString2(String s1, int startNum, int endNum) {
		String[] st = s1.split(",");
		String[] s1Temp={"",""};
		String s2Temp = "";
		String stStr = "";
		
		for(int i=0; i<st.length; i++) {
			stStr = st[i];
			if(i==startNum) {
				s1Temp[0] = stStr;
				//System.out.println("startNum:"+i);
			}
			if(i==endNum) {
				s1Temp[1] = stStr;
				//System.out.println("endNum:"+i);
			}
			
			if(i == endNum) {
				s2Temp += s1Temp[0]+s1Temp[1]+",";
				//System.out.println("su:"+i);
			}
			
			if(i!=startNum && i!=endNum) {
				s2Temp += stStr+",";
				//System.out.println("el:"+i);
			}
			
		}
		//System.out.println(s2Temp);
		 
		return s2Temp;
	}
	
	public static String deleteStr(String s1, int num) {
		String[] st = s1.split(",");
		String stStr = "";
		
		for(int i=0; i<st.length; i++) {
			if(num!=i) {
				stStr += st[i]+",";
			}
		}
		
		return stStr;
	}
}
