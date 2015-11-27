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
	private Senses senses;
	private Reflexes reflexes;
	private PlayerMovement movement;
	private Head head;
	private int number;
	private MessageBoard messageBoard;
	
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
		head = new Head(context, x, y, this);
		this.number = number;
		Iterator<Object> iter = context.getObjects(MessageBoard.class).iterator();
		this.messageBoard = (MessageBoard) iter.next();
	}
	
	void init(){	
		senses.init();
		brain.init();
		reflexes.init();
	}
	
	void step() {
		senses.step();
		brain.step();
		reflexes.step();
	}
	
	Brain getBrain(){
		return brain;
	}
	
	Senses getSenses(){
		return senses;
	}
	
	Reflexes getReflexes(){
		return reflexes;
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
	
	MessageBoard getMessageBoard(){
		return messageBoard;
	}
	
}
