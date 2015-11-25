package environment;

import repast.simphony.context.Context;

/**
 * Controls the creation and movement of the ball
 * @author user
 *
 */

public class Ball extends MovingAgent{
	
	//Ball movement
	protected BallMovement movement;
	
	//Balls current handler
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

}
