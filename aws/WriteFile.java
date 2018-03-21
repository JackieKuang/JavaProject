package aws;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

public class WriteFile {
	OutputStreamWriter out;
	
	public WriteFile(String sFile) throws Exception{
		out = new OutputStreamWriter(new FileOutputStream(sFile),"UTF-8");
	}
	
	public WriteFile(String sFile,String encoding) throws Exception{
		out = new OutputStreamWriter(new FileOutputStream(sFile),encoding);
	}
	
	public void write(String str) throws Exception {
		out.write(str); 
		out.flush(); 
	}
	
	public void writeLine(String str) throws Exception {
		out.write(str+"\n"); 
		out.flush(); 
	}
	
	public void closeFile() throws Exception {
		if(out!=null) {out.close();}
	}
}
