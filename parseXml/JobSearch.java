package parseXml;

//import org.json.JSONObject;
//import org.json.XML;

import com.s104.net.SearchAdapter;

public class JobSearch {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// 參數說明
        // String SearchAdapter.search(url, executeFrom, IndexName, page, countPerPage, sortFieldName, sortFieldDesc, condition, function)
        // url - lab: search.e104.com.tw; production:job.search104.com
        // executeFrom - 任意字串, 以記錄執行來源, 一般傳入執行來源的程式名稱或功能名稱
        // IndexName - Index 名稱, 此處傳入 TwJobOn 代表查詢 jobon
        // page - 傳回第幾頁
        // countPerPage - 每頁有幾筆
        // sortFieldName - 排序用, 可不傳
        // sortFieldDesc - 排序用, 可不傳
        // condition - index 的查詢字串
        // function - 傳入 RETURNFUNCTION:XML2 以指定回傳 XML 格式(全部欄位)
        // 加傳 FIELDMAP 可指定回傳欄位
        // 例 RETURNFUNCTION:XML2@^#FIELDMAP:JOBNO;NAME
       //System.out.println(SearchAdapter.search("search.e104.com.tw", "executeFrom1", "TwJobOn", "1", "500", "", "", "CUSTNO:72055019000", "RETURNFUNCTION:XML2"));
		
		String xmlString = SearchAdapter.search("search.e104.com.tw", "executeFrom1", "TwJobOn", "1", "10", "", "", "+(ROLE:1 ROLE:4) -CUSTNO:87654321000 +JOB_ADDR_NO:6001?????? +PERIOD:[00 TO 06] -(INVOICE:98770235000 INVOICE:97179430000 INVOICE:5714195000 INVOICE:21257316000 INVOICE:3521905000 INVOICE:83416573000) -NAME:\"104 外 包 網\" -CUSTNO:89888888000", "RETURNFUNCTION:XML2@^#FIELDMAP:CUSTNO;NAME;JOB;JOB_ADDR_NO_DESCRIPT;MINBINARY_EDU;PERIOD;SALARY_HIGH;SALARY_LOW;WORKTIME;PCSKILL_ALL_DESC;DRIVER;S9;D3;S5;DESCRIPTION;NEED_EMP;NEED_EMP1");
		System.out.println(xmlString);
       
       
		//JSONObject jsonObj = XML.toJSONObject(xmlString); 
        //System.out.println(jsonObj);
        
       
	}
	
}
