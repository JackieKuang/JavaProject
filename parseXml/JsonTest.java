package parseXml;

import java.util.*;

import org.json.*;

public class JsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		//Json to String
		Hashtable hash = new Hashtable();
		hash.put("a1", "a1");
		hash.put("a2", "a2");
		
		ArrayList arr1 = new ArrayList();
		Hashtable hash1 = new Hashtable();
		hash1.put("s1", "1111");
		hash1.put("s2", "2222");
		arr1.add(hash1);
		
		Hashtable hash2 = new Hashtable();
		hash2.put("s1", "3333");
		hash2.put("s2", "4444");
		arr1.add(hash2);
		
		hash.put("arr", arr1);
		
		JSONObject sJon1 = new JSONObject(hash);
		System.err.println("JsonString="+sJon1.toString());
		System.err.println("");
		
		//String to Json
		System.err.println("String to Json");
		JSONObject sJon = new JSONObject(hash);
		JSONArray jsonArr = (JSONArray)sJon.get("arr");
		
		System.err.println(sJon.getString("a1"));
		System.err.println(sJon.getString("a2"));
		for(int i=0; i<jsonArr.length(); i++) {
			JSONObject jsonObj = (JSONObject)jsonArr.get(i);
			
			System.err.println(jsonObj.getString("s1"));
			System.err.println(jsonObj.getString("s2"));
		}
	}

}
