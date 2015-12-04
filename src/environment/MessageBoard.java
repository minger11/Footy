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
	
	/**
	 * The actual messageBoard arraylist - each time step, the newest message on this list should be heard by players
	 */
	private static List<Message> messageBoard = new ArrayList<Message>();
	/**
	 * The pending arraylist - when new unofficial messages are sent they come here first
	 */
	private static List<Message> pending = new ArrayList<Message>();
	/**
	 * Indicates whether there is a new message on the messageBoard
	 */
	private static boolean newMessage = false;
	
	private MessageBoard(){
	}
	
	/**
	 * Removes messages from the pending list and adds them to the messageboard list
	 */
	public static void update(){
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
					hearable = mess.getTime()+chars*Params.delayPerChar+Params.fixedDelay;
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
	 * Adds a new message directly to the messageboard, bypassing the pending queue - for official messages only
	 * @param text - the text of the message
	 * @param gameOn - boolean signifying whether the game is still on
	 */
	public static void addMessage(String text, boolean gameOn){
		Message message;
		message = new Message(text, gameOn);
		pending.add(message);
	}
	
	/**
	 * Simple getter
	 * @return the messageBoard arraylist
	 */
	public static List<Message> getMessages(){
		return messageBoard;
	}
	
	/**
	 * Simple getter
	 * @return the pending arraylist
	 */
	public static List<Message> getPending(){
		return pending;
	}
	
	/**
	 * Simple getter
	 * @return the newMessage boolean indicating if a new message is on the messageBoard
	 */
	public static boolean getNewMessage(){
		return newMessage;
	}
	
	/**
	 * Returns the last message on the messageboard list
	 * @return the last message on the messageBoard
	 */
	public static Message getLastMessage(){
		if(messageBoard.size()>0){
			return messageBoard.get(messageBoard.size()-1);
		} 
		return null;
	}
}
