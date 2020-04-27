package Chatting;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RoomMaking extends JDialog  implements ActionListener{
	 JTextField maktf;
	 JButton makokbtn,maknobtn;
	 static String ss;
	
	RoomMaking(JFrame frame,String title){
		super(frame,title,true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(frame);
		JPanel makpnl = new JPanel(new GridLayout(0,1));
		JPanel roomtitle = new JPanel();
		JPanel btnpnl = new JPanel();
		
		JLabel maklbl = new JLabel("방 제목");
		maktf = new JTextField(10);
		makokbtn = new JButton("확인");
		maknobtn = new JButton("취소");
		
		roomtitle.add(maklbl);
		roomtitle.add(maktf);
		
		btnpnl.add(makokbtn);
		btnpnl.add(maknobtn);
		makokbtn.addActionListener(this);//방제목추가
		maknobtn.addActionListener(this);//방제목추가
		
		makpnl.add(roomtitle);
		makpnl.add(btnpnl);
		
		add(makpnl);
		pack();
	
		
		
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {//방제목추가
		if(e.getSource()==makokbtn){
			 ss = maktf.getText();//
			 this.setVisible(false);
		}
		else if(e.getSource()==maknobtn){
			this.setVisible(false);
		}
	}

}
