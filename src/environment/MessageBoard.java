package environment;

import java.util.ArrayList;
import java.util.List;

public class MessageBoard {

	private List<Message> messageBoard;
	
	MessageBoard(){
		messageBoard = new ArrayList<Message>();
	}
	
	public void addMessage(Object sender, String text){
		Message message = new Message(sender, text);
		messageBoard.add(message);
	}
	
	public List<Message> getMessages(){
		return messageBoard;
	}
	
	public Message getLastMessage(){
		return messageBoard.get(messageBoard.size()-1);
	}
	
public class Message {
	
	protected Object sender;
	protected String message;
	protected Long time;
	
	Message(Object sender, String message){
		this.time = System.currentTimeMillis();
		this.sender = sender;
		this.message = message;
		System.out.println(sender.getClass().getName()+": "+message);
	}
}
}
