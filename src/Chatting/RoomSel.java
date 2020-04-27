package Chatting;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

class RoomSel extends JFrame implements ActionListener {
	
	Socket sock;//소켓
	String ip, id;
	JPanel pnl;
	JButton nr,getout;
	JButton[] rooms = new JButton[8];
	ImageIcon[] imc = {new ImageIcon("imgs/1.png"),new ImageIcon("imgs/2.png"),
			new ImageIcon("imgs/3.png"),new ImageIcon("imgs/4.png"),
			new ImageIcon("imgs/5.png"),new ImageIcon("imgs/6.png"),
			new ImageIcon("imgs/7.png"),new ImageIcon("imgs/8.png")};
	JLabel[] lbl = new JLabel[8];
	String[] abc = {"강아지","식물","고양이","조류","돈","asdf","gsda","sadg"};
	JLabel[] albl = new JLabel[8];
	JTextArea waitdisplay;
	JTextField waitInput;
	List waitUser,waitFriend;
	int rn = 0, dr = 0, count = 0;
	String fri,end;
	RoomMaking rmk = new RoomMaking(this,"방 만들기");
	PrintWriter pw;
	BufferedReader br;
	ArrayList<String> userID = new ArrayList<String>();
	WinInputThread1 with1 = new WinInputThread1();
	private JPopupMenu pm;
	private JMenuItem pm_1,pm_2,pm_3;

