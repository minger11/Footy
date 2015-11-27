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
	
	Utils utils = new Utils();
	
	Player player;
	
	Reflexes(Player player){
		this.player = player;
	}	
	
	protected void init(){
		mouth();
		neck();
		body();
		arms();
		legs();
		player.movement.init();
	}
	
	protected void step(){
		mouth();
		neck();
		body();
		arms();
		legs();
		player.movement.step();
	}
	
	public void neck(){
		player.movement.setDesiredHeadAngle(getTurnHead());
	}
	
	public void mouth(){
		sendMessage(getMessage());
	}
	
	public void body(){
		player.movement.setDesiredBodyAngle(getTurnBody());
	}
	
	public void legs(){
		//player.movement.setDesiredPosition(getDesiredPosition());
		player.movement.setDesiredBodyVelocity(getBodyVelocity());
	}
	
	public void arms(){
		//player.movement.setDesiredBallPosition(getDesiredBallPosition());
		player.movement.setDesiredBallVelocity(getBallVelocity());
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
	

	//-------------REAL GETTERS-------------------------//
	
	protected String getMessage(){
		return player.brain.getNewMessage();
	}
	
	protected Vector3d getBallVelocity(){
		return player.brain.getBallVelocity();
	}
	
	protected Vector3d getBodyVelocity(){
		return player.brain.getBodyVelocity();
	}
	
	protected double getTurnBody(){
		return utils.RelativeToAbsolute(player.brain.getDesiredBodyAngle(), player.head.rotation);
	}
	
	protected double getTurnHead(){
		return utils.RelativeToAbsolute(player.brain.getDesiredHeadAngle(), player.head.rotation);
	}
	
	protected double getTurnArm(){
		double d=0;
		return d;
	}
}
