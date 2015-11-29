package environment;

import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StrictBorders;

public class ModelBuilder implements ContextBuilder<Object> {
	
	private Parameters params;
	private Context<Object> context;
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;

	/**
	 * Creates the context, init's variables, fills context with projections, objects, etc before returning the context
	 */
	@Override
	public Context build(Context<Object> context) {
		createContext(context);
		createParameters();
		createProjections();
		createScheduler();
		createMessageBoard();
		createField();
		createMover();
		createPlayers();
		createBall();
		createBoundary();
		createReferee();
		return this.context;
	}
	
	/**
	 * Creates the context and sets the context id to Footy (should match context in XML file)
	*/
	public void createContext(Context<Object> context) {
		this.context = context;
		this.context.setId("Footy");
	}

	/**
	 * Retrieves the parameters set by the user in the runtime environment
	 * Sets these parameters to the local parameters
	 */
	public void createParameters() {
		//takes the parameters from the runtime environment
		this.params = RunEnvironment.getInstance().getParameters();
	}
	
	/**
	 * Creates a scheduler to tell each class when and what to do
	 */
	public void createScheduler() {
		Scheduler sim = new Scheduler(context);
		context.add(sim);
	}
	
	/**
	 * Creates a referee to adjudicate the game
	 */
	public void createReferee(){
	}
	
	/**
	 * Creates a messageboard for audio messages to be sent
	 */
	public void createMessageBoard(){
	}
	
	/**
	 * Creates a mover that moves all movable objects at the end of each step and checks for collisions
	 */
	public void createMover(){
	}
	
	/**
	 * Creates both the continuous space and grid style layouts for display
	 */
	public void createProjections() {
		
		//sets the height and width of the boxed environment at runtime
		int displayHeight = (Integer)params.getValue("display_height");
		int displayWidth = (Integer)params.getValue("display_width");
				
		//Creates the space
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory(null);
		space = spaceFactory.createContinuousSpace("space", context, 
				new SimpleCartesianAdder<Object>(), 
				new repast.simphony.space.continuous.StrictBorders(),
				displayWidth, displayHeight);				
		
		//Creates the grid
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new StrictBorders(),
				new SimpleGridAdder<Object>(),
				true, displayWidth, displayHeight));
	}
	
	/**
	 * Initiates both create attackers and create defenders
	 */
	public void createPlayers() {
		createDefenders();
		createAttackers();
	}
	
	/**
	 * Creates the ball and adds it to the field at the position nominated in the parameters
	 */
	public void createBall(){
		int ballStartX = (Integer)params.getValue("ball_start_x");
		int ballStartY = (Integer)params.getValue("ball_start_y");
		new Ball(context, ballStartX, ballStartY);
	}
	
	/**
	 * Creates the defenders and positions them in the relevant spaces
	 * This should be relatively straight forward. We create a specified number of Defenders
	 * by looping through some creation code the specified number of times. 
	 * In this case we loop through the amount of times as specified in the parameters
	 * Note that the parameter’s value is returned as an Object so we need to cast it appropriately
	 * We add the new Defenders to context. 
	 * In adding them to the context we automatically add them to any projections associated with that context. 
	 * So in this case, the Defenders are added to the space and grid using their Adders as described above. 
	*/
	public void createDefenders() {
		
		// Retrieve defender count from runtime environment
				int defenderCount = (Integer)params.getValue("defender_count");
				
				// iterate through the count
				for(int i = 0; i < defenderCount; i++) {
					
					// Retrieve the starting positions from the runtime environment
					int defenderStartX = (Integer)params.getValue("defender_start_x");
					int defenderStartY = (Integer)params.getValue("defender_start_y");
					
					// Create a new defender for each iteration
					new Defender(context, defenderStartX, defenderStartY, i+1);
				}
	}
	
	/**
	 * Creates the Attackers and positions them in the relevant spaces
	 * This should be relatively straight forward. We create a specified number of Attackers
	 * by looping through some creation code the specified number of times. 
	 * In this case we loop through the amount of times as specified in the parameters
	 * Note that the parameter’s value is returned as an Object so we need to cast it appropriately
	 * We add the new Attackers to context. 
	 * In adding them to the context we automatically add them to any projections associated with that context. 
	 * So in this case, the Attackers are added to the space and grid using their Adders as described above. 
	 * The Attackers are created with a random energy level from 4 to 10. 
	 * We use the RandomHelper to do this for us. 
	 * In general, all random number type operations should be done through the RandomHelper. 
	*/
	public void createAttackers() {
		
		// Retrieve the attacker count from the runtime environment
		int attackerCount = (Integer)params.getValue("attacker_count");
		
		//iterate through the count
		for (int i = 0; i < attackerCount; i++) {
			
			// Retrieve the starting positions from the runtime environment
			int attackerStartX = (Integer)params.getValue("attacker_start_x");
			int attackerStartY = (Integer)params.getValue("attacker_start_y");
			int sideLine = (Integer)params.getValue("fieldInset");
			int fieldWidth = (Integer)params.getValue("display_height")-(2*(sideLine));
			
			int y;
			if(attackerCount==1){
				y = attackerStartY;
			} else {
				y = sideLine+((i+1)*(fieldWidth/(attackerCount+1)));
			}
			
			//create a new attacker for each iteration
			new Attacker(context, attackerStartX, y, i+1);
		}

	}

	/**
	 * Creates the field agent and places it in the middle of the screen
	 */
	public void createField() {
		int displayHeight = (Integer)params.getValue("display_height");
		int displayWidth = (Integer)params.getValue("display_width");
		new Field(context,displayWidth/2,displayHeight/2);
	}
	
	/**
	 * creates the boundary of the simulated field - 1 tryline and three sidelines
	 */
	public void createBoundary() {
		createSidelines();
		createTryline();
	}
	
	/**
	 * creates sideline points at each point along the upper, lower and right edges of the display
	 */
	public void createSidelines() {
		
		int southernSideline = (Integer)params.getValue("fieldInset");
		int northernSideline = (Integer)params.getValue("display_height")-(Integer)params.getValue("fieldInset");
		int westernTryline = (Integer)params.getValue("fieldInset")+(Integer)params.getValue("fieldIncrement");
		int easternTryline = (Integer)params.getValue("display_width")-(Integer)params.getValue("fieldInset")-(Integer)params.getValue("fieldIncrement");
		
		//create horizontal sidelines
		for (int i = westernTryline; i < easternTryline; i++) {
			
			//create upper and lower sidepoints
			new SidePoint(context, i, southernSideline);
			new SidePoint(context, i, northernSideline-1);
		}
		
		//create opposing tryline (sideline for this instance)
		for (int i = southernSideline; i < northernSideline; i++) {
			
			//create sidepoint
			new SidePoint(context, easternTryline-1, i);
		}
	}
	
	/**
	 * creates a new trypoint at each point along the left edge of the display
	 */
	public void createTryline() {
		int southernSideline = (Integer)params.getValue("fieldInset");
		int northernSideline = (Integer)params.getValue("display_height")-(Integer)params.getValue("fieldInset");
		int easternTryline = (Integer)params.getValue("fieldInset")+(Integer)params.getValue("fieldIncrement");
		for (int i = southernSideline; i < northernSideline; i++) {
			//create a new trypoint for each iteration
			new TryPoint(context, easternTryline, i);
		}
	}

}