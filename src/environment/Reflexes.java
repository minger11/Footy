package environment;

import java.util.Iterator;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;

/**
 * Reflexes get information from the brain for use by the model
 * reflexes are mainly concerned with brain intentions 
 * @author user
 *
 */

public class Reflexes {
	
	Player player;
	
	Reflexes(Player player){
		this.player = player;
	}	
	
	protected void init(){
		mouth();
		head();
		body();
		arms();
		legs();
		player.movement.init();
	}
	
	protected void step(){
		mouth();
		head();
		body();
		arms();
		legs();
		player.movement.step();
	}
	
	public void head(){
		player.movement.setDesiredHeadAngle(getDesiredHeadAngle());
	}
	
	public void mouth(){
		sendMessage(getMessage());
	}
	
	public void body(){
		player.movement.setDesiredBodyAngle(getDesiredBodyAngle());
	}
	
	public void legs(){
		player.movement.setDesiredPosition(getDesiredPosition());
	}
	
	public void arms(){
		player.movement.setDesiredBallPosition(getDesiredBallPosition());
	}
	
	/**
	 * Adds a string message to the messageBoard only if the current player has no pending messages
	 * @param message - the string to be posted
	 */
	public void sendMessage(String message){
		//If the current message is not null
		if(message!=null){
			
			//Initialize a bool to indicate whether the player has no pending messages on the messageboard
			boolean noPendingMessage = true;
			
			//Iterate through the pending messages on the messageboard
			Iterator<Message> it = player.messageBoard.getPending().iterator();
			while(it.hasNext()){
				
				//If the player has a pending message set the bool to false
				if(it.next().getSender().equals(player)){
					noPendingMessage = false;
				}
			}
			
			//Add the message to the message board only if the player has no pending messages
			if(noPendingMessage)player.messageBoard.addMessage(player, message);
		}
	}
	
	public String getMessage(){
		return player.brain.getNewMessage();
	}
	/**
	 * Gets the intended movement angle and speed from the brain
	 * @return
	 */
	protected Vector3d getDesiredPosition(){
		return player.brain.getDesiredPosition();
	}
	
	protected double getDesiredBodyAngle(){
		return player.brain.getDesiredBodyAngle();
	}
	
	protected double getDesiredHeadAngle(){
		return player.brain.getDesiredHeadAngle();
	}
	
	protected Vector3d getDesiredBallPosition(){
		return player.brain.getDesiredBallPosition();
	}
}
