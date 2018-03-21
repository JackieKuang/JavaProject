
package test;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class HTML2IMG extends JFrame {

	public HTML2IMG(String url, File file) throws Exception {
	    JEditorPane editorPane = new JEditorPane();
	    editorPane.setEditable(false);
	    editorPane.setPage(url);
	    JScrollPane jsp = new JScrollPane(editorPane);
	    getContentPane().add(jsp);
	    this.setLocation(0, 0);
	 
	    Thread.sleep(10 * 1000);
	 
	    setSize(10000, 10000);
	    pack();
	    BufferedImage image = new BufferedImage(editorPane.getWidth(),
	        editorPane.getHeight(), BufferedImage.TYPE_INT_RGB);
	    Graphics2D graphics2D = image.createGraphics();
	    editorPane.paint(graphics2D);
	 
	    ImageIO.write(image, "jpg", file);
	    dispose();
	    
	    System.err.println("Create IMG OK!!");
	  }
	 
	  public static void main(String[] args) throws Exception {
	    new HTML2IMG("http://shopping.pchome.com.tw/", new File("C:\\file.jpg"));
	  }

}
