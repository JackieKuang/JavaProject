package parseXml;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;

public class ParserXML {

	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		//Document doc = db.parse("http://login.e104.com.tw/api/edm.cfm?usr=test@test.com.tw&site=999&paper_list=9,11&subscribe_list=1,0");
		//Document doc = db.parse(new File("D:\\Work\\eclipse-SDK-3.1.2\\eclipse\\workspace\\JavaProject\\parseXml\\edm.xml"));
		args[0] = "http://www.104.com.tw/i/apis/jobsearch.cfm?role=1&fmt=4&custno=98770235000,97179430000,5714195000,21257316000,3521905000,83416573000,86517096001,36552651000,28435809000,5104840000,33977304000,3522600000,3244509000,36057744000,11332202002,70624227000,23932737000,23928945000,47224065000,53000774000,97311466000&cols=CUSTNO,NAME,JOB,JOB_ADDR_NO_DESCRIPT,MINBINARY_EDU,PERIOD,SALARY_HIGH,SALARY_LOW,WORKTIME,PCSKILL_ALL_DESC,DRIVER,S9,D3,S5,DESCRIPTION&exp=5&exp_all=2&pgsz=500&page=1";
		//Document doc = db.parse(args[0]);
		//drawDom(doc);
	}
	
	//處理DOM
	public static void drawDom(Node node) {
		System.err.println("");
		NodeList childNodes = node.getChildNodes();
		Element element;

		System.err.println("NodesLength:"+childNodes.getLength());
		for(int i=0; i < childNodes.getLength(); i++)
	    {
			element = (Element)childNodes.item(i);
			//System.err.println("TOTAL_CNT:"+element.getAttribute("TOTAL_CNT"));
			//System.err.println("ERR_MSG:"+element.getAttribute("ERR_MSG"));
			//System.err.println("AUTO_NO:"+element.getAttribute("AUTO_NO"));
			//System.err.println("getNodeName:"+element.getNodeName());
			//System.err.println("getNodeValue:"+element.getNodeValue());
			
			
			if(element.getNodeName().equals("EPAPER")) {
				System.err.println("nodeName="+element.getNodeName()+",value="+element.getNodeValue());
				Hashtable hash1 = drawAttribute(element);
				System.err.println(element.getNodeName()+"_Attuibute="+hash1);
			}
			else if(element.getNodeName().equals("EPAPERITEM")) {
			    System.err.println("nodeName="+element.getNodeName());
			    Hashtable hash2 = drawAttribute(element);
			    System.err.println(element.getNodeName()+"_Attuibute="+hash2);
			}
			
			if(element.hasChildNodes())
				drawDom(element);
		}
	}
	
	//處理 Attribute
	public static Hashtable drawAttribute(Node nade) {
		Hashtable hash = new Hashtable();
		
		NamedNodeMap attributes = nade.getAttributes();
		System.err.println("AttLength:"+attributes.getLength());
		for (int j=0; j<attributes.getLength(); j++) {
			Node current = attributes.item(j);
			System.err.println(" " + current.getNodeName() +
			"=\"" + current.getNodeValue() +
			"\"");
			hash.put(current.getNodeName(),current.getNodeValue());
		}
		return hash;
	}

}
