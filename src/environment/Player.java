package environment;


import repast.simphony.context.Context;
import brain.Brain;

public class Player extends SimpleAgent {

	protected Brain brain;
	protected Senses senses;
	protected Reflexes reflexes;
	protected Movement movement;
	protected MotionVector motion;
	
	/**
	 * The constructor for the Player
	 * inits the parameters for space, grid and params
	 * @param space - the space projection
	 * @param grid - the grid projection
	 */
	Player(Context context, int x, int y) {
		super(context, x, y);
		brain = new Brain();
		senses = new Senses(this, context);
		reflexes = new Reflexes(this, context);
		movement = new Movement(this, context);
		motion = new MotionVector();
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
		updatePoints();
	}
	
	protected void updatePoints() {
		setPosition(getSpace().getLocation(this));
	}
	
	protected Brain getBrain(){
		return brain;
	}
	
}
