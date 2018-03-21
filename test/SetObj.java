package test;

import java.util.HashSet;

public class SetObj {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] arr = {"RD_製程開發","RD_製程開發","RD_製程開發","RD_製程開發","RD_光機開發","RD_光機開發","RD_面板開發","RD_面板開發","RD_面板開發","RD_產品開發","RD_產品開發","RD_電子電機開發","RD_電子電機開發","RD_電子電機開發","RD_電子電機開發","RD_系統開發","RD_系統開發","RD_系統開發","BU_業務","BU_業務","BU_產品行銷","BU_產品行銷","BU_客戶服務","BU_客戶服務","BU_產品應用","BU_產品應用","BU_產品專案","BU_產品專案","IU_人力資源管理","IU_人力資源管理","IU_人力資源管理","IU_人力資源管理","IU_人力資源管理","IU_人力資源管理","IU_財務管理","IU_財務管理","IU_財務管理","IU_稽核管理","IU_秘書助理","IU_法務智權","IU_法務智權","IU_法務智權","IU_法務智權","IU_法務智權","CIM/IT_IT工程師","CIM/IT_IT工程師","CIM/IT_IT工程師","CIM/IT_IT工程師","CIM/IT_CIM工程師","CIM/IT_CIM工程師","CIM/IT_CIM工程師","CIM/IT_CIM工程師","OU_品質工程","OU_品質工程","OU_品質工程","OU_品質工程","OU_品質工程","OU_品質工程","OU_品質工程","OU_設備工程師(原設備開發)","OU_設備工程師(原設備開發)","OU_設備工程師(原設備開發)","OU_設備工程師(原設備開發)","OU_設備工程師(原設備開發)","OU_設備工程師(原設備開發)","OU_設備工程師(原設備開發)","OU_設備工程師(原設備開發)","OU_設備工程師(原設備開發)","OU_設備工程師(原設備開發)","OU_製程工程師(原製程設備)","OU_製程工程師(原製程設備)","OU_製程工程師(原製程設備)","OU_製程工程師(原製程設備)","OU_製程工程師(原製程設備)","OU_製程工程師(原製程設備)","OU_製程工程師(原製程設備)","OU_測試整合","OU_測試整合","OU_測試整合","OU_測試整合","OU_測試整合","OU_測試整合","OU_測試整合","OU_製造課長","OU_工業工程","OU_自動化","OU_自動化","OU_廠務","OU_廠務","OU_廠務","OU_廠務","OU_廠務","OU_環安","OU_環安","SCM_運籌管理","SCM_運籌管理","SCM_運籌管理","SCM_運籌管理","SCM_採購","SCM_採購"};
		HashSet set = new HashSet();
		
		for(int i=0; i<arr.length; i++) {
			set.add(arr[i]);
		}
		
		System.err.print(set);
	}

}
