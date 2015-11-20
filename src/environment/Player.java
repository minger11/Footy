package environment;


import brain.Brain;
import repast.simphony.context.Context;

public class Player extends SimpleAgent {

	protected Brain brain;
	protected Senses senses;
	protected Reflexes reflexes;
	protected Movement movement;
	protected Head head;
	protected int number;
	
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
		movement = new Movement(this);
		this.number = number;
		head = new Head(context, x, y, this);
	}
	
	protected void init(){
		//updatePoints();
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
