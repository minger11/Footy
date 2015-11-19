package environment;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;

/**
 * Reflexes get information from the brain for use by the model
 * reflexes are mainly concerned with brain intentions 
 * @author user
 *
 */

public class Reflexes extends PlayerInterface{
	
	Reflexes(Player player, Context context){
		super(player, context);	
	}	
	
	protected void init(){
		player.movement.setDesiredPosition(getDesiredPosition());
		player.movement.setDesiredBodyAngle(getDesiredBodyAngle());
		player.movement.setDesiredHeadAngle(getDesiredHeadAngle());
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
		return brain.getDesiredPosition();
	}
	
	protected double getDesiredBodyAngle(){
		return brain.getDesiredBodyAngle();
	}
	
	protected double getDesiredHeadAngle(){
		return brain.getDesiredHeadAngle();
	}
}
