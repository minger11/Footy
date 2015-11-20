package environment;

import javax.vecmath.Vector3d;

import repast.simphony.context.Context;

/**
 * Controls the creation and movement of the ball
 * @author user
 *
 */
public class Ball extends SimpleAgent{
	
	protected BallMovement movement;
	protected Player player;
	
	/**
	 * Creates the ball
	 * @param context
	 * @param x
	 * @param y
	 */
	Ball(Context context, int x, int y){
		super(context, x, y);
		movement = new BallMovement(this);
	}
	
	/**
	 * Calls init on the balls movement
	 */
	public void init(){
		movement.init();
	}
	
	/**
	 * Calls step on the balls movement
	 */
	public void step(){
		movement.step();
	}
	
	/**
	 * Sets the player currently possessing the ball
	 * @param x
	 */
	public void setPlayer(Player x){
		player = x;
	}
	
public class BallMovement{
	
	protected Vector3d desiredPosition; 
	protected Vector3d velocity;
	protected Vector3d currentPosition;
	protected double currentAngle;
	protected double desiredAngle;
	
	protected Ball ball;
	
	/**
	 * Creates the balls movement
	 * @param ball
	 */
	BallMovement(Ball ball){
		this.ball = ball;
		velocity = new Vector3d();
		currentPosition = new Vector3d();
		currentAngle = 0.0;
	}
	
	/**
	 * Initializes the currentPosition vector to be equal to the current position
	 */
	protected void init(){
		currentPosition.set(ball.getPosition().getX(), ball.getPosition().getY(), 0.0);	
	}
	
	/**
	 * Updates the current Position vector and physically moves the ball
	 */
	protected void step(){
		updateMotion();
		move();
	}
	
	public void updateMotion(){
		Physics physics = new Physics(ball, currentPosition, desiredPosition, velocity);
		velocity = physics.getUpdatedVelocity();
		currentPosition.add(velocity);
		physics.ballAngularManipulation();
	}
	
	/**
	 * Moves the ball to the currentPosition
	 */
	protected void move(){
		ball.space.moveTo(ball, currentPosition.getX(), currentPosition.getY());
		ball.grid.moveTo(ball, (int)ball.currentPosition.getX(), (int)ball.currentPosition.getY());
		ball.setPosition(ball.currentPosition);
		setPosition(space.getLocation(ball));
	}
	
	//------SETTERS AND GETTERS -------------------
	
	protected void setDesiredAngle(double angle){
		this.desiredAngle = angle;
	}
	
	protected void setDesiredPosition(Vector3d position){
		this.desiredPosition = position;
	}		
}
}
