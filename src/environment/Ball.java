package environment;

import java.util.ArrayList;
import java.util.List;

/**
 * Controls the creation and movement of the ball
 * @author user
 *
 */

public class Ball extends MovingAgent{
	
	/**
	 * The ball's movement
	 */
	private BallMovement movement;
	/**
	 * The ball's current player
	 */
	private Player player;
	/**
	 * A list of all player who touched the ball
	 */
	private List<Player> players;
	
	Ball(double x, double y){
		super(x, y);
		movement = new BallMovement();
		movement.setTurn(0.0);
		player = null;
		players = new ArrayList<Player>();
	}
	
	/**
	 * Returns the last player who touched the ball
	 * @return last player on the player arraylist
	 */
	Player getLastPlayer(){
		int size = players.size();
		if(size>0){
			Player play = players.get(size-1);
			return play;
		} 
		return null;
	}
	
	/**
	 * Simple getter
	 * @return the ball's current player
	 */
	Player getPlayer(){
		return player;
	}
	
	/**
	 * Simple getter
	 * @return the ball's current movement
	 */
	BallMovement getMovement(){
		return movement;
	}
	
	/**
	 * Sets the player currently possessing the ball and adds to the players list
	 * @param x - the player to be set as the ball's player
	 */
	void setPlayer(Player x){
		player = x;
		if(player!=null){
			players.add(x);
		}
	}

}
