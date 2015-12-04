package environment;

/**
 * Used for adding messages to the messageboard and sending messages to the brain
 * @author user
 *
 */

public class Message {
	
	/**
	 * The sender of the message - messages within the model only (not sent to brain)
	 */
	private Object sender;
	/**
	 * The text string of the message - in all messages
	 */
	private String message;
	/**
	 * The time in milliseconds that message was created - messages within the model only (not sent to brain)
	 */
	private Long time;
	/**
	 * A boolean indicating whether the message is official - in all messages
	 */
	private boolean official;
	/**
	 * A boolean indicating whether the game is on - official messages only
	 */
	private Boolean gameOn;
	
	/**
	 * Used by the messageBoard to add messages
	 * Distinct from the other constructors as it includes a time (for the messageboard to check when to move from pending to the messageboard)
	 * and a sender(for the senses to construct an angle to the message to be sent to the brain)
	 * Needs to be reconstructed for use within the brain
	 * @param sender - the sender of the message
	 * @param message - the text message string
	 */
	Message(Object sender, String message){
		this.time = System.currentTimeMillis();
		this.sender = sender;
		this.message = message;
		this.official = false;
	}
	
	/**
	 * The message constructor used when adding messages to the messageboard
	 * Distinct from the other constructors as this message is immediately posted on the messageboard and can be sent to the brain as is
	 * @param message - text message string 
	 * @param gameOn - boolean indicating whether the current game is on
	 */
	Message(String message, boolean gameOn){
		this.message = message;
		this.gameOn = gameOn;
		this.official = true;
	}
	
	/**
	 * The constructor used when an unofficial message is being heard and sent to the brain
	 * Distinct from the other constructors in that it is only used by the senses when sending information to the brain
	 * @param message - the text message string
	 */
	Message(String message){
		this.message = message;
		this.official = false;
	}
	
	/**
	 * Simple getter
	 * @return the sender of the message
	 */
	public Object getSender(){
		return sender;
	}
	
	/**
	 * Simple getter 
	 * @return the boolean indicating whether the message came from an official source
	 */
	public boolean getOfficial(){
		return official;
	}
	
	/**
	 * Simple getter
	 * @return the long that represents the time the message was created
	 */
	public Long getTime(){
		return time;
	}
	
	/**
	 * Simple getter
	 * @return the string that was sent as the actual message
	 */
	public String getMessage(){
		return message;
	}
	
	/**
	 * Simple getter
	 * @return the boolean that indicates whether the game is still on - official messages only
	 */
	public Boolean getGameOn(){
		return gameOn;
	}
}
