package Chatting;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

class Emoticon extends JDialog{
	JButton[] emotibtn;
	ImageIcon[] img = {new ImageIcon("imgs/1.png"),new ImageIcon("imgs/2.png"),
			new ImageIcon("imgs/3.png"),new ImageIcon("imgs/4.png"),
			new ImageIcon("imgs/5.png"),new ImageIcon("imgs/6.png"),
			new ImageIcon("imgs/7.png"),new ImageIcon("imgs/8.png"),
			new ImageIcon("imgs/9.png")};
	Emoticon(JFrame frame, String title){
		super(frame,title,true);
		
		setSize(300,200);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JPanel emotipnl = new JPanel(new GridLayout(0,3,5,5));
		
		emotibtn = new JButton[9];
		for(int i=0; i<9; i++) {
			emotibtn[i] = new JButton(img[i]);
			emotibtn[i].setBorderPainted(false);
			emotibtn[i].setContentAreaFilled(false);
			emotibtn[i].setFocusPainted(false);
			emotipnl.setBackground(Color.white);
			emotipnl.add(emotibtn[i]);
			
		}
		add(emotipnl);
		pack();
	}
}
