package tbc;

import com.tbc.util.*;

public class EncodeTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//设置通用对称加密密码。最终我们会约定这个密码。
		CryptUtil.setPassword("tbc-104_estimate");
		
		//对测试数据加密
		String testData = "设置通用对称加密密码。最终我们会约定这个密码。";
		String edata = CryptUtil.encrypt(testData);
		System.err.println(edata);
		
		//对加密的数据解密。
		String ddata = CryptUtil.decrypt(edata);
		System.err.println(ddata);
		
		
	}

}
