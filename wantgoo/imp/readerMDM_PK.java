package wantgoo.imp;

import java.io.BufferedReader;
import java.io.FileReader;

//股價淨值比 = 收盤價/每股淨值。

public class readerMDM_PK {

	public static void main(String[] args) throws Exception {
		FileReader fr = new FileReader("C:\\mdm_pk.txt");
		BufferedReader br = new BufferedReader(fr);
		String s = null;
		
		while((s=br.readLine())!=null) {
			String s1 = "{\"" + s.replaceAll("\t","\",\"") + "\"},";
			
			
			System.err.println(s1);
		}
		
		
	}

}
