package environment;

import brain.Brain;

/**
 * The main class for the players
 * @author user
 *
 */

public class Player extends MovingAgent {

	/**
	 * The player's brain
	 */
	private Brain brain;
	/**
	 * The player's movement object
	 */
	private PlayerMovement movement;
	/**
	 * The player's head
	 */
	private Head head;
	/**
	 * The player's arms
	 */
	private Arms arms;
	/**
	 * The player's number
	 */
	private int number;
	
	Player(double x, double y, int number) {
		super(x, y);
		brain = new Brain();
		movement = new PlayerMovement(this);
		head = new Head(x, y);
		arms = new Arms(x, y);
		this.number = number;
	}
	
	/**
	 * Simple getter
	 * @return the player's brain
	 */
	Brain getBrain(){
		return brain;
	}
	
	/**
	 * Simple getter
	 * @return the player's head
	 */
	Head getHead(){
		return head;
	}
	
	/**
	 * Simple getter
	 * @return the player's arms
	 */
	Arms getArms(){
		return arms;
	}
	
	/**
	 * Simple getter 
	 * @return the player's movement
	 */
	PlayerMovement getMovement(){
		return movement;
	}
	
	/**
	 * Simple getter
	 * @return the player's number
	 */
	int getNumber(){
		return number;
	}	
}
