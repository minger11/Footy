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
	private BallMovement movement;
	
	//Balls current handler
	private Player player;
	
	//A list of all ball handlers who touch the ball
	private List<Player> players;
	
	/**
	 * Creates the ball
	 * @param context
	 * @param x
	 * @param y
	 */
	Ball(Context context, int x, int y){
		super(context, x, y);
		movement = new BallMovement();
		movement.setTurn(0.0);
		player = null;
		players = new ArrayList<Player>();
	}
	
	/**
	 * Returns the last player who touched the ball
	 * @return
	 */
	Player getLastPlayer(){
		int size = players.size();
		if(size>0){
			Player play = players.get(size-1);
			return play;
		} 
		return null;
	}
	
	Player getPlayer(){
		return player;
	}
	
	BallMovement getMovement(){
		return movement;
	}
	
	/**
	 * Sets the player currently possessing the ball and adds to the players list
	 * @param x
	 */
	void setPlayer(Player x){
		player = x;
		if(player!=null){
			players.add(x);
		}
	}

}
