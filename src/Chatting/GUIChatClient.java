package Chatting;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;


public class GUIChatClient extends JFrame implements ActionListener{
	String ip,id;//id와 ip 저장될곳
	public static final int NORMAL = 0;
	public static final int EXCEPTIONAL = -1;	
	private JTextField input;
	private JTextPane display;
	private BufferedReader br;
	private PrintWriter pw;
	private Socket sock;
	public List user,friend;
	private JPopupMenu pm;
	private JMenuItem pm_1,pm_2,pm_3;	
	private ObjectInputStream ois;
	int count = 0;
	int yLocate = 0;
	String str="";
	String fi,fo;
	ArrayList<String> userID = new ArrayList<String>();
	ArrayList<ImageIcon> img = new ArrayList<ImageIcon>();
	Color myColor = new Color(255,255,122);
	Emoticon emt = new Emoticon(this,"이모티콘");
	JButton emotibtn,lobi;
	
	public GUIChatClient(String ip, String id, int port){
		super("채팅 클라이언트");
		init();
		setDisplay();
		
		this.ip=ip;//id와 ip저장
		this.id=id;
		
		connect(ip, id, port);		
		addListeners();
		showFrame();
	}
	
	private void init() {
		input = new JTextField();
		input.setBorder(new TitledBorder("Input"));
		display = new JTextPane();
		display.setEditable(false);
		display.setLayout(null);
	}
	
