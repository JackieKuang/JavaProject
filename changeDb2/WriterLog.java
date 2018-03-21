package changeDb2;

import java.io.*;
import java.util.*;

public class WriterLog {
	FileWriter fw;
	BufferedWriter bw;
	
	WriterLog(String path) {
		
		try{
			fw = new FileWriter(path,true);
			bw = new BufferedWriter(fw);
		}
		catch(IOException e) {System.out.println(e.toString());}
		catch(Exception e) {System.out.println(e.toString());}
	}
	
	void writeLog(String err_message) {
		try{
			bw.write(err_message);
			bw.flush();
			bw = null;
			fw.close();
			fw = null;
		}
		catch(IOException e) {System.out.println(e.toString());}
		catch(Exception e) {System.out.println(e.toString());}
	}
}