package changeDb2;

import java.awt.*;
import java.awt.event.*;

public class SkemaMessage extends Dialog implements Runnable, ActionListener {
    String db1,db2,message,tableName;
    boolean flag = false;
    
    public SkemaMessage(Frame f, String s, boolean b, String d1, String d2, String t) {
        super(f,s,b);
        tableName = t;
        setMessage(s);
        setDB(d1,d2);
        setBounds(300,300,400,100);
        Thread th = new Thread(this);
        th.start();
        show();
    }
    
    public void setMessage(String s) {
        message = s;
    }
    
    public void setDB(String d1, String d2) {
        db1 = d1;
        db2 = d2;
    }
    
    public void paint(Graphics g) {
            g.drawString(message, 80, 40);
    }
    
    //public void update(Graphics g) {
    //        paint(g);
    //}
    
    public void SkemaClose() {
        setVisible(false);
    }
    
    public void run() {
        Skema sk = new Skema(db1,db2,tableName);  //建構執行 
        flag = sk.flag;
        
        if(flag) {
            successSkema(db1+"--->"+db2+"，建構成功!!");
        }
        else {
            successSkema(db1+"--->"+db2+"，建構失敗!!");
        }
    }
    
    public void successSkema(String s) {
        setMessage(s);
        Panel pan = new Panel();
        Panel pan2 = new Panel();

        Label label = new Label(message,Label.CENTER);
        Button b1 = new Button("確定");
        b1.addActionListener(this);
        pan.add(label);
        pan2.add(b1);
        add(pan,"Center");
        add(pan2,"South");
	show();
    }
    
    public void actionPerformed(ActionEvent e) {
        SkemaClose();
    }
    
}
