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

	protected Brain brain;
	protected Senses senses;
	protected Reflexes reflexes;
	protected PlayerMovement movement;
	protected Head head;
	protected int number;
	protected MessageBoard messageBoard;
	
	/**
	 * The constructor for the Player
	 * inits the parameters for space, grid and params
	 * @param space - the space projection
	 * @param grid - the grid projection
	 */
	Player(Context context, int x, int y, int number) {
		super(context, x, y);
		brain = new Brain();
		senses = new Senses(this);
		reflexes = new Reflexes(this);
		movement = new PlayerMovement(this);
		this.number = number;
		head = new Head(context, x, y, this);
		Iterator<Object> iter = context.getObjects(MessageBoard.class).iterator();
		this.messageBoard = (MessageBoard) iter.next();
	}
	
	protected void init(){	
		senses.init();
		brain.init();
		reflexes.init();
	}
	
	protected void step() {
		senses.step();
		brain.step();
		reflexes.step();
	}
	
	protected Brain getBrain(){
		return brain;
	}
	
}
