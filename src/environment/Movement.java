package environment;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;

/**
 * Movement is concerned with the physical movement of players within the simulation
 * @author user
 *
 */

public class Movement{
	
	protected Player player;
	protected Vector3d desiredPosition; 
	protected Vector3d velocity;
	protected Vector3d currentPosition;
	protected Vector3d desiredBallPosition;
	protected double currentHeadAngle;
	protected double currentBodyAngle;
	protected double desiredHeadAngle;
	protected double desiredBodyAngle;
	
	Movement(Player player){
		this.player = player;
		velocity = new Vector3d();
		currentPosition = new Vector3d();
	}
	
	protected void init(){
		currentPosition.set(player.getPosition().getX(), player.getPosition().getY(), 0.0);	
		currentHeadAngle = desiredHeadAngle;
		currentBodyAngle = desiredBodyAngle;
	}
	
	protected void step(){
		updateMotion();
		move();
	}
	
	/**
	 * passes the current and intended motion vectors to the physics engine updates the current motion with output
	 */
	protected void updateMotion(){
		Physics physics = new Physics(player, currentPosition, desiredPosition, desiredBallPosition, velocity);
		velocity = physics.getUpdatedVelocity();
		currentPosition.add(velocity);
	}
	
	/**
	 * Physically moves the player in the grid, space and context
	 */
	protected void move(){
		//Move player
		player.space.moveTo(player, currentPosition.getX(), currentPosition.getY());
		player.grid.moveTo(player, (int)player.currentPosition.getX(), (int)player.currentPosition.getY());
		player.setPosition(player.currentPosition);
		//Move head
		player.space.moveTo(player.head, player.currentPosition.getX(), player.currentPosition.getY());
		player.grid.moveTo(player.head, (int)player.currentPosition.getX(), (int)player.currentPosition.getY());
		player.head.setPosition(player.currentPosition);
		currentHeadAngle = desiredHeadAngle;
		currentBodyAngle = desiredBodyAngle;
		player.setPosition(player.space.getLocation(player));
	}	
	
	protected void setDesiredPosition(Vector3d position){
		this.desiredPosition = position;
	}		
	
	protected void setDesiredBodyAngle(double angle){
		desiredBodyAngle = angle;
	}
	
	protected void setDesiredHeadAngle(double angle){
		desiredHeadAngle = angle;
	}
	
	protected void setDesiredBallPosition(Vector3d x){
		this.desiredBallPosition = x;
	}
}


