package environment;

import repast.simphony.context.Context;

/**
 * Reflexes get information from the brain for use by the model
 * reflexes are mainly concerned with brain intentions 
 * @author user
 *
 */

public class Reflexes extends PlayerInterface{
	
	Physics physics;
	
	Reflexes(Player player, Context context){
		super(player, context);	
	}	
	
	protected void init(){
		player.movement.setDesiredMotion(getDesiredMotion());
		player.movement.init();
	}
	
	protected void step(){
		player.movement.setDesiredMotion(getDesiredMotion());
		player.movement.step();
	}
	
	/**
	 * Gets the intended movement angle and speed from the brain
	 * @return
	 */
	protected MotionVector getDesiredMotion(){
		MotionVector motion = new MotionVector();
		motion.setVelocity(brain.getSpeed());
		motion.setAngle(brain.getAngle());
		return motion;
	}
}
