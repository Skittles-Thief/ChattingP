# ChattingServerProject
TCP/IP방식을 이용한 간단한 채팅서버 프로그램입니다.

##개발환경
윈도우 10, eclipse, jdk 1.8.0

## 사용법
#### 서버창 -ChatServer파일
* 서버를 먼저 실행 시켜줍니다.
* 서버창에서는 현재 접속자 확인, 접속자의 관리, 공지사항 전달을 할 수 있습니다.
* 로그인 시도를 감지하여 메세지를 띄웁니다.
#### 로그인창 -ChatLogin파일
* 회원가입을 한 뒤 서버 컴퓨터의 아이피와 아이디, 비밀번호를 입력합니다.
* 회원가입할때 파일 내의 connectIP.setText("127.0.0.1") 부분을 서버측 아이피로 설정하셔야 합니다.
* 로그인 이후 방을 만들고, 채팅을 할 수 있습니다.
## 참고
* 이클립스에 톰캣이 설치되어있을 경우, 한글이 깨지거나 / 한글로 저장을 할 수 없는 문제가 있습니다. 
  그럴때는 https://m.blog.naver.com/PostView.nhn?blogId=eco71&logNo=20049152977&proxyReferer=https:%2F%2Fwww.google.com%2
  를 참조해주세요.

## 제작자
본 프로그램 신지엽, 박호진, 유정연, 최영진의 팀 프로젝트입니다.