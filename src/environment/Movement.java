package environment;

import repast.simphony.context.Context;

/**
 * Movement is concerned with the physical movement of players within the simulation
 * @author user
 *
 */

public class Movement extends PlayerInterface{
	
	MotionVector desiredMotion;
	
	Movement(Player player, Context<Object> context){
		super(player, context);
	}
	
	protected void init(){
		updateMotion();
	}
	
	protected void step(){
		updateMotion();
		move();
	}
	
	/**
	 * passes the current and intended motion vectors to the physics engine updates the current motion with output
	 */
	protected void updateMotion(){
		Physics physics = new Physics(player.motion, desiredMotion);
		player.motion = physics.updateMotion();
	}
	
	/**
	 * Physically moves the player in the grid, space and context
	 */
	protected void move(){
		space.moveByVector(player, player.motion.getVelocity(), player.motion.getAngle(), 0);
		grid.moveTo(player, (int)player.currentPosition.getX(), (int)player.currentPosition.getY());
		player.setPosition(player.currentPosition);
	}	
	
	protected void setDesiredMotion(MotionVector motion){
		desiredMotion = motion;
	}
}
	
