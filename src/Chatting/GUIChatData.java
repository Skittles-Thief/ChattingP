package Chatting;

import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class GUIChatData implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String msg;
	private ArrayList<ImageIcon> img;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}	
	
	public ArrayList<ImageIcon> getImg(){
		return img;
	}
	
	public void setImg(ArrayList<ImageIcon> img) {
		this.img = img;
	}
}