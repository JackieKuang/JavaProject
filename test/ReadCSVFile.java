package test;

import java.io.*;

public class ReadCSVFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ReadCSVFile csvWrite = new ReadCSVFile();
		csvWrite.action();

	}
	
	public void action() throws Exception {
		File wfile = new File("C:\\Downloads\\split3.csv");
		BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(wfile),"BIG5"));
		FileInputStream fin = null;
		BufferedReader br = null;
		
		for(int i=1; i<=12; i++) {
			fin = new FileInputStream("C:\\Downloads\\0505\\part3\\"+i+".csv");
			br = new BufferedReader(new InputStreamReader(fin,"BIG5"));
			
			String dataLine = "";
	        while (null != (dataLine = br.readLine()))
	        {
	        	if(i!=1 && dataLine.indexOf("受測代碼")>=0) {
	        		continue;
	        	}
	        	fw.write("\r\n");
	        	fw.write(dataLine);
	        	System.err.println(dataLine);
	        }
		}
		
        fw.close();
        br.close();
        fin.close();
	}
}
