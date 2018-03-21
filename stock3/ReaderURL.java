package stock3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class ReaderURL {
	public String readTextFile(String sFileName)
    {
        StringBuffer sbStr = new StringBuffer();

        try
        {
        	URL url = new URL(sFileName);
    		InputStream iss = url.openStream();
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
            System.out.println(e.toString());
        }

        return sbStr.toString();
    }
}
