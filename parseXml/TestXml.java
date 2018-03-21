package parseXml;

import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import jxl.*;
import jxl.write.*;


import java.util.*;

public class TestXml {
	
	int itemCount = 0;
	
	public static void main(String[] args) throws Exception {
		TestXml xmlParset = new TestXml();
		xmlParset.parserExecute();
	}
	
	public void parserExecute() throws Exception {
		//Output Excel
		WritableWorkbook workbook=Workbook.createWorkbook(new File("C:\\test.xls"));//創建工作簿
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

		WritableSheet sheet0=workbook.createSheet("遠東集團",0);//創建新的Sheet

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
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		//Document doc = db.parse("http://login.e104.com.tw/api/edm.cfm?usr=test@test.com.tw&site=999&paper_list=9,11&subscribe_list=1,0");
		//Document doc = db.parse(new File("D:\\Work\\eclipse-SDK-3.1.2\\eclipse\\workspace\\JavaProject\\parseXml\\edm.xml"));
		
		String s1 = "";
		for(int i=1; i<=11; i++) {
			s1 = "http://www.104.com.tw/i/apis/jobsearch.cfm?role=1&fmt=4&custno=98770235000,97179430000,5714195000,21257316000,3521905000,83416573000,86517096001,36552651000,28435809000,5104840000,33977304000,3522600000,3244509000,36057744000,11332202002,70624227000,23932737000,23928945000,47224065000,53000774000,97311466000&cols=CUSTNO,NAME,JOB,JOB_ADDR_NO_DESCRIPT,MINBINARY_EDU,PERIOD,SALARY_HIGH,SALARY_LOW,WORKTIME,PCSKILL_ALL_DESC,DRIVER,S9,D3,S5,DESCRIPTION&exp=5&exp_all=2&pgsz=100&page="+i;
			
			Document doc = db.parse(s1);
			drawDom(doc);
			
			System.err.println("page="+i+",itemCount="+itemCount);
		}
		
		//把創建的内容寫入到輸出流中，並關閉輸出流
		workbook.write();
		workbook.close();
	}
	
	//處理DOM
		public void drawDom(Node node) {
			
			
			System.err.println("");
			NodeList childNodes = node.getChildNodes();
			Element element;

			System.err.println("NodesLength:"+childNodes.getLength());
			for(int i=0; i < childNodes.getLength(); i++)
		    {
				//element = (Element)childNodes.item(i);
				if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE){
				    element = (Element) childNodes.item(i);
				    
				    //System.err.println("TOTAL_CNT:"+element.getAttribute("TOTAL_CNT"));
					//System.err.println("ERR_MSG:"+element.getAttribute("ERR_MSG"));
					//System.err.println("AUTO_NO:"+element.getAttribute("AUTO_NO"));
					//System.err.println("getNodeName:"+element.getNodeName());
					//System.err.println("getNodeValue:"+element.getNodeValue());
					
					
					if(element.getNodeName().equals("LIST")) {
						System.err.println("nodeName="+element.getNodeName()+",value="+element.getNodeValue());
						Hashtable hash1 = drawAttribute(element);
						System.err.println(element.getNodeName()+"_Attuibute="+hash1);
					}
					else if(element.getNodeName().equals("ITEM")) {
						itemCount++;
					    System.err.println("nodeName="+element.getNodeName());
					    Hashtable hash2 = drawAttribute(element);
					    System.err.println(element.getNodeName()+"_Attuibute="+hash2);
					}
					
					if(element.hasChildNodes())
						drawDom(element);
				}
				
			}
			
		}
		
		//處理 Attribute
		public Hashtable drawAttribute(Node nade) {
			Hashtable hash = new Hashtable();
			
			NamedNodeMap attributes = nade.getAttributes();
			System.err.println("AttLength:"+attributes.getLength());
			for (int j=0; j<attributes.getLength(); j++) {
				Node current = attributes.item(j);
				//System.err.println(" " + current.getNodeName() +
				//"=\"" + current.getNodeValue() +
				//"\"");
				hash.put(current.getNodeName(),current.getNodeValue());
			}
			return hash;
		}
}
