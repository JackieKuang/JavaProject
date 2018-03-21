package test;

public class SerialNumGenerator {
	
	int strLen = 6;			// default length:6

	int num = 0;			// 隨機字符碼

	String outStr = "";		// 產生的密碼

	public String generator(int strLen){

		int num = 0;

		while(this.outStr.length() < strLen)  {

			num = (int)(Math.random()*(90-50+1))+50;	//亂數取編號為 50~90 的字符	(排除 0 和 1)

			if (num > 57 && num < 65)

				continue;			//排除非數字非字母

			else if (num == 73 || num == 76 || num == 79)

				continue;			//排除 I、L、O

			this.outStr += (char)num;

		}

		return this.outStr.toUpperCase();

	}// end of generator
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SerialNumGenerator sng = new SerialNumGenerator();
		String s1 = sng.generator(10);
		System.err.println(s1);
		
	}

}
