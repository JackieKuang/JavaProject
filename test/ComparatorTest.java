package test;

import java.util.*;

public class ComparatorTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Hashtable t1 = new Hashtable();
		Hashtable t2 = new Hashtable();
		Hashtable t3 = new Hashtable();
		Hashtable t4 = new Hashtable();
		Hashtable t5 = new Hashtable();
		
		t1.put("name","1");
		t1.put("val","5.67");
		
		t2.put("name","2");
		t2.put("val","1.38");
		
		t3.put("name","3");
		t3.put("val","3.33");
		
		t4.put("name","4");
		t4.put("val","6.74");
		
		t5.put("name","5");
		t5.put("val","5.67");
		
		
		ArrayList samples = new ArrayList();
		samples.add(t1);
		samples.add(t2);
		samples.add(t3);
		samples.add(t4);
		samples.add(t5);
		
		Collections.sort(samples, new Comparator(){
			public int compare(Object arg0, Object arg1) {
				Hashtable r0 = (Hashtable) arg0;
				
				Hashtable r1 = (Hashtable) arg1;
				if(Double.parseDouble(r1.get("val").toString())>Double.parseDouble(r0.get("val").toString()))return -1;
				
				else if(Double.parseDouble(r1.get("val").toString())==Double.parseDouble(r0.get("val").toString())) return 0;
				
				else return 1;
		}});
		
		for(int i=0; i<samples.size(); i++) {
			System.err.println(samples.get(i));
		}
	}

}
