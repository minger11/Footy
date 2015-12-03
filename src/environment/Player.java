package environment;

import brain.Brain;

/**
 * The main class for the players
 * @author user
 *
 */

public class Player extends MovingAgent {

	private Brain brain;
	private PlayerMovement movement;
	private Head head;
	private Arms arms;
	private int number;
	
	/**
	 * The constructor for the Player
	 * inits the parameters for space, grid and params
	 * @param space - the space projection
	 * @param grid - the grid projection
	 */
	Player(double x, double y, int number) {
		super(x, y);
		brain = new Brain();
		movement = new PlayerMovement(this);
		head = new Head(x, y, this);
		arms = new Arms(x, y, this);
		this.number = number;
	}
	
	Brain getBrain(){
		return brain;
	}
	
	Head getHead(){
		return head;
	}
	
	Arms getArms(){
		return arms;
	}
	
	PlayerMovement getMovement(){
		return movement;
	}
	
	int getNumber(){
		return number;
	}	
}
