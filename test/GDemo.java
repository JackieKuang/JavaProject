package test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
 
public class GDemo extends JFrame {
 
  int w=1024;
  int h=1500;
 
  public GDemo(String url, File file) throws Exception {
    JEditorPane editorPane = new JEditorPane();
    editorPane.setPreferredSize(new Dimension(w, h));
    editorPane.setEditable(false);
    editorPane.setPage(url);
    JScrollPane jsp=new JScrollPane(editorPane);
    getContentPane().add(jsp, BorderLayout.CENTER);  
 
    pack();
 
    try {
      Thread.sleep(5 * 1000);
    } catch (InterruptedException e) {
    }
 
    BufferedImage image = new BufferedImage(
      w, h, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics2D = image.createGraphics();
    editorPane.paint(graphics2D);
 
    ImageIO.write(image, "jpg", file);
    this.setLocation(0, 0);
    this.setSize(w, h);
    this.setVisible(true);
    
    dispose();
  }
 
  public static void main(String[] args) throws Exception {
    new GDemo("http://assessment.104china.com/talent/", new File("c:\\file.jpg"));
  }
}