package environment;

import javax.vecmath.Vector3d;

public class BallMovement{
	
	//Positional and velocity vectors
	protected Vector3d desiredPosition; 
	
	//Rotational angles (in radians)
	protected double desiredAngle;
	
	//Current ball
	protected Ball ball;
	
	protected Physics physics;
	
	/**
	 * Creates the balls movement
	 * @param ball
	 */
	BallMovement(Ball ball){
		this.ball = ball;
		desiredAngle = 0.0;
	}
	
	/**
	 * Initializes the currentPosition vector to be equal to the current position
	 */
	protected void init(){
		desiredPosition = ball.positionVector;
	}
	
	/**
	 * Updates the current Position vector and physically moves the ball
	 */
	protected void step(){
		getPhysics();
		updateVelocity();
		updateRotation();
		updatePositionVector();
	}
	
	public void getPhysics(){
		//Creates a new physics
		physics = new Physics(ball, ball.positionVector, desiredPosition, ball.velocity);
	}
	
	/**
	 * Handles all changes to the ball velocity and positional vectors as well as angles
	 */
	public void updateVelocity(){
		//Sets the current velocity to the return from physics
		ball.velocity = physics.getUpdatedVelocity();
	}
	
	public void updateRotation(){
		//Calls the angular manipulation method in physics
		physics.ballAngularManipulation();
	}
	
	public void updatePositionVector(){
		//Adds the current velocity to the current position
		ball.positionVector.add(ball.velocity);	
	}
	
	//------SETTERS AND GETTERS -------------------
	
	protected void setDesiredAngle(double angle){
		this.desiredAngle = angle;
	}
	
	protected void setDesiredPosition(Vector3d position){
		this.desiredPosition = position;
	}		
}
