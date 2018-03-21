package test;

public class NumericValue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "104group";
		char[] arr = s.toCharArray();
		int numTotal = 0;
		
		for(int i=0; i<arr.length; i++) {
			System.err.println(Character.getNumericValue(arr[i]));
			numTotal += Character.getNumericValue(arr[i]);
		}
		
		if((numTotal+"").length()<8) {
			numTotal += 90000000;
		}
		
		System.err.println("numTotal="+numTotal);
	}

}
