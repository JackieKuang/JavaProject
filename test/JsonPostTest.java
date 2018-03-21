package test;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonPostTest {

	public static void main(String[] args) throws Exception {
		
		String query = "http://esb.cloud.s104.com.tw/services/ac/isValidAccount";
        //String json = "{\"loginid\": \"145aff2bd08c97898cd94690e8be0de4b\",\"password\": \"1ac8949f48e977e8423663346f5fbcb30\"}";
        
        Hashtable hash = new Hashtable();
        hash.put("loginid", "145aff2bd08c97898cd94690e8be0de4b");
        hash.put("password", "1ac8949f48e977e8423663346f5fbcb30");
        
        JSONObject jsonObject2 = new JSONObject(hash);
        String json = jsonObject2.toString();
        
        URL url = new URL(query);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");

        OutputStream os = conn.getOutputStream();
        os.write(json.getBytes("UTF-8"));
        os.close();

        // read the response
        InputStream in = new BufferedInputStream(conn.getInputStream());
        //String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
        
        String file = "";
        String UTF8 = "utf8";

        BufferedReader br = new BufferedReader(new InputStreamReader(in,
                UTF8));
        String str;
        while ((str = br.readLine()) != null) {
            file += str;
        }
        
       JSONObject jsonObject = new JSONObject(file);
       String error = jsonObject.getString("error");
       String success = jsonObject.getString("success");
       JSONObject jsonData = jsonObject.getJSONObject("data");
       String validity = jsonData.getString("validity");
	   String pid = jsonData.getString("pid");
	   String idno = jsonData.getString("idno");
       System.err.println("validity="+validity);
	   System.err.println("pid="+pid);
	   System.err.println("idno="+idno);
       
       in.close();
       conn.disconnect();
       
       System.err.println(jsonObject);
       System.err.println("error:"+error);
       System.err.println("success:"+success);
       
	}

}
