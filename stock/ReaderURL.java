package stock;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ReaderURL {
	public String getReader(String urlPath, int max, int min) throws Exception {
		URL url = new URL(urlPath);
		InputStream iss = url.openStream();
		BufferedInputStream bis = new BufferedInputStream(iss);

		InputStreamReader isr = new InputStreamReader(bis);
		BufferedReader br = new BufferedReader(isr);

		String seek = "";
		String strAll = "";
		int a1=0;
		while((seek=br.readLine())!=null) {
			a1++;
			if(a1>min && a1<max) {strAll += seek;}
		}
		
		strAll = strAll.replaceAll("'","");
		strAll = strAll.replaceAll("\"","");
		strAll = strAll.replaceAll(" ","");
		strAll = strAll.replaceAll("\r\n","");
		strAll = strAll.replaceAll("	","");
		
		br.close();
		isr.close();
		bis.close();
		iss.close();
		
		return strAll;
	}
}
