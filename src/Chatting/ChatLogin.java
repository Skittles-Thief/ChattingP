package Chatting;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ChatLogin extends JFrame implements ActionListener,KeyListener{
	
	JPanel panNorth, panCenter, panSouth, ipPan, idPan,pwPan,waitingPan ,connectIPPan, userIDPan,userPWPan, rightPan, leftPan, mainPan, paintPan;
	JLabel ipLab, idLab,pwLab, empty, paintLab;
	JTextField connectIP, userID;
	JPasswordField userPW;
	JButton login,membership,  room1, room2, room3, room4,makroom;
	String ip = "", id = "",passwd="";
	String idu,pdu;
	ButtonGroup gp;
	boolean loginCheck = false;
	Chatmembership cmbs = new Chatmembership(this,"회원가입");

	FileWriter fw;
	
	ChatLogin(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		paintPan = new JPanel();
		paintLab = new JLabel(new ImageIcon("imgs/hi.gif"));
		paintPan.add(paintLab);
		paintLab.setLayout(new BorderLayout());
		panNorth = new JPanel();
		empty = new JLabel();
		empty.setPreferredSize(new Dimension(250,150));
		
		
		mainPan = new JPanel(new GridLayout(1,0));		
		leftPan = new JPanel(new BorderLayout());
		
		panNorth.add(empty);
		
		room1 = new JButton("1번방");
		room1.addActionListener(this);
		room2 = new JButton("2번방");
		room2.addActionListener(this);
		room3 = new JButton("3번방");
		room3.addActionListener(this);
		room4 = new JButton("4번방");		
		room4.addActionListener(this);
		
		panCenter = new JPanel();
		panCenter.setLayout(new GridLayout(0,1));
		ipPan = new JPanel();
		ipLab = new JLabel("접속 IP");
		ipPan.add(ipLab);
		idPan = new JPanel();
		idLab = new JLabel("ID");
		idPan.add(idLab);
		pwPan = new JPanel();
		pwLab = new JLabel("Password");
		pwPan.add(pwLab);
		connectIP = new JTextField(20);
		connectIP.setText("127.0.0.1");
		connectIPPan = new JPanel();
		connectIPPan.add(connectIP);
		userID = new JTextField(20);
		
		
		userIDPan = new JPanel();
		userIDPan.add(userID);
		userPW = new JPasswordField(20);
		userPWPan = new JPanel();
		userPWPan.add(userPW);
		userPW.addKeyListener(this);
		
		panCenter.add(ipPan);
		ipPan.setOpaque(false);
		panCenter.add(connectIPPan);
		connectIPPan.setOpaque(false);
		panCenter.add(idPan);
		idPan.setOpaque(false);
		panCenter.add(userIDPan);
		userIDPan.setOpaque(false);
		panCenter.add(pwPan);
		pwPan.setOpaque(false);
		panCenter.add(userPWPan);
		userPWPan.setOpaque(false);
		
		panSouth = new JPanel();
		login = new JButton("접속");		
		login.addActionListener(this);	
		membership = new JButton("회원가입");
		membership.addActionListener(this);
		cmbs.okbtn.addActionListener(this); //membership의 가입하기 버튼
		
		rightPan = new JPanel(new GridLayout(0,1,5,20));			
		
		panSouth.add(membership);
		panSouth.add(login);
		
		leftPan.add(panCenter, "Center");		
		leftPan.add(panNorth, "North");
		leftPan.add(panSouth,"South");
		
		leftPan.setOpaque(false);
		panCenter.setOpaque(false);
		panNorth.setOpaque(false);
		panSouth.setOpaque(false);
		
		paintLab.add(leftPan, "Center");
		
		mainPan.add(paintLab);
	
		
		waitingPan = new JPanel(new BorderLayout());
		JPanel makingPan = new JPanel(new FlowLayout(FlowLayout.TRAILING));
		makroom = new JButton("방만들기");
		makroom.addActionListener(this);
		makingPan.add(makroom);
		waitingPan.add(makingPan,BorderLayout.SOUTH);
		
		
		
		
		add(mainPan);
		logthread lt = new logthread();
		lt.start();
		pack();
		setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e){				
		ip = connectIP.getText();
		id = userID.getText();
		passwd = userPW.getText();
		
		
		//회원가입
				if(e.getSource()==membership) {
					cmbs.setVisible(true);
				}
				if(e.getSource()==cmbs.okbtn) {
					meminfo();
					cmbs.dispose();
					early();
					
				}
				//로그인
				if(e.getSource()==login){
					if(ip.equals("") || id.equals("")){
						JOptionPane.showMessageDialog(ChatLogin.this, "ip나 id가 입력되지 않았습니다.");
						
					} 
					else{
						loginCheck = true;
						connectIP.setText(ip);
						userID.setText(id);
						userPW.setText(passwd);

						logthread lt = new logthread();
						lt.start();
					}
				}
				
			
					
	}
	//회원가입 할 파일 만들고 파일에 아이디 비밀번호 저장
	public void meminfo() {
		String id, pass;
		id = cmbs.idtf.getText();
		pass = cmbs.pwpf.getText();
		
		try {
			Socket sock = new Socket("127.0.0.1", 10000);
			PrintWriter pwi = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));		
			String memberInfo = "회원가입 "+id+" "+pass;			
			
			pwi.println(memberInfo);
			pwi.flush();
			
			String rts = br.readLine();
			System.out.println(rts);
			if(rts.equals("already")){
				JOptionPane.showMessageDialog(ChatLogin.this,"중복된 아이디가 있습니다.");
			} else if (rts.equals("check")) {
				JOptionPane.showMessageDialog(ChatLogin.this,"가입을 환영합니다.");
				cmbs.dispose();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//회원가입창 초기화
	public void early() {
		cmbs.idtf.setText(null);
		cmbs.pwpf.setText(null);
	}
	
	public static void main(String args[]){
		new ChatLogin();
	}
	
	class logthread extends Thread{		
		public void run(){
			try {
				Socket sock = new Socket(ip,123);
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
				BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				pw.println("login//"+id+"//"+passwd);
				pw.flush();
				String ok = br.readLine();
				System.out.println(ok);

				System.out.println("dd3");
				if(ok.indexOf("loginallow")==1){
					JOptionPane.showMessageDialog(ChatLogin.this,"환영합니다");
					int croom = ok.charAt(0);
					new RoomSel(ip,id,croom);
					setVisible(false);
				}
				else{
					JOptionPane.showMessageDialog(ChatLogin.this,"로그인 실패");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	 
	@Override
	public void keyPressed(KeyEvent e) {
		ip = connectIP.getText();
		id = userID.getText();
		passwd = userPW.getText();
		
		int key=e.getKeyCode();
		if(key == KeyEvent.VK_ENTER){
			
				if(ip.equals("") || id.equals("")){
					JOptionPane.showMessageDialog(ChatLogin.this, "ip나 id가 입력되지 않았습니다.");
					
				}
	
				else{
					loginCheck = true;
					connectIP.setText(ip);
					userID.setText(id);
					userPW.setText(passwd);

					logthread lt = new logthread();
					lt.start();
				}
			
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}