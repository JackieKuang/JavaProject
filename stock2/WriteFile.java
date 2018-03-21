package stock2;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class WriteFile {
	FileWriter fw;
	BufferedWriter bw;
	String sFile = "";
	
	public WriteFile(String sFile) throws Exception{
		this.sFile = sFile;
	}
	
	public void writeFile(String str) throws Exception {
		fw = new FileWriter(sFile,true);
		bw = new BufferedWriter(fw);
		bw.write(str);
		bw.flush();
	}
	
	public void closeFile() throws Exception {
		bw.close();
		fw.close();
	}
}
