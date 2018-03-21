package test;

import java.awt.Dimension; 
import java.awt.Graphics; 
import java.awt.Image; 
import java.awt.image.BufferedImage; 
import java.io.File; 
import java.io.IOException; 
import java.net.MalformedURLException; 
import java.net.URL; 


import javax.imageio.ImageIO; 
import javax.swing.JDialog; 
import javax.swing.JEditorPane; 
import javax.swing.JFrame; 
import javax.swing.JScrollPane; 




public class HTMLtoIMGDemo extends JFrame {

	public static void main(String[] args) { 
        try { 
        URL myURL; 
                myURL = new URL("http://shopping.pchome.com.tw/"); 
        JFrame frame=new JFrame(); 
        JScrollPane scrollPane=new JScrollPane(); 
        JEditorPane tp1=new JEditorPane(); 
        frame.getContentPane().add(scrollPane); 
        scrollPane.getViewport().add(tp1); 
        tp1.setPage(myURL); 
        frame.validate(); 
        Thread.sleep(1000); 
        Dimension prefSize = tp1.getPreferredSize(); 
        tp1.setSize(prefSize); 
        Thread.sleep(1000); 
    BufferedImage img = new BufferedImage(prefSize.width, 
prefSize.height, BufferedImage.TYPE_INT_RGB); 
    Graphics graphics=img.createGraphics(); 
    tp1.paint(graphics); 
    ImageIO.write(img, "jpeg", new File("c:\\tom.jpg")); 
    System.exit(0); 
        } catch (MalformedURLException e) { 
                e.printStackTrace(); 
        } catch (IOException e) { 
                e.printStackTrace(); 
        } catch (InterruptedException e) { 
                e.printStackTrace(); 
        } 
} 



}
