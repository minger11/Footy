package environment;

import java.util.ArrayList;
import java.util.List;

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
	
	//A list of all ball handlers who touch the ball
	protected List<Player> players;
	
	/**
	 * Creates the ball
	 * @param context
	 * @param x
	 * @param y
	 */
	Ball(Context context, int x, int y){
		super(context, x, y);
		movement = new BallMovement(this);
		player = null;
		players = new ArrayList<Player>();
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
	 * Returns the last player who touched the ball
	 * @return
	 */
	public Player getLastPlayer(){
		int size = players.size();
		if(size>0){
			Player player = players.get(size-1);
			return player;
		} 
		return null;
	}
	
	/**
	 * Sets the player currently possessing the ball
	 * @param x
	 */
	public void setPlayer(Player x){
		player = x;
	}

}
