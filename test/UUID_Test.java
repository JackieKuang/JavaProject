package test;

import java.util.*;

public class UUID_Test {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		UUID uuid = UUID.randomUUID();
		System.err.println(uuid);
		
		long timeStemp = new Date().getTime();
		timeStemp = timeStemp+10;
		System.err.println(timeStemp);
		
		
		String s1 = HMACSHA1.calculateRFC2104HMAC("action=qryCarrierAgg&appID=EINV3201604298733&cardEncrypt=fd70&cardNo=/UJ.2AS2&cardType=3J0002&serial=0000000001&timeStamp="+(timeStemp)+"&uuid=25e1912c-d9ef-49a9-8a7b-f5a303cd3a31&version=1.0", "UTloMVR0OTQ0Y3ZZcXRZMw==");
		System.err.println(s1);
		
		ArrayList arr = new ArrayList();
		arr.add("1企劃1");
		arr.add("3工程1");
		arr.add("5企劃2");
		arr.add("2工程2");
		Collections.sort(arr);
		System.err.println(arr);
	}

}
