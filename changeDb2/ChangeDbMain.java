package changeDb2;

import java.awt.*;
import java.awt.event.*;
import java.util.Properties;

public class ChangeDbMain extends Frame implements ActionListener, WindowListener {
    Button b;
    TextField tf;
    Checkbox ch1,ch2,ch3,ck1,ck2,ck3,skema,skema2;
    CheckboxGroup cg1,cg2;
    ChangeDbMain cdm2;
        
    public static void main(String[] args) {
        ChangeDbMain cdm = new ChangeDbMain();
        cdm.setObject(cdm);
	cdm.setVisible(true);
    }
    
    void setObject(ChangeDbMain cdm) {
	cdm2 = cdm;
    }
    
    ChangeDbMain() {
        setTitle("轉資料庫~~");
        setLayout(new BorderLayout());
        setBounds(300,300,400,150);
        setBackground(Color.orange);
        Panel p1 = new Panel();
        b = new Button("開始轉換");
        tf = new TextField("請輸入Table名稱",15);
        b.addActionListener(this);
        this.addWindowListener(this);
        p1.add(tf);
        p1.add(b);
        
        cg1 = new CheckboxGroup();
        cg2 = new CheckboxGroup();
        Panel p2 = new Panel(new BorderLayout());
        Panel p2_1 = new Panel();
        p2_1.setLayout(new FlowLayout(FlowLayout.LEFT));
        Label bel = new Label("請選擇資料庫型態");
        skema = new Checkbox("需自動產生Table",true);
        skema2 = new Checkbox("轉資料",true);
        ch1 = new Checkbox("MYSQL",cg1,true);
        ch2 = new Checkbox("Oracle",cg1,false);
        ch3 = new Checkbox("MSSQL",cg1,false);
        p2.add(bel,BorderLayout.NORTH);
        
        p2_1.add(ch1);
        p2_1.add(ch2);
        p2_1.add(ch3);
        p2_1.add(skema);
        p2_1.add(skema2);
        p2.add(BorderLayout.CENTER,p2_1);
        
        
        Panel p2_2 = new Panel();
        p2_2.setLayout(new FlowLayout(FlowLayout.LEFT));
        ck1 = new Checkbox("MYSQL",cg2,true);
        ck2 = new Checkbox("Oracle",cg2,false);
        ck3 = new Checkbox("MSSQL",cg2,false);
        p2_2.add(ck1);
        p2_2.add(ck2);
        p2_2.add(ck3);
        p2.add(BorderLayout.SOUTH,p2_2);
        
        add(BorderLayout.NORTH,p1);
        add(BorderLayout.CENTER,p2);
    }
    
    public void actionPerformed(ActionEvent av) {
            String cg1Str = cg1.getSelectedCheckbox().getLabel();   //來源
            String cg2Str = cg2.getSelectedCheckbox().getLabel();   //目的
            if(cg1Str.equals(cg2Str)) {
                StartDialog sd_dia = new StartDialog(cdm2,"資料庫錯誤....",true,"資料庫不可一致...","same");
            }
            else {
                //Object[] skemaSelect = skema.getSelectedObjects();
                //if(skemaSelect!=null && ((String)skemaSelect[0]).equals("YES")) {
                SkemaMessage skemaM = null;
                boolean flag = false;
                String msgTemp = "";
                if(skema.getState()) {
                    msgTemp = cg1Str+"--->"+cg2Str+"，Table 建構中.....請稍待!!";
                    skemaM = new SkemaMessage(cdm2,msgTemp,true,cg1Str,cg2Str,tf.getText());
                    flag = skemaM.flag;
                }
                else {
                    flag = true;
                }
                
                if((skema.getState() && flag && skema2.getState()) || (skema2.getState() && flag)) {
                    msgTemp = cg1Str+"--->"+cg2Str+"，轉換資料中....請勿重複執行！";
                    StartDialog sd_dia = new StartDialog(cdm2,"轉換資料中....",false,msgTemp,"not");
                    GetProperty gpt = new GetProperty();
                    Properties pt = gpt.setProperty(); 
                    ChangeDBThread cdt = new ChangeDBThread(pt,tf.getText(),cdm2,sd_dia);
                    cdt.setValues(cg1Str,cg2Str);
                    cdt.setConnection();

                    cdt.start();
                }
                
            }
    }

    public void windowOpened(WindowEvent e){}

    public void windowClosing(WindowEvent e) {System.exit(0);}

    public void windowClosed(WindowEvent e) {}

    public void windowIconified(WindowEvent e) {}

    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}
}
