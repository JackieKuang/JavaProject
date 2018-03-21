package test;

import java.util.HashSet;
import java.util.Hashtable;

public class TestHashSet {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Hashtable hash1 = new Hashtable();
		hash1.put("a1", "111");
		hash1.put("a2", "222");
		hash1.put("a3", "333");
		hash1.put("a4", "444");
		
		Hashtable hash2 = new Hashtable();
		hash2.put("a1", "111");
		hash2.put("a2", "222");
		hash2.put("a3", "333");
		hash2.put("a4", "444");
		
		Hashtable hash3 = new Hashtable();
		hash3.put("a1", "1111");
		hash3.put("a2", "2222");
		hash3.put("a3", "3333");
		hash3.put("a4", "4444");
		
		HashSet set = new HashSet();
		set.add(hash1);
		set.add(hash2);
		set.add(hash3);
		
		System.err.println(set);
		
		int str = true?1:2;
		String str1 = "YES".equals("YES")? "YES":"NO";
		System.err.println(str1);
		
		
	}

}
