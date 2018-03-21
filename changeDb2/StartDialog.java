package changeDb2;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.image.BufferedImage;
import java.io.File;

public class StartDialog extends Dialog implements ActionListener {
	BufferedImage bi = null;
        String msStr="",msStr2="",sameDb="", Message="";
        int scale = 0;
        Button b1;
        int flag=0;
        
	StartDialog(Frame f, String s, boolean b, String message, String same) {
                super(f,s,b);    
                Message = message;    
                sameDb = same;
                if(sameDb.equals("same")) {
                  b1 = new Button("½T ©w");
                  b1.addActionListener(this);
                  add(b1,BorderLayout.SOUTH);
                }
                else {
                    try {
                        File ff = new File("line.gif");
                        bi = ImageIO.read(ff);
                        bi.flush();
                    }
                    catch(Exception e) {System.out.println("line.gif Error");}
                }
		setBounds(300,300,400,100);
                show();
	}
        
        public void setMessage(String message, String s, int i) {
                msStr = message;
                msStr2 = s;
                scale = 400/100;
                scale = i*scale;
                flag=1;
                repaint();
        }
        
        public void paint(Graphics g) {
            if(flag==0) {
                g.drawString(Message, 80, 40);
            }
            else {
                g.drawImage(bi, 0, 55, scale, 15, this);
                g.drawString(msStr, 80, 40);
                g.drawString(msStr2, 10, 67);
            }
        }
        
        //´î¤Ö°{Ã{
        //public void update(Graphics g) {
         //   paint(g);
        //}
        
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
        }
}