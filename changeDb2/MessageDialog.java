package changeDb2;

import java.awt.*;
import java.awt.event.*;

public class MessageDialog extends Dialog implements ActionListener, WindowListener {
	
	MessageDialog(Frame f, String s, boolean b, String message, StartDialog sd) {
		super(f,s,b);
		sd.setVisible(false);
		Panel pan = new Panel();
		Panel pan2 = new Panel();
		
		Label label = new Label(message,Label.CENTER);
		Button b1 = new Button("½T©w");
		b1.addActionListener(this);
		pan.add(label);
		pan2.add(b1);
		add(pan,"Center");
		add(pan2,"South");
		setBounds(300,300,400,100);
		pack();
	}
	
	public void actionPerformed(ActionEvent av) {setVisible(false);}
	
	public void windowOpened(WindowEvent e){}
	
	public void windowClosing(WindowEvent e) {System.exit(0);}
	
	public void windowClosed(WindowEvent e) {}
	
	public void windowIconified(WindowEvent e) {}
	
	public void windowDeiconified(WindowEvent e) {}
	
	public void windowActivated(WindowEvent e) {}
	
	public void windowDeactivated(WindowEvent e) {}
	
}