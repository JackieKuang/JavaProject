package stock3;

import java.io.*;

public class WriteFile {
	FileWriter fw;
	File file = null;
	BufferedWriter bw;
	
	public WriteFile(String sPath, String sFile) throws Exception{
		file = new File(sPath,sFile);
		File file2 = new File(sPath);
		
		if(!file2.exists()){
			file2.mkdirs();
		}
	}
	
	public void writeFile(String str) throws Exception {
		fw = new FileWriter(file,true);
		bw = new BufferedWriter(fw);
		bw.write(str);
		bw.flush();
	}
	
	public void closeFile() throws Exception {
		bw.close();
		fw.close();
	}
}