	RoomSel(String ip, String id) { //로비에서 다시 돌아올때는 croom을 넘겨받지않음
		
		this.ip = ip;
		this.id = id;
		try {
			sock = new Socket(ip,1234);

			roomtitleThread rtt = new roomtitleThread(sock);
			rtt.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("방갯"+rn);
		
		GUIRoomSel(ip,id);
		waitchat(ip,id);
		rmk.makokbtn.addActionListener(this);
		rmk.maknobtn.addActionListener(this);
		pm = new JPopupMenu();
		pm_1 = new JMenuItem("친구추가");
		pm_2 = new JMenuItem("친구삭제");
		pm_3 = new JMenuItem("귓속말");
		pm_1.addActionListener(this);
		pm_2.addActionListener(this);
		pm_3.addActionListener(this);
		pm.add(pm_1);
		pm.add(pm_2);
		pm.add(pm_3);
		with1.start();
	}
	
	RoomSel(String ip, String id, int croom) {
		
		this.ip = ip;
		this.id = id;
		this.rn = croom-48;
		System.out.println("초기값"+rn);
		try {
			sock = new Socket(ip,1234);

			roomtitleThread rtt = new roomtitleThread(sock);
			rtt.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		GUIRoomSel(ip,id);
		waitchat(ip,id);
		rmk.makokbtn.addActionListener(this);
		rmk.maknobtn.addActionListener(this);
		pm = new JPopupMenu();
		pm_1 = new JMenuItem("친구추가");
		pm_2 = new JMenuItem("친구삭제");
		pm_3 = new JMenuItem("귓속말");
		pm_1.addActionListener(this);
		pm_2.addActionListener(this);
		pm_3.addActionListener(this);
		pm.add(pm_1);
		pm.add(pm_2);
		pm.add(pm_3);
		with1.start();
	}

	public void GUIRoomSel(String ip,String id) {
		this.setTitle("대기실");
		this.setSize(600, 400);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pnl = new JPanel();
		pnl.setLayout(null);
		nr = new JButton("방 만들기");
		getout = new JButton("종료");
		
		//리스트
		waitUser = new List(10);
		waitFriend = new List(10);
		waitUser.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON3)
					pm.show(waitUser,e.getX(),e.getY());
					pm.revalidate();
					pm.repaint();
			}
		});
		waitFriend.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON3)
					pm.show(waitFriend,e.getX(),e.getY());
					pm.revalidate();
					pm.repaint();
			}
		});
		waitdisplay = new JTextArea(20,80);
		waitdisplay.setEditable(false);
		waitInput = new JTextField(80);
		waitInput.addActionListener(this);
		waitInput.setBorder(new TitledBorder("Input"));
		
		JPanel back = new JPanel(new BorderLayout());
		JPanel totalpnl = new JPanel(new BorderLayout());
		JPanel displaypnl = new JPanel();
		JPanel nrpnl = new JPanel();
		JPanel Listpnl = new JPanel(new GridLayout(2,1));
		JPanel gridpnl = new JPanel(new GridLayout(0,2));
		JPanel Userpnl = new JPanel();
		JPanel Friendpnl = new JPanel();
		JPanel inputpnl = new JPanel(new FlowLayout(FlowLayout.LEADING));
		
		
		nrpnl.add(nr);
		nrpnl.add(getout);;
		inputpnl.add(waitInput);
		Userpnl.add(waitUser);
		Friendpnl.add(waitFriend);
		
		gridpnl.add(Userpnl);
		gridpnl.add(Friendpnl);
		Listpnl.add(gridpnl);
		Listpnl.add(nrpnl);
	

		displaypnl.add(new JScrollPane(waitdisplay));
		displaypnl.setBorder(new TitledBorder("대기실 챗"));
		Userpnl.setBorder(new TitledBorder("접속자"));
		Friendpnl.setBorder(new TitledBorder("친구 목록"));
		pnl.setBorder(new TitledBorder("대기방"));
		totalpnl.add(displaypnl,BorderLayout.CENTER);
		totalpnl.add(inputpnl,BorderLayout.SOUTH);
	
		
		back.add(pnl,BorderLayout.CENTER);
		back.add(Listpnl,BorderLayout.EAST);
		back.add(totalpnl,BorderLayout.SOUTH);
		
		
		
		this.add(back);
		pnl.setPreferredSize(new Dimension(400, 400));
		nr.setPreferredSize(new Dimension(100, 40));
		getout.setPreferredSize(new Dimension(100,40));
		nr.addActionListener(this);
		getout.addActionListener(this);
		
		if(rn!=0)
		{
		for (int i = dr; i < rn; i++) {
			albl[i] = new JLabel(abc[i]);
			albl[i].setFont(new Font("한컴 솔잎B",Font.BOLD,12));
			lbl[i] = new JLabel(imc[i]);
			rooms[i] = new JButton();
			rooms[i].setLayout(new BorderLayout());
			rooms[i].setBackground(Color.white);
			rooms[i].add(lbl[i],BorderLayout.WEST);
			rooms[i].add(albl[i]);
			pnl.add(rooms[i]);
			int rnx = 10, rny = 0;
			if (i < 4) {
				rny = i * 75+50;
			} else if (i >= 4) {
				rnx = 200;
				rny = ((i - 4) * 75)+50;

			}
			rooms[i].setBounds(rnx, rny, 175, 70);
			rooms[i].addActionListener(this);
			rooms[i].revalidate();
			rooms[i].repaint();
			;
			dr++;
			}
		}
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == waitInput){
			String msg1 = waitInput.getText();
			pw.println(msg1);
			pw.flush();
			waitInput.setText("");
			waitInput.requestFocus();
			System.out.println(waitdisplay.getCaretPosition());
		}
		if (e.getSource() == nr) {
			rmk.setVisible(true);
			if(rn<8){
			newroomThread nrt = new newroomThread(sock);
			nrt.start();
			}
			else{
				JOptionPane.showMessageDialog(RoomSel.this,"더 이상 만들 수 없습니다");
			}
		}
		//종료
		if(e.getSource() == getout){
			System.exit(-1);
		}
		if(e.getSource() == pm_1){
			fri = waitUser.getSelectedItem();
			waitFriend.add(fri);
			friend();
		}
		if(e.getSource() == pm_2){
			end = waitFriend.getSelectedItem();
			waitFriend.remove(end);
			removefriend();
		}
		if(e.getSource() == pm_3){
			waitInput.setText("/to "+waitUser.getSelectedItem()+" ");
			waitInput.requestFocus();
		}

		if (e.getSource() == rooms[0]) {
			try {
				new GUIChatClient(ip, id, 10);
				
				setVisible(false);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == rooms[1]) {
			try {
				new GUIChatClient(ip, id, 11);
				setVisible(false);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == rooms[2]) {
			try {
				new GUIChatClient(ip,id, 12);
				setVisible(false);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == rooms[3]) {
			try {
				new GUIChatClient(ip, id, 13);
				setVisible(false);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == rooms[4]) {
			try {
				new GUIChatClient(ip, id, 14);
				setVisible(false);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == rooms[5]) {
			try {
				new GUIChatClient(ip, id,15);
				setVisible(false);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == rooms[6]) {
			try {
				new GUIChatClient(ip, id, 16);
				setVisible(false);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == rooms[7]) {
			try {
				new GUIChatClient(ip,id, 17);
				setVisible(false);

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	public void friend(){
		try {
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			pw.println("addfriend"+fri);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void removefriend(){
		try {
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			pw.println("removefriend"+end);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void waitchat(String ip, String id){
		try{
			Socket sock = new Socket(ip,12222);
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			pw.println(id.trim());
			pw.flush();
			
			String myfriends[] =null;
			String idstr=br.readLine();
			String str = br.readLine();
			
			if(idstr.indexOf("id")==0) {
				myfriends = idstr.split("//");
				for(int c= 1; c<myfriends.length; c++) {
					if(c==myfriends.length-1)
					{
						waitFriend.add(myfriends[c].replace("/"," "));//친구삭제관련
						break;
					}
					waitFriend.add(myfriends[c]);
					
				}
				myfriends[0]=myfriends[0].substring(3);
			}
		
			str = str.substring(1);
			StringTokenizer st = new StringTokenizer(str, "=");
			String test = "";
			
			while(st.hasMoreTokens()){
				test = test + st.nextToken() + "\n";
			}
			StringTokenizer st2 = new StringTokenizer(test);
			String test2 = "";
			
			while(st2.hasMoreTokens()){
				test2 = st2.nextToken();
				if(count%2==0){
					userID.add(test2);					
				}				
				count++;
			}	
			for(int i=0 ; i<userID.size(); i++){
				waitUser.add(userID.get(i));
			}
			
		}catch(Exception e){
		
		}
	}
	class newroomThread extends Thread {
		Socket sock;
		public newroomThread(Socket sock){
			this.sock = sock;
		}
		
		public void run() {
			try {
				Socket sock = new Socket(ip, 1234);
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(
						sock.getOutputStream()));
				BufferedReader br = new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
				pw.println("addroom"+RoomMaking.ss);
				pw.flush();
				rn = br.read()-48;
				abc[rn-1]= br.readLine();
				System.out.println(rn);
				roomThread rt = new roomThread();
				rt.start(); 
			} catch (Exception e) {
			}
		}

	}

	class roomThread extends Thread implements ActionListener {

		public void run() {
			if(rn!=0)
			{
				
			for (int i = dr; i < rn; i++) {
				
				albl[i] = new JLabel(abc[i]);
				albl[i].setFont(new Font("한컴 솔잎B",Font.BOLD,12));
				lbl[i] = new JLabel(imc[i]);
				rooms[i] = new JButton();
				rooms[i].setLayout(new BorderLayout());
				rooms[i].add(lbl[i],BorderLayout.WEST);
				rooms[i].add(albl[i]);
				pnl.add(rooms[i]);
				int rnx = 10, rny = 0;
				if (i < 4) {
					rny = i * 75+50;
				} else if (i >= 4) {
					rnx = 200;
					rny = ((i - 4) * 75)+50;

				}
				rooms[i].setBounds(rnx, rny, 175,70);
				rooms[i].setBackground(Color.white);
				rooms[i].addActionListener(this);
				rooms[i].revalidate();
				rooms[i].repaint();
				;
				dr++;
			}
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == waitInput){
				String msg1 = waitInput.getText();
				pw.println(msg1);
				pw.flush();
				waitInput.setText("");
				waitInput.requestFocus();
				System.out.println(waitdisplay.getCaretPosition());
			}
			//종료
			if(e.getSource() == getout){
				System.exit(-1);
			}
			if(e.getSource() == pm_1){
				fri = waitUser.getSelectedItem();
				waitFriend.add(fri);
				friend();
			}
			if(e.getSource() == pm_2){
				end = waitFriend.getSelectedItem();
				waitFriend.remove(end);
				removefriend();
			}
			if(e.getSource() == pm_3){
				waitInput.setText("/to "+waitUser.getSelectedItem()+" ");
				waitInput.requestFocus();
			}
			if (e.getSource() == rooms[0]) {
				try {
					new GUIChatClient(ip, id, 10);
					setVisible(false);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (e.getSource() == rooms[1]) {
				try {
					new GUIChatClient(ip, id, 11);
					setVisible(false);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (e.getSource() == rooms[2]) {
				try {
					new GUIChatClient(ip,id, 12);
					setVisible(false);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (e.getSource() == rooms[3]) {
				try {
					new GUIChatClient(ip, id, 13);
					setVisible(false);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (e.getSource() == rooms[4]) {
				try {
					new GUIChatClient(ip, id, 14);
					setVisible(false);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (e.getSource() == rooms[5]) {
				try {
					new GUIChatClient(ip, id,15);
					setVisible(false);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (e.getSource() == rooms[6]) {
				try {
					new GUIChatClient(ip, id, 16);
					setVisible(false);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			} else if (e.getSource() == rooms[7]) {
				try {
					new GUIChatClient(ip,id, 17);
					setVisible(false);

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
		public void friend(){
			try {
				pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
				pw.println("addfriend"+fri);
				pw.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void removefriend(){
			try {
				pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
				pw.println("removefriend"+end);
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
class roomtitleThread extends Thread{// 룸 이름 관련 쓰레드
		Socket sock;
		public roomtitleThread(Socket sock){
			this.sock = sock;
		}
		public void run(){
			try {
				PrintWriter crl = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
				crl.println("callroomlist");
				crl.flush();
				BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				String[] cc= br.readLine().split("//");

				rn = Integer.parseInt(cc[0]);
				for(int i=0;i< rn ;i++){
					abc[i] = cc[i+1];
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
class WinInputThread1 extends Thread{
	public void run(){
		try{
			String line = null;
	
			while((line = br.readLine()) != null){		
				
				if(line.equals("이미 친구가 추가되어 있습니다.")){
					waitFriend.remove(waitFriend.getItemCount()-1);
				}
				// 접속하면 문자열에서 id만 잘라서 리스트에 추가함
				else if(line.contains("접속하였")){
					StringTokenizer st = new StringTokenizer(line);
					String str = st.nextToken();
					waitUser.add(str);
				}
				// 퇴장하면 문자열에서 id만 잘라서 리스트에서 제거함
				else if(line.contains("종료하였")){
					StringTokenizer st = new StringTokenizer(line);
					String str = st.nextToken();
					waitUser.remove(str);
				}
				else if(line.equals("/quit")) {
					throw new Exception();
				}
				else {
					waitdisplay.append(line+"\n");
					waitdisplay.setCaretPosition(waitdisplay.getDocument().getLength());
				}								
			}						
		} catch (Exception e){
			System.out.println("client thread : " + e);
			JOptionPane.showMessageDialog(RoomSel.this, "프로그램을 종료합니다.");
		} finally {
			try{
				if(br != null) {
					br.close();
				} 
			} catch (Exception e){}
			try{
				if(pw != null) {
					pw.close();
				}
			} catch (Exception e){}
			try{
				if(sock != null){
					sock.close();
				}
			} catch(Exception e){}
			System.exit(NORMAL);
		}
	}
}
}