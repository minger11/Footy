package environment;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;

/**
 * Movement is concerned with taking the reflex desires, 
 * running them through the appropriate physics methods 
 * and updating the true movements in the model
 * @author user
 *
 */

public class PlayerMovement{
	
	//Current Player
	protected Player player;
	
	//Relevant positional and velocity vectors
	protected Vector3d desiredPosition; 
	
	//Desired ball position
	protected Vector3d desiredBallPosition;
	
	//Rotational angles (radians not degrees)
	protected double desiredHeadAngle;
	protected double desiredBodyAngle;	
	
	protected Physics physics;
	
	PlayerMovement(Player player){
		this.player = player;
		desiredPosition = new Vector3d();
	}
	
	protected void init(){
		player.head.rotation = desiredHeadAngle;
		player.rotation = desiredBodyAngle;
	}
	
	protected void step(){
		getPhysics();
		updateVelocity();
		updateRotation();
		updatePositionVector();
	}
	
	protected void getPhysics(){
		physics = new Physics(player, player.positionVector, desiredPosition, desiredBallPosition, player.velocity);
	}
	
	/**
	 * passes the current and intended motion vectors to the physics engine updates the current motion with output
	 */
	protected void updateVelocity(){
		player.velocity = physics.getUpdatedVelocity();
		player.head.velocity = player.velocity;
	}
	
	protected void updateRotation(){
		player.head.rotation = desiredHeadAngle;
		player.rotation = desiredBodyAngle;
	}
	
	protected void updatePositionVector(){
		player.positionVector.add(player.velocity);
		player.head.positionVector = player.positionVector;
	}
	
	//------------SETTERS AND GETTERS---------------//
	
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