	private void setDisplay(){
		JPanel pnlCenter = new JPanel(new BorderLayout());
		pnlCenter.add(new JScrollPane(display));
		pnlCenter.setBorder(new TitledBorder("Chat"));
		JPanel pnlCenter_sub = new JPanel(new BorderLayout());
		pnlCenter_sub.add(pnlCenter);
		user = new List(10);
		user.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON3)
					pm.show(user,e.getX(),e.getY());
					pm.revalidate();
					pm.repaint();
			}
		});
		
		friend = new List(10);
		friend.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON3)
					pm.show(friend,e.getX(),e.getY());
				pm.revalidate();
				pm.repaint();
			}
		});
		
		JPanel pnlEast = new JPanel(new GridLayout(0,2));//접속자,친구목록담음
		JPanel pnluser = new JPanel();
		JPanel pnlfriend = new JPanel();
		pnluser.add(user);
		pnlfriend.add(friend);
		pnlEast.add(pnluser);
		pnlEast.add(pnlfriend);
		pnluser.setBorder(new TitledBorder("User"));
		pnlfriend.setBorder(new TitledBorder("Friend"));
		
		pnlCenter_sub.add(pnlEast, "East");
		add(pnlCenter_sub, BorderLayout.CENTER);
		JPanel pnlSouth = new JPanel(new BorderLayout());
		pnlSouth.add(input);
		
		emotibtn = new JButton("이모티콘");
		emotibtn.addActionListener(this);
		JPanel exitRoom_sub = new JPanel();
		JButton exitRoom = new JButton("나가기");
		lobi = new JButton("로비로");//로비로 가는 버튼 추가
		lobi.addActionListener(this);
		
		exitRoom.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(-1);
			}			
		});
		exitRoom_sub.add(emotibtn);
		exitRoom_sub.add(lobi);//로비로 가는 버튼 추가
		exitRoom_sub.add(exitRoom);
		pnlSouth.add(exitRoom_sub, "East");
		add(pnlSouth, BorderLayout.SOUTH);		
		
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
		
		for(int i =0; i<9; i++) {//이모티콘버튼에 액션
			emt.emotibtn[i].addActionListener(this);
			
		}
	} // setDisplay()
	
	private void addListeners(){
		input.addActionListener(this);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				pw.println("/quit");
				pw.flush();
			}
		});
	}
	
	private void showFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setSize(800, 500);
		setResizable(false);
		setVisible(true);
		input.requestFocus();
	} 
	
	private void connect(String ip, String id, int port) {		
		try{
			sock = new Socket(ip, port);
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));			
			ois = new ObjectInputStream(sock.getInputStream());			
			GUIChatData data =(GUIChatData) ois.readObject(); 
			this.img = data.getImg();						
			pw.println(id.trim());
			pw.flush();
			
			//친구저장
			String myfriends[] =null;
			String idstr = br.readLine();//GUIChatThread에서 가장 먼저 넘어오는 정보인 아이디,친구목록을 받아옴
			String str = br.readLine();
			if(idstr.indexOf("id")==0) {
				myfriends = idstr.split("//");
				for(int c= 1; c<myfriends.length; c++) {
					if(c==myfriends.length-1)
					{
						friend.add(myfriends[c].replace("/"," "));//친구삭제관련
						break;
					}
					friend.add(myfriends[c]);
					
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
				user.add(userID.get(i));
			}
			
			WinInputThread wit = new WinInputThread();
			wit.start();
		} catch (Exception e){
			System.out.println("서버와 접속시 오류가 발생하였습니다.");
			System.out.println(e);
			System.exit(EXCEPTIONAL);
		}
	} // connect()
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//채팅
		if(e.getSource()==input){
			String msg = input.getText();
			pw.println(msg);
			pw.flush();
			input.setText("");
			input.requestFocus();
			System.out.println(display.getCaretPosition());
		}
		// 이모티콘 다욜로그 화면
		if(e.getSource()==emotibtn){
			emt.setVisible(true);			
		}
		if(e.getSource()==emt.emotibtn[0]){
			pw.println("Moti 1");
			pw.flush();
		}else if(e.getSource()==emt.emotibtn[1]){
			pw.println("Moti 2");
			pw.flush();
		}else if(e.getSource()==emt.emotibtn[2]){
			pw.println("Moti 3");
			pw.flush();
		}else if(e.getSource()==emt.emotibtn[3]){
			pw.println("Moti 4");
			pw.flush();
		}else if(e.getSource()==emt.emotibtn[4]){
			pw.println("Moti 5");
			pw.flush();
		}else if(e.getSource()==emt.emotibtn[5]){
			pw.println("Moti 6");
			pw.flush();
		}else if(e.getSource()==emt.emotibtn[6]){
			pw.println("Moti 7");
			pw.flush();
		}else if(e.getSource()==emt.emotibtn[7]){
			pw.println("Moti 8");
			pw.flush();
		}else if(e.getSource()==emt.emotibtn[8]){
			pw.println("Moti 9");
			pw.flush();
		}
	
		//친구추가
		if(e.getSource()==pm_1) {
			fi=user.getSelectedItem();
			friend.add(fi);
			friend();
		}
		//친구삭제
		if(e.getSource()==pm_2) {
			fo=friend.getSelectedItem();
			friend.remove(fo);
			removefriend();
		}
		if(e.getSource()==pm_3){
			input.setText("/to "+user.getSelectedItem()+" ");
			input.requestFocus();
		}
		
		if(e.getSource()==lobi){//로비로 나가기

			try {
				new RoomSel(ip, id);
				this.setVisible(false);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}
	
	}
	public void friend(){
		try {
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			pw.println("addfriend"+fi);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void removefriend(){
		try {
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			pw.println("removefriend"+fo);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	// JTextPane에 append 메소드가 없어서 만듬
	public void append(String str){
		try{
			Document doc = display.getDocument();
			doc.insertString(doc.getLength(), str, null);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}
	
	class WinInputThread extends Thread{
		public void run(){
			try{
				String line = null;
				while((line = br.readLine()) != null){		
					if(line.equals("이미 친구가 추가되어 있습니다.")){
						friend.remove(friend.getItemCount()-1);
					}
					if(line.equals("/quit")) {
						throw new Exception();
					}
					// 접속하면 문자열에서 id만 잘라서 리스트에 추가함
					if(line.contains("접속하였")){
						StringTokenizer st = new StringTokenizer(line);
						String str = st.nextToken();
						user.add(str);
					}
					// 퇴장하면 문자열에서 id만 잘라서 리스트에서 제거함
					if(line.contains("종료하였")){
						StringTokenizer st = new StringTokenizer(line);
						String str = st.nextToken();
						user.remove(str);
					}
					// 이모티콘 출력용. 완전하지 않음
					if(line.contains("Moti")){
						StringTokenizer st = new StringTokenizer(line);
						String id = st.nextToken();
						st.nextToken();
						int n = Integer.parseInt(st.nextToken());
						
						display.insertIcon(img.get(n-1));
						append(System.getProperty("line.separator")+id+" : "+input.getText()+"\n");	
						display.setCaretPosition(display.getDocument().getLength());
						input.setText("");
					} else {
						append(line+"\n");
						display.setCaretPosition(display.getDocument().getLength());
					}								
				}						
			} catch (Exception e){
				System.out.println("client thread : " + e);
				JOptionPane.showMessageDialog(GUIChatClient.this, "프로그램을 종료합니다.");
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
