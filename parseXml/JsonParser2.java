package parseXml;

/**
 * 查公司統編/CUSTNO
 * http://www.104.com.tw/i/apis/jobsearch.cfm?fmt=4&cols=CUSTNO,NAME&pgsz=1&kws=company:(愛買_遠百企業股份有限公司) 
 */

import java.text.SimpleDateFormat;
import java.util.*;

import org.json.*;
import java.io.*;
import jxl.*;
import jxl.write.*;

/*
 * 104快報
 */
public class JsonParser2 {

	public static void main(String[] args) throws Exception {
		Date today = new Date();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
        String dateSrting = DATE_FORMAT.format(today);
        //System.out.println("Today in format : " + dateSrting);
        
		//Output Excel
		WritableWorkbook workbook=Workbook.createWorkbook(new File("C:\\104工作快報Excel_"+dateSrting+".xls"));//創建工作簿
		WritableFont myFont=new WritableFont(WritableFont.createFont("新細明體"),12);//設定字體
		WritableCellFormat nameCellFormat=new WritableCellFormat();//Colnum Name Cell格式
		nameCellFormat.setFont(myFont);//指定字型
		nameCellFormat.setBackground(Colour.GREY_25_PERCENT);//背景顏色
		nameCellFormat.setAlignment(jxl.format.Alignment.LEFT);//對齊方式
		nameCellFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK);
		WritableCellFormat valueCellFormat=new WritableCellFormat();//Colnum Value Cell格式
		valueCellFormat.setFont(myFont);//指定字型
		valueCellFormat.setBackground(Colour.WHITE);//背景顏色
		valueCellFormat.setAlignment(jxl.format.Alignment.LEFT);//對齊方式
		valueCellFormat.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN,jxl.format.Colour.BLACK);
		
		
		String[] catArr = {"2003001000","2010001000","2011002000","2001002000","2009003000","2002001000","2004003000","2011001000","2009001000","2007002000","2005001000"};
		String[] catArrName = {"會計_稅務類人員","操作_技術類人員","運輸物流類人員","人力資源類人員","品保_品管類人員","行政_總務類人員","專案管理類人員","採購類人員","生產管理類人員","MIS_網管類人員","售票_收銀人員"};
		//String[] catArr = {"2003001000"};
		
		for(int c=0; c<catArr.length; c++) {
			WritableSheet sheet0=workbook.createSheet(catArrName[c],c);//創建新的Sheet

			//Title
			sheet0.addCell(new Label(0,0,"公司名稱",nameCellFormat));
			sheet0.addCell(new Label(1,0,"職務名稱/工作內容",nameCellFormat));
			sheet0.addCell(new Label(2,0,"地區",nameCellFormat));
			sheet0.addCell(new Label(3,0,"學歷",nameCellFormat));
			sheet0.addCell(new Label(4,0,"經歷",nameCellFormat));
			sheet0.addCell(new Label(5,0,"待遇",nameCellFormat));
			sheet0.addCell(new Label(6,0,"上班時段",nameCellFormat));
			sheet0.addCell(new Label(7,0,"擅長工具",nameCellFormat));
			sheet0.addCell(new Label(8,0,"具備駕照",nameCellFormat));
			sheet0.addCell(new Label(9,0,"需求人數",nameCellFormat));
			
			ReaderURL readerURL = new ReaderURL();
			
			String jsonString = readerURL.readTextFile("http://www.104.com.tw/i/apis/jobsearch.cfm?role=1,4,2,5&fmt=8&area=6001000000&h_custnos=98770235000,97179430000,5714195000,21257316000,3521905000,83416573000,86517096001,36552651000,28435809000,5104840000,33977304000,3522600000,3244509000,36057744000,11332202002,70624227000,23932737000,23928945000,47224065000,53000774000,97311466000&cols=CUSTNO,NAME,JOB,JOB_ADDR_NO_DESCRIPT,MINBINARY_EDU,PERIOD,SALARY_HIGH,SALARY_LOW,WORKTIME,PCSKILL_ALL_DESC,DRIVER,S9,D3,S5,DESCRIPTION,NEED_EMP,NEED_EMP1&exp=5&exp_all=2&pgsz=500&page=1&cat="+catArr[c]);
			JSONObject sJon = new JSONObject(jsonString);
			int TOTALPAGE = Integer.parseInt(sJon.getString("TOTALPAGE"));
			
			int closNum = 1;
			for(int p=1; p<=TOTALPAGE; p++) {
				jsonString = readerURL.readTextFile("http://www.104.com.tw/i/apis/jobsearch.cfm?role=1,4,2,5&fmt=8&area=6001000000&h_custnos=98770235000,97179430000,5714195000,21257316000,3521905000,83416573000,86517096001,36552651000,28435809000,5104840000,33977304000,3522600000,3244509000,36057744000,11332202002,70624227000,23932737000,23928945000,47224065000,53000774000,97311466000&cols=CUSTNO,NAME,JOB,JOB_ADDR_NO_DESCRIPT,MINBINARY_EDU,PERIOD,SALARY_HIGH,SALARY_LOW,WORKTIME,PCSKILL_ALL_DESC,DRIVER,S9,D3,S5,DESCRIPTION,NEED_EMP,NEED_EMP1&exp=5&exp_all=2&pgsz=500&page="+p+"&cat="+catArr[c]);
				//System.err.println(jsonString);
				
				sJon = new JSONObject(jsonString);
				JSONArray jsonArr = (JSONArray)sJon.get("data");
				
				for(int i=0; i<jsonArr.length(); i++) {
					//System.err.println(jsonArr.get(i));
					JSONObject hashTmp = (JSONObject)jsonArr.get(i);
					
					//排除博碩士、限制4年以上~5年以下工作經驗、限制台灣本島
					int MINBINARY_EDU_INT = Integer.parseInt(hashTmp.getString("MINBINARY_EDU"));
					int PERIOD_INT = Integer.parseInt(hashTmp.getString("PERIOD"));
					if((MINBINARY_EDU_INT&16)==16 || (MINBINARY_EDU_INT&32)==32 || PERIOD_INT>5) {
						continue;
					}
					
					//Records
					sheet0.addCell(new Label(0,closNum,hashTmp.getString("NAME"),valueCellFormat));
					sheet0.addCell(new Label(1,closNum,hashTmp.getString("JOB"),valueCellFormat));
					sheet0.addCell(new Label(2,closNum,hashTmp.getString("JOB_ADDR_NO_DESCRIPT"),valueCellFormat));
					
					//1: 高中以下   2:高中   4:專科   8:大學   16碩士   32:博士
					String MINBINARY_EDU_DESC = "";
					if((MINBINARY_EDU_INT&1)==1) {
						MINBINARY_EDU_DESC = "高中以下";
					}
					if((MINBINARY_EDU_INT&2)==2) {
						MINBINARY_EDU_DESC = "高中";
					}
					if((MINBINARY_EDU_INT&4)==4) {
						MINBINARY_EDU_DESC = "專科";
					}
					if((MINBINARY_EDU_INT&8)==8) {
						MINBINARY_EDU_DESC = "大學";
					}
					if((MINBINARY_EDU_INT&16)==16) {
						MINBINARY_EDU_DESC = "碩士";
					}
					if((MINBINARY_EDU_INT&32)==32) {
						MINBINARY_EDU_DESC = "博士";
					}
					sheet0.addCell(new Label(3,closNum,MINBINARY_EDU_DESC,valueCellFormat));
					
					/*
					 工作經驗：PERIOD
					00=工作經歷不拘
					01=1年以下
					02=1年以上
					03=2年以上
					04=3年以上
					05=4年以上
					06=5年以上
					07=6年以上
					08=7年以上
					09=8年以上
					10=9年以上
					 */
					String PERIOD_DESC = "";
					switch(PERIOD_INT) {
					case 0:
						PERIOD_DESC = "工作經歷不拘";
						break;
					case 1:
						PERIOD_DESC = "1年以下";
						break;
					case 2:
						PERIOD_DESC = "1年以上";
						break;
					case 3:
						PERIOD_DESC = "2年以上";
						break;
					case 4:
						PERIOD_DESC = "3年以上";
						break;
					case 5:
						PERIOD_DESC = "4年以上";
						break;
					case 6:
						PERIOD_DESC = "5年以上";
						break;
					case 7:
						PERIOD_DESC = "6年以上";
						break;
					case 8:
						PERIOD_DESC = "7年以上";
						break;
					case 9:
						PERIOD_DESC = "8年以上";
						break;
					case 10:
						PERIOD_DESC = "9年以上";
						break;
					}
					sheet0.addCell(new Label(4,closNum,PERIOD_DESC,valueCellFormat));
					
					/*
					 薪資：SALARY_HIGH(最高) SALARY_LOW(最低)
					希望待遇(LOW)單位K--------------------------------- (全職) 
					10:1萬 
					20:2萬... 
					160:16萬 
					170:17萬 (17→萬 0→千) 
					990:面議 
					000:依公司規定 
					 */
					int SALARY_LOW_INT = Integer.parseInt(hashTmp.getString("SALARY_LOW"));
					int SALARY_HIGH = Integer.parseInt(hashTmp.getString("SALARY_HIGH"));
					String SALARY_DESC = "";
					if(SALARY_LOW_INT == 0 || SALARY_HIGH == 0) {
						SALARY_DESC = "依公司規定 ";
					}
					else if(SALARY_LOW_INT == 990 || SALARY_HIGH == 990) {
						SALARY_DESC = "面議  ";
					}
					else {
						SALARY_DESC  = (SALARY_LOW_INT/10) + "萬 ~ " + (SALARY_HIGH/10) + "萬";
					}
					sheet0.addCell(new Label(5,closNum,SALARY_DESC,valueCellFormat));
					
					/*
					 上班時段目前由下列這三個欄位組成：
					s9：上班時段，預設:0
					        1:日班
					        2:晚班
					        4:大夜班
					        8:假日班
					S5(二進位制)： 有256這個值代表該職缺"需輪班"
					D3：此欄是由客戶自行輸入，若為null或空字串代表客戶未填寫
					 */
					int S9_INT = Integer.parseInt(hashTmp.getString("S9"));
					int S5_INT = Integer.parseInt(hashTmp.getString("S5"));
					String S9_DESC = "";
					String S5_DESC = "";
					String D3_DESC = "";
					if((S9_INT&1) == 1) {
						S9_DESC += "日班,";
					}
					if((S9_INT&2) == 2) {
						S9_DESC += "晚班,";
					}
					if((S9_INT&4) == 4) {
						S9_DESC += "大夜班,";
					}
					if((S9_INT&8) == 8) {
						S9_DESC += "假日班,";
					}
					if((S5_INT&256) == 256) {
						S5_DESC = "需輪班,";
					}
					if(!"null".equals(hashTmp.getString("D3"))) {
						D3_DESC = hashTmp.getString("D3");
					}
					
					sheet0.addCell(new Label(6,closNum,S9_DESC+S5_DESC+D3_DESC,valueCellFormat));
					
					sheet0.addCell(new Label(7,closNum,hashTmp.getString("PCSKILL_ALL_DESC"),valueCellFormat));
					
					/*
					 駕照(二進位制)：DRIVER
					1 輕型機車 
					2 普通重型機車  
					4 大型重型機車  
					8 普通小型車  
					16 普通大貨車  
					32 普通大客車  
					64 普通聯結車  
					128 職業小型車  
					256 職業大貨車  
					512 職業大客車  
					1024 職業聯結車 
					例如：DRIVER=10，則解析出來為：普通重型機車、普通小型車 
					 */
					int DRIVER_INT = Integer.parseInt(hashTmp.getString("DRIVER"));
					String DRIVER_DESC = "";
					if((DRIVER_INT&1) == 1) {
						DRIVER_DESC += "輕型機車,";
					}
					if((DRIVER_INT&2) == 2) {
						DRIVER_DESC += "普通重型機車,";
					}
					if((DRIVER_INT&4) == 4) {
						DRIVER_DESC += "大型重型機車,";
					}
					if((DRIVER_INT&8) == 8) {
						DRIVER_DESC += "普通小型車,";
					}
					if((DRIVER_INT&16) == 16) {
						DRIVER_DESC += "普通大貨車,";
					}
					if((DRIVER_INT&32) == 32) {
						DRIVER_DESC += "普通大客車,";
					}
					if((DRIVER_INT&64) == 64) {
						DRIVER_DESC += "普通聯結車,";
					}
					if((DRIVER_INT&128) == 128) {
						DRIVER_DESC += "職業小型車,";
					}
					if((DRIVER_INT&256) == 256) {
						DRIVER_DESC += "職業大貨車,";
					}
					if((DRIVER_INT&512) == 512) {
						DRIVER_DESC += "職業大客車,";
					}
					if((DRIVER_INT&1024) == 1024) {
						DRIVER_DESC += "職業聯結車,";
					}
					
					sheet0.addCell(new Label(8,closNum,DRIVER_DESC,valueCellFormat));
					
					/*
					 需求人數
					NEED_EMP (需求人數最低)
					NEED_EMP1 (需求人數最高) 
					NEED_EMP=0 and NEED_EMP1=0 (不拘)
					NEED_EMP=0 and NEED_EMP1=99 (不拘)
					NEED_EMP=99 and NEED_EMP1=99 (不拘)
					*/
					int NEED_EMP = 0;
					int NEED_EMP1 = 0;
					if(!"".equals(hashTmp.getString("NEED_EMP"))) {
						NEED_EMP = Integer.parseInt(hashTmp.getString("NEED_EMP"));
					}
					if(!"".equals(hashTmp.getString("NEED_EMP1"))) {
						NEED_EMP1 = Integer.parseInt(hashTmp.getString("NEED_EMP1"));
					}
					
					String NEED_EMP_DESC = "";
					
					if(NEED_EMP==0 && NEED_EMP1==0) {
						NEED_EMP_DESC = "不拘";
					}
					else if(NEED_EMP==0 && NEED_EMP1==99) {
						NEED_EMP_DESC = "不拘";
					}
					else if(NEED_EMP==99 && NEED_EMP1==99) {
						NEED_EMP_DESC = "不拘";
					}
					else if(NEED_EMP1 <= NEED_EMP) {
						NEED_EMP_DESC = NEED_EMP+"";
					}
					else {
						NEED_EMP_DESC = NEED_EMP+" ~ "+NEED_EMP1;
					}
					
					sheet0.addCell(new Label(9,closNum,NEED_EMP_DESC,valueCellFormat));
					
					closNum++;
					sheet0.addCell(new Label(1,closNum,hashTmp.getString("DESCRIPTION"),valueCellFormat));
					
					closNum += 2;
				}
				
				System.err.println("c="+(c+1)+"/"+catArr.length+",CAT="+catArr[c]+"-"+catArrName[c]+",Page="+p+"/"+TOTALPAGE);
			}
		}
		
		//把創建的内容寫入到輸出流中，並關閉輸出流
		workbook.write();
		workbook.close();
		
		System.err.println("Excel Export Finished!!");
	}

}
