package test;

public class StringTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		String s = "select count(tu.STATUS) total from TB_USER tu inner join TB_REPORT_INDEX tr on tu.USER_ID=tr.USER_ID inner join TB_TEMPLATE tt on tu.TMPL_ID=tt.TMPL_ID left join TB_MANAGER tm on tu.MGR_ID=tm.MGR_ID where tu.CUSTNO=841497380000       and ((tt.CUSTNO in(0,1) and tt.LNG_ID='1') or (tt.CUSTNO='841497380000'))       and tu.RETRY_STATUS<>0 and tu.DEL_STATUS=0       and not (tt.EXAM_IDS=128 and instr(tt.DMS_IDS,'H01')=0) and instr(concat(',',tu.PERMISSION_IDS,','),',94733831906631981,')>0  /* Test_Bank  tb.obj.dao.result.ResultImpl.getAllData */";
//		
//		String s1 = "";
//		if("order by".equals(s)) {
//			s1 = s.substring(0, s.indexOf("order by"))+s.substring(s.indexOf("/*"), s.indexOf("*/")+2);
//		}
		
		String s1 = "";
//		for(int i=1; i<=100; i++) {
//			s1 += "t1.Q"+i+"_Y,t1.Q"+i+"_N,";
//		}
		for(int i=101; i<=224; i++) {
			s1 += "t1.Q"+i+"_Y,t1.Q"+i+"_N,";
		}
		
		System.err.println(s1);
	}

}
