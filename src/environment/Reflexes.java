package environment;

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
		player.movement.setDesiredPosition(getDesiredPosition());
		player.movement.setDesiredBodyAngle(getDesiredBodyAngle());
		player.movement.setDesiredHeadAngle(getDesiredHeadAngle());
		player.movement.setDesiredBallPosition(getDesiredBallPosition());
		player.movement.init();
	}
	
	protected void step(){
		player.movement.setDesiredPosition(getDesiredPosition());
		player.movement.setDesiredBodyAngle(getDesiredBodyAngle());
		player.movement.setDesiredHeadAngle(getDesiredHeadAngle());
		player.movement.step();
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
