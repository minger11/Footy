package environment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A domain for sound and messages
 * @author user
 *
 */

public final class MessageBoard {
	
	//The list of sent and pending messages
	private static List<Message> messageBoard;
	private static List<Message> pending;
	private static boolean newMessage;
	
	//The delay variables which determine how long a message remains pending
	private static int delayPerChar = 50;
	private static int fixedDelay = 100;
	
	private MessageBoard(){
	}
	
	public static void init(){
		messageBoard = new ArrayList<Message>();
		pending = new ArrayList<Message>();
		newMessage = false;
	}
	
	public static void step(){
		updateMessageBoard();
	}
	
	/**
	 * Removes messages from the pending list and adds them to the messageboard list
	 */
	public static void updateMessageBoard(){
		newMessage = false;
		//Iterate through the pending list
		Iterator<Message> it = pending.iterator();
		while(it.hasNext()){
				Message mess = it.next();
				
				//Set a time that the messages should be heard, based on character length and variables
				int chars = mess.getMessage().length();
				long hearable;
				if(mess.getOfficial()){
					hearable = System.currentTimeMillis();
				} else {
					hearable = mess.getTime()+chars*delayPerChar+fixedDelay;
				}
				
				//If the required time has passed, remove the message from pending and add it to the messageboard
				//The message will now be available to be heard by players
				if(System.currentTimeMillis()>=hearable){
					it.remove();
					messageBoard.add(mess);
					newMessage = true;
				} 
		}
	}
	
	/**
	 * Adds a new message to the pending list
	 * @param sender
	 * @param text
	 */
	public static void addMessage(Object sender, String text){
		Message message = new Message(sender, text);
		pending.add(message);
	}
	
	/**
	 * For official messages only
	 * @param sender
	 * @param text
	 */
	public static void addMessage(String text, boolean gameOn){
		Message message;
		message = new Message(text, gameOn);
		pending.add(message);
	}
	
	public static List<Message> getMessages(){
		return messageBoard;
	}
	
	public static List<Message> getPending(){
		return pending;
	}
	public static boolean getNewMessage(){
		return newMessage;
	}
	
	/**
	 * Returns the last message on the messageboard list
	 * @return
	 */
	public static Message getLastMessage(){
		if(messageBoard.size()>0){
			return messageBoard.get(messageBoard.size()-1);
		} 
		return null;
	}
}
