package environment;

import javax.vecmath.Vector3d;

public class BallMovement{
	
	//Positional and velocity vectors
	private Vector3d effort; 
	
	//Rotational angles (in radians)
	private double turn;
	
	/**
	 * Creates the balls movement
	 * @param ball
	 */
	BallMovement(){
	}
	
	//------SETTERS AND GETTERS -------------------
	
	void setTurn(double angle){
		this.turn = angle;
	}
	
	void setEffort(Vector3d position){
		this.effort = position;
	}	
	
	double getTurn(){
		return turn;
	}
	
	Vector3d getEffort(){
		return effort;
	}	
}
