package stock5;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ReaderURL {
	public String readTextFile(String sFileName) throws Exception
    {
        StringBuffer sbStr = new StringBuffer();

        try
        {
        	URL url = new URL(sFileName);
        	URLConnection connection = url.openConnection();
        	connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT; DigExt)");
        	InputStream iss = connection.getInputStream();
    		BufferedInputStream bis = new BufferedInputStream(iss);

    		InputStreamReader isr = new InputStreamReader(bis);
    		BufferedReader br = new BufferedReader(isr);

            String dataLine = "";
            while (null != (dataLine = br.readLine()))
            {
                sbStr.append(dataLine);
                sbStr.append("\r\n");
            }

            br.close();
    		isr.close();
    		bis.close();
    		iss.close();
            
        }
        catch (Exception e)
        {
            throw e;
        }

        return sbStr.toString();
    }
	
	public String readTextFile2(String sFileName, String encoding) throws Exception
    {
        StringBuffer sbStr = new StringBuffer();

        try
        {
        	URL url = new URL(sFileName);
        	URLConnection connection = url.openConnection();
        	connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT; DigExt)");
        	InputStream iss = connection.getInputStream();
    		BufferedInputStream bis = new BufferedInputStream(iss);

    		InputStreamReader isr = new InputStreamReader(bis,encoding);
    		BufferedReader br = new BufferedReader(isr);

            String dataLine = "";
            while (null != (dataLine = br.readLine()))
            {
                sbStr.append(dataLine);
                sbStr.append("\r\n");
            }

            br.close();
    		isr.close();
    		bis.close();
    		iss.close();
            
        }
        catch (Exception e)
        {
            throw e;
        }

        return sbStr.toString();
    }
}
