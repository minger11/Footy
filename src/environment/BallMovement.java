package environment;

import javax.vecmath.Vector3d;

public class BallMovement{
	
	/**
	 * The effort currently being applied to the ball
	 */
	private Vector3d effort; 
	/**
	 * A boolean to indicate whether the ball has recently been involved in a collision. Used in physics to switch on decay.
	 */
	private boolean collided = false;
	/**
	 * The rotation of the ball (in radians)
	 */
	private double turn;
	
	/**
	 * Creates the balls movement
	 * @param ball
	 */
	BallMovement(){
	}
	
	/**
	 * Simple setter
	 * @param angle - the new turn angle to be applied to the ball
	 */
	void setTurn(double angle){
		this.turn = angle;
	}
	
	/**
	 * Simple setter
	 * @param effort - the new effort vector to be applied to the ball
	 */
	void setEffort(Vector3d effort){
		this.effort = effort;
	}	
	
	/**
	 * Simple setter
	 * @param collided - the boolean indicating whether the ball has recently been involved in a collision
	 */
	void setCollided(boolean collided){
		this.collided = collided;
	}
	
	/**
	 * Simple getter
	 * @return the angle of turn currently being applied to the ball
	 */
	double getTurn(){
		return turn;
	}
	
	/**
	 * Simple getter
	 * @return a boolean to indicate whether the ball has been involved in a collision recently or not
	 */
	boolean getCollided(){
		return collided;
	}
	
	/**
	 * Simple getter
	 * @return the effort vector currently being applied to the ball
	 */
	Vector3d getEffort(){
		return effort;
	}	
}
