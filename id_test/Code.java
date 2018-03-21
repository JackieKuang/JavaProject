package id_test;

import java.io.*;
import java.util.*;

public class Code implements Serializable{
	public Code(){}
	public String encode(String ss){
		String s=null;
		try{
			s=new String(ss.getBytes(),"Big5");
		}
		catch(Exception e){}
		String S0="    "+ss;
		if(s!=null && s.length()>0 && !S0.substring(S0.length()-4,S0.length()).equals("RJSS")){
			int len=s.length();
			String tmp=s;
			String result="";
			if(len%8!=0){
				for(int i=len%8;i<8;i++)tmp+="\uffff";
			}
			for(int i=0;i<tmp.length();i+=8)
			result+=getEncode(tmp.substring(i,i+8));
			result+="RJSS";
			return result.toUpperCase();
		}else{
			return s;
		}
	}
	public String decode(String s){
		String S0="    "+s;
		if(s!=null && s.length()>0 && S0.substring(S0.length()-4,S0.length()).equals("RJSS") && s.length()%48==4){
			String result="";
			String s0=s.substring(0,s.length()-4);
			for(int i=0;i<s0.length();i+=48)
			result+=getDecode(s0.substring(i,i+48));
			try{
				result=new String(result.getBytes("Big5"));
			}
			catch(Exception e){}
			return result;
		}else{
			return s;
		}
	}
	private String getEncode(String s){
		String[][] hex=toHexString(s);
		String[] sum=sum(hex);
		String[] transfer=transfer(hex);
		String[] xor=XOR(transfer,sum);
		String result="";
		int i=0;
		for(int k=0;k<8;k++){
			result+=xor[k];
			if(k%2==1)
			result+=sum[i++];
		}
		return result;
	}
	private String getDecode(String s){
		String[] hex=new String[12];
		String[] transfer=new String[8];
		String[] sum=new String[8];
		for(int i=0;i<s.length();i+=4)
		hex[i/4]=s.substring(i,i+4);
		int i=0;
		int j=0;
		for(int k=0;k<8;k++){
			transfer[k]="";
			transfer[k]+=hex[j];
			j++;
			if(k%2==1){
				sum[i]="";
				sum[i]+=hex[j];
				sum[7-i]=sum[i];
				i++;
				j++;
			}
		}
		String[] xor=XOR(transfer,sum);
		String[] x=new String[4];
		x[0]=xor[4]+xor[5];
		x[1]=xor[0]+xor[1];
		x[2]=xor[6]+xor[7];
		x[3]=xor[2]+xor[3];
		String tmp="";
		String oragin="";
		for(int l=0;l<8;l++){
			for(int m=0;m<4;m++)
			tmp+=x[m].substring(l,l+1);
			if(!tmp.equals("ffff"))
			oragin+=restoreHex(tmp);
			tmp="";
		}
		
		return oragin;
	}
	private String[] transfer(String[][] hex0){
		String[] result= new String[hex0[0].length];
		String[][] hex=new String[hex0.length][hex0[0].length];
		hex[0]=hex0[1];
		hex[1]=hex0[3];
		hex[2]=hex0[0];
		hex[3]=hex0[2];
		int k=0;
		for(int i=0;i<hex.length;i++){
				result[k]="";
				for(int j=0;j<hex[i].length/2;j++)
				result[k]+=hex[i][j];
				k++;
				result[k]="";
				for(int j=hex.length;j<hex[i].length;j++)
				result[k]+=hex[i][j];
				k++;
		}
		return result;
	}
	private String[] sum(String[][] hex){
		int[] sum=new int[hex[0].length];
		String[][] result=new String[hex.length/2][hex[0].length];
		for(int i=0;i<hex.length;i++){
			for(int j=0;j<hex[i].length;j++)
				sum[j]+=Integer.parseInt(hex[i][j],16);
		}
		for(int i=0;i<sum.length;i++){
			String s0="00"+sum[i];
			s0=s0.substring(s0.length()-2);
			for(int j=0;j<s0.length();j++)
			result[j][i]=""+s0.charAt(j);
		}
		String[] result0=new String [result[0].length];
		int k=0;
		for(int i=0;i<result.length;i++){
			result0[k]="";
			for(int j=0;j<result[i].length/2;j++)
			result0[k]+=result[i][j];
			result0[result0.length-1-k]=result0[k++];
			result0[k]="";
			for(int j=result[i].length/2;j<result[i].length;j++)
			result0[k]+=result[i][j];
			result0[result0.length-1-k]=result0[k++];
		}
		return result0;
	}
	private String[][] toHexString(String s){
		String[][] hex=new String[4][s.length()];
 		for (int i=0; i<s.length(); i++){
   			int ch=(int)s.charAt(i);
   			String s4="0000"+Integer.toHexString(ch);
   			s4=s4.substring(s4.length()-4);
	   		for(int j=0;j<4;j++)
	   		hex[j][i]=""+s4.charAt(j);
	   	}
		return hex;
	}
	private String[] XOR(String[] transfer,String[] sum){
		String[] result=new String[sum.length];
		for(int i=0;i<sum.length;i++){
			result[i]="";
			for(int j=0;j<transfer[i].length();j++)
			result[i]+=Integer.toHexString(Byte.parseByte(""+transfer[i].charAt(j),16)^Byte.parseByte(""+sum[i].charAt(j),16));
		}
		return result;
	}
	private String restoreHex(String s2) {
		String sRestore = "";
		try{
			Vector v1 = new Vector();
			String str1 = "";
			int int1 = 0;
			v1 = getStrElement(s2.trim()," ");
			for (int i = 0 ; i < v1.size() ; i++){
				str1 = (String)v1.elementAt(i);
				int1 = Integer.parseInt(str1,16);
				sRestore = sRestore + (char)int1;
			}
		}catch(Exception exception){
			System.out.println("Exception:" + exception);
		}
		//try{
		//	sRestore=new String(sRestore.getBytes(),"Big5");
		//}
		//catch(Exception e){}
		return  sRestore;
	}
	private Vector getStrElement(String strOriginal, String strDivide){
		Vector vtStrElement = new Vector();
		try{
			if (strOriginal.length()> 0){
				int index = 0;
				String strTmp = "";
				int intLenDivide = strDivide.length();
				while (strOriginal.indexOf(strDivide) > -1) {
					index = strOriginal.indexOf(strDivide);
					strTmp = strOriginal.substring(0,index).trim();
					strOriginal = strOriginal.substring(index + intLenDivide);
					vtStrElement.addElement(strTmp);
				}
				vtStrElement.addElement(strOriginal);
			}else{
				vtStrElement.addElement("");
			}
		}catch(Exception e){
			System.out.println("DealStr : " + e);
		}
		return vtStrElement;
	}
	public void destroy(){
		System.gc();
	}
} 