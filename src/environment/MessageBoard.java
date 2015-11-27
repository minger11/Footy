package environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A domain for sound and messages
 * @author user
 *
 */

public class MessageBoard {
	
	//The list of sent and pending messages
	private List<Message> messageBoard;
	private List<Message> pending;
	
	//The delay variables which determine how long a message remains pending
	private int delayPerChar = 50;
	private int fixedDelay = 100;
	
	MessageBoard(){
		messageBoard = new ArrayList<Message>();
		pending = new ArrayList<Message>();
	}
	
	void step(){
		updateMessageBoard();
	}
	
	/**
	 * Removes messages from the pending list and adds them to the messageboard list
	 */
	void updateMessageBoard(){
		
		//Iterate through the pending list
		Iterator<Message> it = pending.iterator();
		while(it.hasNext()){
				Message mess = it.next();
				
				//Set a time that the messages should be heard, based on character length and variables
				int chars = mess.getMessage().length();
				long hearable = mess.getTime()+chars*delayPerChar+fixedDelay;
				
				//If the required time has passed, remove the message from pending and add it to the messageboard
				//The message will now be available to be heard by players
				if(System.currentTimeMillis()>hearable){
					it.remove();
					messageBoard.add(mess);
				}
		}
	}
	
	/**
	 * Adds a new message to the pending list
	 * @param sender
	 * @param text
	 */
	void addMessage(Object sender, String text){
		Message message = new Message(sender, text);
		pending.add(message);
	}
	
	List<Message> getMessages(){
		return messageBoard;
	}
	
	List<Message> getPending(){
		return pending;
	}
	
	/**
	 * Returns the last message on the messageboard list
	 * @return
	 */
	Message getLastMessage(){
		if(messageBoard.size()>0){
			return messageBoard.get(messageBoard.size()-1);
		} 
		return null;
	}
}
