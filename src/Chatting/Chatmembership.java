package Chatting;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Chatmembership extends JDialog {
	JTextField idtf;
	JPasswordField pwpf;
	JButton  okbtn,nobtn;
	Chatmembership(JFrame frame,String title){
		super(frame,title,true);
		setSize(300,200);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(frame);
		
		JPanel mempnl = new JPanel(new GridLayout(0,1,1,1));
		JPanel idpnl = new JPanel();
		JPanel pwpnl = new JPanel();
		JPanel btnpnl = new JPanel();
		
		JLabel idlbl = new JLabel("아이디");
		idlbl.setPreferredSize(new Dimension(70,15));
		JLabel pwlbl = new JLabel("비밀번호");
		pwlbl.setPreferredSize(new Dimension(70,15));
		
		idtf = new JTextField(10);
		pwpf = new JPasswordField(10);
				
		okbtn = new JButton("가입하기");
		nobtn = new JButton("취소");

		idpnl.add(idlbl);
		idpnl.add(idtf);
		
		pwpnl.add(pwlbl);
		pwpnl.add(pwpf);
		
		btnpnl.add(okbtn);
		btnpnl.add(nobtn);
		
		mempnl.add(idpnl);
		mempnl.add(pwpnl);
		mempnl.add(btnpnl);
		
		add(mempnl);
		pack();
	}

}
