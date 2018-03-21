package stock2;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.*;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;
import org.htmlparser.visitors.TextExtractingVisitor;


public class HTMLParserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String aFile = "D:\\Temp\\GOGOBOX\\stock_order\\test.html";
        String content = readTextFile(aFile, "BIG5");
        //System.out.println(content);
        test4(content);

	}
	
	/**
     * 按页面方式处理.对一个标准的Html页面,推荐使用此种方式.
     */
    public static void test4(String content) throws Exception
    {
        Parser myParser;
        myParser = Parser.createParser(content, "BIG5");

        HtmlPage visitor = new HtmlPage(myParser);

        myParser.visitAllNodesWith(visitor);

        String textInPage = visitor.getTitle();
        
        TableTag[] tableTag = visitor.getTables();
        NodeList nl = tableTag[0].getChildren();
        //CompositeTag ct = tableTag[0].getChildrenHTML();
        for(int i=0;i<tableTag.length;i++) {
        	//System.out.println(i);
        	//System.out.println(tableTag[i]);
        	//System.out.println("==============");
        }
        
        //NodeFilter filter=new TagNameFilter("font");
        
        System.out.println(tableTag[12].toHtml());
        System.out.println("========");
        System.out.println(tableTag[12].getRow(0).toHtml());
        System.out.println(tableTag[12].getRow(0).getColumnCount());
        //System.out.println(tableTag[12].getRow(1).getColumns()[0].toHtml());
        //System.out.println(tableTag[12].getRow(1).getColumns()[0].getFirstChild().toHtml());
        //System.out.println(tableTag[12].getRow(1).getColumns()[0].getChildren().toNodeArray()[1]);
        //System.out.println(tableTag[12].getRow(1).getColumns()[0].getChildren().extractAllNodesThatMatch(filter).toString());
        //System.out.println(tableTag[12].getRow(1).getColumns()[0].getChildren().toNodeArray()[tableTag[12].getRow(1).getColumns()[0].getChildren().size()-1]);
        System.out.println(tableTag[12].getRow(0).getColumns()[0].getChildren());
        
        NodeList a1 = tableTag[12].getRow(0).getColumns()[0].getChildren();
        Node[] n1 = a1.toNodeArray();
        for(int i=0; i<n1.length; i++) {
	        if (n1[i] instanceof TextNode) {
	        	TextNode textnode = (TextNode) n1[i];
	        	System.out.println(textnode.getText());
	        }
        }
        //System.out.println(tableTag[0].getChildren().toHtml());
        
        Parser parser = Parser.createParser(content, "BIG5");
        StringBuffer text = new StringBuffer();
        NodeList nodes = parser.extractAllNodesThatMatch(new NodeFilter(){
        	   public boolean accept(Node node) {
        	    return true;
        	   }});
        	  Node node = nodes.elementAt(0);
        	  text.append(node.toPlainTextString());
        	
        	  //System.out.println(textInPage);
        	  //System.out.println(text);

        
    }

    
	public static String readTextFile(String sFileName, String sEncode)
    {
        StringBuffer sbStr = new StringBuffer();

        try
        {
        	URL url = new URL("http://pchome.syspower.com.tw/stock/?stock_no=3130&item=12");
    		InputStream iss = url.openStream();
    		BufferedInputStream bis = new BufferedInputStream(iss);

    		InputStreamReader isr = new InputStreamReader(bis);
    		BufferedReader br = new BufferedReader(isr);

            String dataLine = "";
            while (null != (dataLine = br.readLine()))
            {
                sbStr.append(dataLine);
                sbStr.append("\r\n");
            }

            br.close();
    		isr.close();
    		bis.close();
    		iss.close();
            
        }
        catch (Exception e)
        {
            System.out.println(e.toString());
        }

        return sbStr.toString();
    }
    
    /*
    public static String readTextFile(String sFileName, String sEncode)
    {
        StringBuffer sbStr = new StringBuffer();

        try
        {
            File ff = new File(sFileName);
            InputStreamReader read = new InputStreamReader(new FileInputStream(ff),
                    sEncode);
            BufferedReader ins = new BufferedReader(read);

            String dataLine = "";
            while (null != (dataLine = ins.readLine()))
            {
                sbStr.append(dataLine);
                sbStr.append("\r\n");
            }

            ins.close();
        }
        catch (Exception e)
        {
        	System.out.println(e.toString());
        }

        return sbStr.toString();
    }
    */


}
