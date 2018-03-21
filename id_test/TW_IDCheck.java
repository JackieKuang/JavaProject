package id_test;

public class TW_IDCheck {
	
	public static boolean IDCheck(String twID) {
		boolean flag = false;
		String id = twID.toUpperCase();
		char temp[] = id.toCharArray();
		String engNum = getNum(temp[0])+"";
		char[] N1 = engNum.toCharArray();
		
		int checkNum = (Integer.parseInt(N1[0]+"") + Integer.parseInt(N1[1]+"")*9 + Integer.parseInt(temp[1]+"")*8 + Integer.parseInt(temp[2]+"")*7 + Integer.parseInt(temp[3]+"")*6 + Integer.parseInt(temp[4]+"")*5 + Integer.parseInt(temp[5]+"")*4 + Integer.parseInt(temp[6]+"")*3 + Integer.parseInt(temp[7]+"")*2 + Integer.parseInt(temp[8]+"") + Integer.parseInt(temp[9]+""))%10;
		if(checkNum == 0) { 
			flag = true;
		}
		
		System.out.println(checkNum);
		return flag;
	}
	
	private static int getNum(char a) {
		int num2 = -1;
		char[] charList = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
		int[] num = {10,11,12,13,14,15,16,17,34,18,19,20,21,22,35,23,24,25,26,27,28,29,32,30,31,33};
		for(int i=0;i<charList.length; i++) {
			if(charList[i] == a) {
				num2 = num[i];
			}
		}
		
		return num2;
	}
}
