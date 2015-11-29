package environment;

import java.util.Iterator;
import brain.Brain;
import repast.simphony.context.Context;

/**
 * The main class for the players
 * @author user
 *
 */

public class Player extends MovingAgent {

	private Brain brain;
	private PlayerMovement movement;
	private Head head;
	private int number;
	
	/**
	 * The constructor for the Player
	 * inits the parameters for space, grid and params
	 * @param space - the space projection
	 * @param grid - the grid projection
	 */
	Player(Context context, int x, int y, int number) {
		super(context, x, y);
		brain = new Brain();
		movement = new PlayerMovement(this);
		head = new Head(context, x, y, this);
		this.number = number;
	}
	
	void init(){	
		Senses.init(this);
		brain.init();
		Reflexes.init(this);
	}
	
	void step() {
		Senses.step(this);
		brain.step();
		Reflexes.step(this);
	}
	
	Brain getBrain(){
		return brain;
	}
	
	Head getHead(){
		return head;
	}
	
	PlayerMovement getMovement(){
		return movement;
	}
	
	int getNumber(){
		return number;
	}	
}
