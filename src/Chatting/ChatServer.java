package Chatting;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

				
public class ChatServer extends JFrame implements ActionListener{
	String[] roomtitle = new String[8];//방목록배열
	int CurrentRoom = 0;
	String id,passwd;
	JTextArea textarea;
	JScrollPane taScroll;
	JTable table;
	DefaultTableModel model;
	JScrollPane tableScorll;
	
	JTextField inputField;
	JButton sendBtn;
	JPanel southPanel;
	
	JPopupMenu popMenu;
	JMenuItem itemSetLevel;
	JMenuItem itemBan;
	ChatServer server = this;
	HashMap<String, String> memberList = new HashMap<String, String>();
	
	String console = "", str = "";
	int roomCount1=0, roomCount2=0, roomCount3=0, roomCount4=0,roomCount5=0,roomCount6=0,roomCount7=0,roomCount8=0,waroom=0;
	
	ChatServer(){
		guiInit();
		wait.start();
		waitroom.start();
		login.start();
		rooms.start();
		room1.start();
		room2.start();
		room3.start();
		room4.start();
		room5.start();
		room6.start();
		room7.start();
		room8.start();
	}
	public void guiInit(){
		this.setSize(500,700);		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		textarea = new JTextArea(20,25);
		taScroll = new JScrollPane(textarea);
		textarea.setLineWrap(true);
		String [][] contents={};
		String [] header = {"접속자","등급"};
		model = new DefaultTableModel(contents, header);
		table = new JTable(model);
		tableScorll = new JScrollPane(table);		
		tableScorll.setPreferredSize(new Dimension(200, 700));
		
		popMenu = new JPopupMenu();
		itemSetLevel = new JMenuItem("등급조정");
		itemSetLevel.addActionListener(this);
		itemBan = new JMenuItem("추방하기");
		itemBan.addActionListener(this);
		
		popMenu.add(itemSetLevel);
		popMenu.add(itemBan);
		
		// 우클릭시 팝업 띄움. 등급조정은 아직 미구현...
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					int row = table.getSelectedRow();
					popMenu.show(table, e.getX(), e.getY());
					if(row != -1){
						str = (String)table.getValueAt(row, 0);				
					}
				}
			}
		});		
		
		southPanel  = new JPanel();
		inputField = new JTextField(30);
		sendBtn = new JButton("공지 전송");
		southPanel.add(inputField);
		southPanel.add(sendBtn);				
		
		add(southPanel,"South");		
		this.add(tableScorll,"East");
		this.add(taScroll,"Center");
		this.setVisible(true);
	}			
	
	/*
	 * 방 4개 임시로 설정해둠
	 * 각각 포트번호는 12345, 12346, 12347, 12348
	 * 4개의 쓰레드 각각 accept로 연결 대기중
	 */	
	Thread login = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket lserver = new ServerSocket(123);
				
				while(true){	
					Socket sock = lserver.accept();
					
					LoginThread lt = new LoginThread(sock);
					lt.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	Thread wait = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket server0 = new ServerSocket(10000);
				textarea.append("접속을 기다립니다.\n");
				while(true){	
					Socket sock = server0.accept();
					TradeMemberInfo tmi = new TradeMemberInfo(sock);
					tmi.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});	
	Thread waitroom = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket waitserver = new ServerSocket(12222);
				HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
				while(true){	
					Socket sock = waitserver.accept();
					GUIChatThread2 chatthread = new GUIChatThread2(sock, hm, textarea, model, server, waroom);
					chatthread.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	
	Thread rooms = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket rserver = new ServerSocket(1234);
				
				while(true){	
					Socket sock = rserver.accept();
					
					roomsThread rt = new roomsThread(sock);
					rt.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	
	Thread room1 = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket server1 = new ServerSocket(10);
				HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
				while(true){	
					Socket sock = server1.accept();
					roomCount1++;
					GUIChatThread chatthread = new GUIChatThread(sock, hm, textarea, model, server, roomCount1);
					chatthread.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	
	Thread room2 = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket server2 = new ServerSocket(11);
				HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
				while(true){	
					Socket sock = server2.accept();
					roomCount2++;
					GUIChatThread chatthread = new GUIChatThread(sock, hm, textarea, model, server, roomCount2);
					chatthread.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	
	Thread room3 = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket server3 = new ServerSocket(12);
				HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
				while(true){	
					Socket sock = server3.accept();
					roomCount3++;
					GUIChatThread chatthread = new GUIChatThread(sock, hm, textarea, model, server, roomCount3);
					chatthread.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	
	Thread room4 = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket server4 = new ServerSocket(13);
				HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
				while(true){	
					Socket sock = server4.accept();
					roomCount4++;
					GUIChatThread chatthread = new GUIChatThread(sock, hm, textarea, model, server, roomCount4);
					chatthread.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	Thread room5 = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket server5 = new ServerSocket(14);
				HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
				while(true){	
					Socket sock = server5.accept();
					roomCount5++;
					GUIChatThread chatthread = new GUIChatThread(sock, hm, textarea, model, server, roomCount5);
					chatthread.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	Thread room6 = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket server6 = new ServerSocket(15);
				HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
				while(true){	
					Socket sock = server6.accept();
					roomCount6++;
					GUIChatThread chatthread = new GUIChatThread(sock, hm, textarea, model, server, roomCount6);
					chatthread.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	Thread room7 = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket server7 = new ServerSocket(16);
				HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
				while(true){	
					Socket sock = server7.accept();
					roomCount7++;
					GUIChatThread chatthread = new GUIChatThread(sock, hm, textarea, model, server, roomCount7);
					chatthread.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	Thread room8 = new Thread(new Runnable(){
		@Override
		public void run(){
			try{		
				ServerSocket server8 = new ServerSocket(17);
				HashMap<String, PrintWriter> hm = new HashMap<String, PrintWriter>();	
				while(true){	
					Socket sock = server8.accept();
					roomCount8++;
					GUIChatThread chatthread = new GUIChatThread(sock, hm, textarea, model, server, roomCount8);
					chatthread.start();
				} // while			
			}catch(Exception e){	
				System.out.println("server main : " + e);
			}	
		}
	});
	@Override
	public void actionPerformed(ActionEvent e) {}
	
	class LoginThread extends Thread{
		String idu,pdu;
		String id,passwd;
		Socket sock;
		public LoginThread(Socket sock){
			this.sock=sock;

		}
		public void run(){
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				String bb = br.readLine();
				String[] bs = bb.split("//");
				id=bs[1];
				passwd=bs[2];
				textarea.append("로그인 시도를 감지\n");
				textarea.append("id:"+id+"|pw:"+passwd+"|sock:"+sock+"\n");
				BufferedReader bfr = new BufferedReader(new FileReader("C:/Users/user/Desktop/userdata/member.txt"));
				String member = null;
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
				while((member=bfr.readLine())!=null) {
				String[] info = member.split("/");//파일 읽어와서 "/"기준으로 잘라서 배열에 담음
					idu = info[0]; //info배열에 0번쨰에 있는 아이디정보를 idu에담음
					pdu = info[1]; //info배열에 1번쨰에 있는 비밀번호정보를 pdu에담음
					if(id.equals(idu) && passwd.equals(pdu)) {

						textarea.append("로그인 허용"+id+"\n");
						pw.print(CurrentRoom+"loginallow");
						pw.flush();
						pw.close();
						break;
					}
					if(id.equals(idu)&&!passwd.equals(pdu)){
						JOptionPane.showMessageDialog(ChatServer.this, "정보를 확인해주세요");
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
	class TradeMemberInfo extends Thread {
		private Socket sock;		
		private PrintWriter pwi;
		private BufferedReader br;
		FileWriter fw2;
		BufferedWriter bw2;
		public TradeMemberInfo(Socket sock){
			this.sock = sock;
			
			try {
				File Folder = new File("C:/Users/user/Desktop/userdata");//유저 데이터가 저장될 위치 지정
				
				if(!Folder.exists()) {//해당 위치에 폴더가 있는지 확인
					try {
					Folder.mkdir();//폴더 생성
					System.out.println("폴더가 생성되었습니다.");
					}catch(Exception e) {
						
						e.getStackTrace();
					}
				}else {
						System.out.println("이미 유저 데이터 폴더가 생성되어 있습니다");
					}	
				pwi = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
				br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				
				String memberInfo = br.readLine();
				
				StringTokenizer st = new StringTokenizer(memberInfo);
				st.nextToken();
				String id, pass;
				id = st.nextToken(); 
				pass = st.nextToken();
					
				//id가 해쉬맵에 있는 키값이면 already를 클라이언트로 반환
				//없다면 memberList해쉬맵에 저장하고 파일에도 저장함
				if(memberList.containsKey(id)){
					pwi.println("already");
					pwi.flush();
				} else {
					memberList.put(id, pass);
					
					fw2 = new FileWriter("C:/Users/user/Desktop/userdata/member.txt", true);
					bw2 = new BufferedWriter(fw2);
						
					String putMem = id+"/"+pass+"\n";
					bw2.write(putMem);
					bw2.flush();			
					
					pwi.println("check");
					pwi.flush();									
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(bw2 != null){
						bw2.close();
					}
					if(fw2 != null){
						fw2.close();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}		
	}
	class roomsThread extends Thread{
		Socket sock;
		
		public roomsThread(Socket sock){
			this.sock = sock;
		}
		public void run(){
			try{
			BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			String rc = br.readLine();
			if(rc.indexOf("addroom")==0){
				roomtitle[CurrentRoom] = rc.substring(7);//방목록의 배열을 저장
				textarea.append(roomtitle[CurrentRoom]);
				CurrentRoom++;
				textarea.append("채팅방 생성"+CurrentRoom);
				pw.println(CurrentRoom+roomtitle[CurrentRoom-1]);
				pw.flush();
				pw.close();
			}
			else if (rc.indexOf("callroomlist")==0){//방목록을 호출받으면
				textarea.append(rc);
				String cr = (CurrentRoom)+"//";
				for(int i=0;i<CurrentRoom;i++){
					cr = cr+(roomtitle[i]+"//");
				}
				textarea.append(cr);
				pw.println(cr);
				pw.flush();
				pw.close();
			}
			
			
			}
			catch(Exception e){
				
			}
		}
	}
	class GUIChatThread extends Thread implements ActionListener,KeyListener{			
		private Socket sock;		
		private ChatServer server;
		private String id;		
		private JTextArea ta;
		private DefaultTableModel model;
		private BufferedReader br;		
		private ObjectOutputStream oos;
		private HashMap<String, PrintWriter> hm;	
		private ArrayList<String> idList;		
		private ArrayList<ImageIcon> img;
		private boolean initFlag = false;
		public GUIChatThread(Socket sock, HashMap<String, PrintWriter> hm, JTextArea ta, DefaultTableModel model, ChatServer server, int n){		
			this.sock = sock;	
			this.hm = hm;
			this.ta = ta;
			this.model = model;
			this.server = server;
			server.itemBan.addActionListener(this);			
		
			/*
			 * GUIChatData에 넘길 ImageIcon type의 ArrayList
			 * 다른컴퓨터에서 돌릴때는 파일과 경로를 수정 해줘야함.
			 */			
			img = new ArrayList<ImageIcon>();

			img.add(new ImageIcon("imgs/1.png"));
			img.add(new ImageIcon("imgs/2.png"));
			img.add(new ImageIcon("imgs/3.png"));
			img.add(new ImageIcon("imgs/4.png"));
			img.add(new ImageIcon("imgs/5.png"));
			img.add(new ImageIcon("imgs/6.png"));
			img.add(new ImageIcon("imgs/7.png"));
			img.add(new ImageIcon("imgs/8.png"));
			img.add(new ImageIcon("imgs/9.png"));
			if(n<=1){
				server.sendBtn.addActionListener(this);
				
			}			
			server.inputField.addKeyListener(this);
			try{	
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));	
				oos = new ObjectOutputStream(sock.getOutputStream());
				br = new BufferedReader(new InputStreamReader(sock.getInputStream()));		
				GUIChatData data = new GUIChatData();
				data.setImg(img);
				oos.writeObject(data);	
				oos.flush();
				
				id = br.readLine();	
				broadcast(id + " 님이 접속하였습니다."); // 서버에서 클라이언트에게 알리는 메소드 broadcast (ta에만?)
				
				File Folder = new File("C:/Users/user/Desktop/userdata");//유저 데이터가 저장될 위치 지정
				
				if(!Folder.exists()) {//해당 위치에 폴더가 있는지 확인
					try {
					Folder.mkdir();//폴더 생성
					System.out.println("폴더가 생성되었습니다.");
					}catch(Exception e) {
						
						e.getStackTrace();
					}
				}else {
						System.out.println("이미 유저 데이터 폴더가 생성되어 있습니다");
					}
				 File file = new File("C:/Users/user/Desktop/userdata/"+id+".txt");//지정한 위치에 있는 id.txt지정
				 if(!file.exists()) {//id 파일없으면 만듬
					 try {
						 System.out.println("파일 없음");
						 FileWriter fw = new FileWriter(file);
						 fw.write("id:"+id+"/");  // 친구삭제관련
						 fw.close();
					 }catch(Exception e) {
						 
					 }
				 }
				 if(file.exists()) {//아이디 파일이 있을 경우 데이터를 읽어 넘겨쥼
					 System.out.println("저장된 아이디 파일이 있습니다");
					 
					 BufferedReader fr = new BufferedReader(new FileReader(file));
					 String userdata = fr.readLine();
					 fr.close();
					 System.out.println(userdata);
					 if(userdata.indexOf("//")!=-1) {
						 pw.println(userdata);
						 pw.flush();
					 }else {
						 System.out.println("친구가 없음");
						 pw.println(userdata);
						 pw.flush();
					 }
				 }
				
				ta.append("접속한 사용자의 아이디는 " + id + "입니다.\n");
				model.addRow(new String[]{id,"guest"});
				ta.setCaretPosition(ta.getDocument().getLength());
				synchronized(hm){	
					hm.put(id, pw); // 해쉬맵에 사용자가 지정한 ID와 printWriter를 저장함
				}				
				pw.println(hm);
				pw.flush();
			}catch(Exception ex){		
				System.out.println("server thread constructor: " + ex);	
			}		
		} // 생성자		
				
		public void run(){					
			try{		
				String line = null;		
				while((line = br.readLine()) != null){		// br.readLine 은 클라이언트에서 사용자가 쓴 글을 읽음			
					if(line.equals("/quit")){
						break;					
					}
					else if(line.indexOf("/kick ") == 0){
						kickUser(line);
					}
					else if(line.indexOf("/to ") == 0){					
						sendmsg(line);
					}
					else if(line.contains("Moti")){
						broadcast(id+" "+line);
					} 
					else {
						broadcast(id + " : " + line);
					}
				}
			}catch(Exception ex){			
				System.out.println("server thread run : " + ex);		
			}finally{			
				synchronized(hm){
					PrintWriter pw = hm.remove(id);
					pw.println("/quit");	
					pw.flush();
				}
				
				String info = id + " 님이 접속 종료하였습니다.";
				
				int i = 0;				
				while(i<=model.getRowCount()-1){
					if((String)model.getValueAt(i, 0) == id){
						model.removeRow(i);
					} else {
						i++;					
					}				
				}
				
				broadcast(info);
				ta.append(info + "\n");
				ta.setCaretPosition(ta.getDocument().getLength());
				
				try{		
					if(sock != null) {	
						sock.close();
					}
				}catch(Exception ex){}		
			}			
		} // run			
		
				 
		//강퇴		
		public void kickUser(String str){
			String id = str;
			PrintWriter pw = hm.get(id);
			if(pw != null){
				pw.println("/quit");
				pw.flush();
			}			
		}
		
		// 귓말
		public void sendmsg(String msg){			
			int start = msg.indexOf(" ") +1;			
			int end = msg.indexOf(" ", start);			
			if(end != -1){			
				String to = msg.substring(start, end);		
				String msg2 = msg.substring(end+1);		
				PrintWriter pw = hm.get(to);	// pw 역할 중요					
				if(pw != null){						
					pw.println(id + " 님이 다음의 귓속말을 보내셨습니다. :" + msg2);	
					pw.flush();	
				} // if	
				pw = hm.get(id);
				pw.println(id + " 님께 다음의 귓속말을 보냈습니다. :" + msg2);
				pw.flush();
			}		
		} // sendmsg	
		
		/*	
		 * 인자가 없는 broadcast를 따로 만듬
		 * 해쉬맵을 넘겨서 현재 접속한 사람의 ID를 클라이언트에 표시하기 위해 만들어둠
		 */
		public void broadcast(String msg){	
			if(msg.indexOf("addfriend")!=-1) {//친구 추가 명령을 수신받아 해당 유저의 파일에 정보기록
				String[] ei =msg.split(" : ");
				ei[1] = ei[1].substring(9);
				try {
					BufferedReader fr = new BufferedReader(new FileReader(new File("C:/Users/user/Desktop/userdata/"+ei[0]+".txt")));//이미친구있음
					String rs = fr.readLine();
					fr.close();
					if(rs.indexOf(ei[1]) != -1){
						System.out.println("이미 친구가 추가되어 있습니다.");
						PrintWriter pww = hm.get(ei[0]);
						pww.println("이미 친구가 추가되어 있습니다.");					
						pww.flush();	
					}
					else{
					FileWriter fw = new FileWriter(new File
							("C:/Users/user/Desktop/userdata/"+ei[0]+".txt"),true);
					fw.write("/"+ei[1]+"/");//친구삭제관련
					fw.close();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else if(msg.indexOf("removefriend")!=-1) {//친구삭제관련
				String[] ei=msg.split(" : ");
				System.out.println(ei[1]);
				ei[1] = ei[1].substring(12);
				try {
					BufferedReader fr = new BufferedReader(new FileReader(new File("C:/Users/user/Desktop/userdata/"+ei[0]+".txt")));
					String rs = fr.readLine();
					fr.close();
					rs = rs.replace("/"+ei[1]+"/","");
					FileWriter fw = new FileWriter(new File
							("C:/Users/user/Desktop/userdata/"+ei[0]+".txt"),false);
					fw.write(rs);
					fw.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			else {
			synchronized(hm){		
				Collection<PrintWriter> collection = hm.values();	
				Iterator<PrintWriter> iter = collection.iterator();	
				while(iter.hasNext()){				
					PrintWriter pw = iter.next();
					pw.println(msg);					
					pw.flush();				
					}	
				}
			}
		} // broadcast		

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==itemBan){
				kickUser(str);
			}
			else if(e.getSource()==sendBtn){
				broadcast("<공지사항> "+inputField.getText());
			
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key=e.getKeyCode();
			if(key == KeyEvent.VK_ENTER ){
				broadcast("<공지사항> "+inputField.getText());
				inputField.setText("");
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
		//대기실 채팅 스레드
	}class GUIChatThread2 extends Thread implements ActionListener,KeyListener{			
		private Socket sock;		
		private ChatServer server;
		private String id;		
		private JTextArea ta;
		private DefaultTableModel model;
		private BufferedReader br;		
		private ObjectOutputStream oos;
		private HashMap<String, PrintWriter> hm;	
		private ArrayList<String> idList;		
		private ArrayList<ImageIcon> img;
		private boolean initFlag = false;
		public GUIChatThread2(Socket sock, HashMap<String, PrintWriter> hm, JTextArea ta, DefaultTableModel model, ChatServer server, int n){		
			this.sock = sock;	
			this.hm = hm;
			this.ta = ta;
			this.model = model;
			this.server = server;
			server.itemBan.addActionListener(this);			
		
			/*
			 * GUIChatData에 넘길 ImageIcon type의 ArrayList
			 * 다른컴퓨터에서 돌릴때는 파일과 경로를 수정 해줘야함.
			 */			

			if(n<=1){
				server.sendBtn.addActionListener(this);
				
			}			
			server.inputField.addKeyListener(this);
			try{	
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));	
				oos = new ObjectOutputStream(sock.getOutputStream());
				br = new BufferedReader(new InputStreamReader(sock.getInputStream()));		
				GUIChatData data = new GUIChatData();
				
				
				id = br.readLine();	
				broadcast(id + " 님이 접속하였습니다."); // 서버에서 클라이언트에게 알리는 메소드 broadcast (ta에만?)
				
				File Folder = new File("C:/Users/user/Desktop/userdata");//유저 데이터가 저장될 위치 지정
				
				if(!Folder.exists()) {//해당 위치에 폴더가 있는지 확인
					try {
					Folder.mkdir();//폴더 생성
					System.out.println("폴더가 생성되었습니다.");
					}catch(Exception e) {
						
						e.getStackTrace();
					}
				}else {
						System.out.println("이미 유저 데이터 폴더가 생성되어 있습니다");
					}
				 File file = new File("C:/Users/user/Desktop/userdata/"+id+".txt");//지정한 위치에 있는 id.txt지정
				 if(!file.exists()) {//id 파일없으면 만듬
					 try {
						 System.out.println("파일 없음");
						 FileWriter fw = new FileWriter(file);
						 fw.write("id:"+id+"/");  // 친구삭제관련
						 fw.close();
					 }catch(Exception e) {
						 
					 }
				 }
				 if(file.exists()) {//아이디 파일이 있을 경우 데이터를 읽어 넘겨쥼
					 System.out.println("저장된 아이디 파일이 있습니다");
					 
					 BufferedReader fr = new BufferedReader(new FileReader(file));
					 String userdata = fr.readLine();
					 fr.close();
					 System.out.println(userdata);
					 if(userdata.indexOf("//")!=-1) {
						 pw.println(userdata);
						 pw.flush();
					 }else {
						 System.out.println("친구가 없음");
						 pw.println(userdata);
						 pw.flush();
					 }
				 }
				
				ta.append("접속한 사용자의 아이디는 " + id + "입니다.\n");
				model.addRow(new String[]{id,"guest"});
				ta.setCaretPosition(ta.getDocument().getLength());
				synchronized(hm){	
					hm.put(id, pw); // 해쉬맵에 사용자가 지정한 ID와 printWriter를 저장함
				}				
				pw.println(hm);
				pw.flush();
			}catch(Exception ex){		
				System.out.println("server thread constructor: " + ex);	
			}		
		} // 생성자		
				
		public void run(){					
			try{		
				String line = null;		
				while((line = br.readLine()) != null){		// br.readLine 은 클라이언트에서 사용자가 쓴 글을 읽음			
					if(line.equals("/quit")){
						break;					
					}
					else if(line.indexOf("/kick ") == 0){
						kickUser(line);
					}
					else if(line.indexOf("/to ") == 0){					
						sendmsg(line);
					}
					else if(line.contains("Moti")){
						broadcast(id+" "+line);
					} 
					else {
						broadcast(id + " : " + line);
					}
				}
			}catch(Exception ex){			
				System.out.println("server thread run : " + ex);		
			}finally{			
				synchronized(hm){
					PrintWriter pw = hm.remove(id);
					pw.println("/quit");	
					pw.flush();
				}
				
				String info = id + " 님이 접속 종료하였습니다.";
				
				int i = 0;				
				while(i<=model.getRowCount()-1){
					if((String)model.getValueAt(i, 0) == id){
						model.removeRow(i);
					} else {
						i++;					
					}				
				}
				
				broadcast(info);
				ta.append(info + "\n");
				ta.setCaretPosition(ta.getDocument().getLength());
				
				try{		
					if(sock != null) {	
						sock.close();
					}
				}catch(Exception ex){}		
			}			
		} // run			
		
				 
		//강퇴		
		public void kickUser(String str){
			String id = str;
			PrintWriter pw = hm.get(id);
			if(pw != null){
				pw.println("/quit");
				pw.flush();
			}			
		}
		
		// 귓말
		public void sendmsg(String msg){			
			int start = msg.indexOf(" ") +1;			
			int end = msg.indexOf(" ", start);			
			if(end != -1){			
				String to = msg.substring(start, end);		
				String msg2 = msg.substring(end+1);		
				PrintWriter pw = hm.get(to);	// pw 역할 중요					
				if(pw != null){						
					pw.println(id + " 님이 다음의 귓속말을 보내셨습니다. :" + msg2);	
					pw.flush();	
				} // if	
				pw = hm.get(id);
				pw.println(id + " 님께 다음의 귓속말을 보냈습니다. :" + msg2);
				pw.flush();
			}		
		} // sendmsg	
		
		/*	
		 * 인자가 없는 broadcast를 따로 만듬
		 * 해쉬맵을 넘겨서 현재 접속한 사람의 ID를 클라이언트에 표시하기 위해 만들어둠
		 */
		public void broadcast(String msg){	
			if(msg.indexOf("addfriend")!=-1) {//친구 추가 명령을 수신받아 해당 유저의 파일에 정보기록
				String[] ei =msg.split(" : ");
				ei[1] = ei[1].substring(9);
				try {
					BufferedReader fr = new BufferedReader(new FileReader(new File("C:/Users/user/Desktop/userdata/"+ei[0]+".txt")));//이미친구있음
					String rs = fr.readLine();
					fr.close();
					if(rs.indexOf(ei[1]) != -1){
						System.out.println("이미 친구가 추가되어 있습니다.");
						PrintWriter pww = hm.get(ei[0]);
						pww.println("이미 친구가 추가되어 있습니다.");					
						pww.flush();	
					}
					else{
					FileWriter fw = new FileWriter(new File
							("C:/Users/user/Desktop/userdata/"+ei[0]+".txt"),true);
					fw.write("/"+ei[1]+"/");//친구삭제관련
					fw.close();
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else if(msg.indexOf("removefriend")!=-1) {//친구삭제관련
				String[] ei=msg.split(" : ");
				System.out.println(ei[1]);
				ei[1] = ei[1].substring(12);
				try {
					BufferedReader fr = new BufferedReader(new FileReader(new File("C:/Users/user/Desktop/userdata/"+ei[0]+".txt")));
					String rs = fr.readLine();
					fr.close();
					rs = rs.replace("/"+ei[1]+"/","");
					FileWriter fw = new FileWriter(new File
							("C:/Users/user/Desktop/userdata/"+ei[0]+".txt"),false);
					fw.write(rs);
					fw.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
			else {
			synchronized(hm){		
				Collection<PrintWriter> collection = hm.values();	
				Iterator<PrintWriter> iter = collection.iterator();	
				while(iter.hasNext()){				
					PrintWriter pw = iter.next();
					pw.println(msg);					
					pw.flush();				
					}	
				}
			}
		} // broadcast		

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==itemBan){
				kickUser(str);
			}
			else if(e.getSource()==sendBtn){
				broadcast("<공지사항> "+inputField.getText());
			
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key=e.getKeyCode();
			if(key == KeyEvent.VK_ENTER ){
				broadcast("<공지사항> "+inputField.getText());
				inputField.setText("");
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
	
	
	public static void main(String[] args) {			
		new ChatServer();
	} // main	
}			

