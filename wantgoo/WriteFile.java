package wantgoo;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

public class WriteFile {
	OutputStreamWriter out;
	
	
	public WriteFile(String sFile) throws Exception{
		out = new OutputStreamWriter(new FileOutputStream(sFile),"BIG5");
	}
	
	public WriteFile(String sFile,String encoding) throws Exception{
		out = new OutputStreamWriter(new FileOutputStream(sFile),encoding);
	}
	
	public void writeFile(String str) throws Exception {
		out.write(str); 
		out.flush(); 
	}
	
	public void closeFile() throws Exception {
		if(out!=null) {out.close();}
	}
}
