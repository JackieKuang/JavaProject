package testing_bank;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class GetProperty {
	 //根据key读取value
	 public static String readValue(String key) {
	  Properties props = new Properties();
	        try {
	        
	         InputStream is = GetProperty.class.getResourceAsStream("/testing_bank/info.properties");
	         InputStream in = new BufferedInputStream(is);
	         props.load(in);
	         String value = props.getProperty (key);
	            //System.out.println(key+value);
	            return value;
	        } catch (Exception e) {
	         e.printStackTrace();
	         return null;
	        }
	 }
	 
	 //读取properties的全部信息
	    public static void readProperties() {
	     Properties props = new Properties();
	        try {
	        	InputStream is = GetProperty.class.getResourceAsStream("/testing_bank/info.properties");
		         InputStream in = new BufferedInputStream(is);
	         props.load(in);
	            Enumeration en = props.propertyNames();
	             while (en.hasMoreElements()) {
	              String key = (String) en.nextElement();
	                    String Property = props.getProperty (key);
	                    //System.out.println(key+Property);
	                }
	        } catch (Exception e) {
	         e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	        System.out.println("OK");
	    	
	    }

}
