package test;

import java.awt.AWTException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;


public class ScreenCapturer2 {
	
  public static void main(String args[]) throws AWTException, IOException {
     // capture the whole screen
//	  Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize(); // 桌面屏幕尺寸
//	  System.err.println(scrSize.height);
//	  System.err.println(scrSize.width);
//     BufferedImage screencapture = new Robot().createScreenCapture(
//           new Rectangle(scrSize));
     
     //To capture a specific area 
     BufferedImage screencapture = new Robot().createScreenCapture(
    		   new Rectangle( 15, 15, 150, 2000));

     //To capture a specific visual object 
//     BufferedImage image = new Robot().createScreenCapture( 
//    		   new Rectangle( myframe.getX(), myframe.getY(), 
//    				myframe.getWidth(), myframe.getHeight() ) );




     // Save as JPEG
     File file = new File("c:\\screencapture.jpg");
     ImageIO.write(screencapture, "jpg", file);

     // Save as PNG
     // File file = new File("screencapture.png");
     // ImageIO.write(screencapture, "png", file);
  }
  
}

