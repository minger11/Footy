package environment;

/**
 * Used for adding messages to the messageboard and sending messages to the brain
 * @author user
 *
 */

public class Message {
	
	private Object sender;
	private String message;
	private Long time;
	private boolean official;
	private Double angle;
	
	/**
	 * The message constructor used when adding messages to the messageboard
	 * @param sender
	 * @param message
	 */
	Message(Object sender, String message){
		this.time = System.currentTimeMillis();
		this.sender = sender;
		this.message = message;
	}
	
	/**
	 * The message constructor used when adding messages to the messageboard
	 * @param sender
	 * @param message
	 */
	Message(String message){
		this.message = message;
		this.official = true;
	}
	
	/**
	 * The constructor used when an official message (from the referee) is being heard and sent to the brain
	 * @param official
	 * @param message
	 */
	Message(boolean official, String message){
		this.message = message;
		this.official = official;
	}
	
	/**
	 * The constructor used when an unofficial message (from a player) is being heard and sent to the brain
	 * @param official
	 * @param message
	 * @param angle
	 */
	Message(boolean official, String message, Double angle){
		this.message = message;
		this.angle = angle;
		this.official = official;
	}
	
	Object getSender(){
		return sender;
	}
	
	Double getAngle(){
		return angle;
	}
	
	Boolean getOfficial(){
		return official;
	}
	
	Long getTime(){
		return time;
	}
	
	String getMessage(){
		return message;
	}
}
